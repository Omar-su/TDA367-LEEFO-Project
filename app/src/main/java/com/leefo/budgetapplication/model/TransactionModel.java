package com.leefo.budgetapplication.model;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The TransactionModel class contains methods for manipulating, adding and removing categories and
 * transactions. The TransactionModel class also saves every change it does to the database for
 * persistence storage.
 *
 * @author Felix Edholm, Emelie Edberg, Linus Lundgren, Omar Suliman
 */
public class TransactionModel implements ITransactionModel {

    /**
     * The list of FinancialTransactions used in the application
     */
    private  final ArrayList<FinancialTransaction> transactionList;

    /**
     * Interface used to interact with database. Used for reading and writing transactions and categories.
     */
    private final IDatabase database;


    /**
     * Constructor for creating a TransactionModel instance.
     */
    public TransactionModel(IDatabase database) {
        this.database = database;
        transactionList = getFinancialTransactionsFromDatabase();
    }



    /**
     * Adds a financial transaction to the list of financial transactions at appropriate index by date (lower index --> more recent).
     * Also saves the changes to the persistence storage.
     * @param newTransaction The financial transaction to be added.
     */
    public void addTransaction(FinancialTransaction newTransaction) {

        // loops through transactions to find where new transaction should be inserted
        for(int i = 0; i < transactionList.size(); i++)
        {
            FinancialTransaction transaction = transactionList.get(i);

            // If the transaction is made after the transaction in the list
            // Then the transaction should be inserted in the position of the transaction that is in the current index
            if(transaction.getDate().isBefore(newTransaction.getDate()))
            {
                transactionList.add(i, newTransaction); // adds transaction at index i (note: does NOT replace)
                saveTransactionToDatabase(newTransaction); // saves to database for persistence

                ObserverHandler.updateObservers(); // updates views
                return;
            }
        }

        transactionList.add(newTransaction);

        saveTransactionToDatabase(newTransaction); // saves to database for persistence

        ObserverHandler.updateObservers(); // updates views
    }

    /**
     * Deletes a financial transaction from the list of financial transactions.
     * Also saves the changes to the persistence storage.
     * @param transaction The financial transaction to be deleted.
     */
    public void deleteTransaction(FinancialTransaction transaction) {
        transactionList.remove(transaction);

         deleteTransactionFromDatabase(transaction);

        ObserverHandler.updateObservers();
    }

    /**
     * Edits the information of a financial transaction.
     * Also saves the changes to the persistence storage.
     * @param oldTransaction The transaction with the information to be edited.
     * @param editedTransaction The transaction with the edited information.
     */
    public void editTransaction(FinancialTransaction oldTransaction, FinancialTransaction editedTransaction){
        deleteTransaction(oldTransaction);
        addTransaction(editedTransaction);
    }


    /**
     * Replaces category object in all transactions with oldCategory to newCategory.
     * @param oldCategory Category that is going to be replaced.
     * @param newCategory Category that will replace oldCategory.
     */
    @Override
    public void replaceCatForTransactions(Category oldCategory, Category newCategory){
        for(int i = 0; i < getTransactionList().size(); i++){
            FinancialTransaction t = getTransactionList().get(i);

            if(oldCategory.transactionBelongs(t)){
                editTransaction(t, new FinancialTransaction(t.getAmount(), t.getDescription(), t.getDate(), newCategory));
            }
        }

    }

    /**
     * Returns a copy of the transactionList
     * @return copy if transactionList
     */
    public ArrayList<FinancialTransaction> getTransactionList() {
        return new ArrayList<>(transactionList);
    }


    /**
     * Gets the sum of the transactions that match the parameters in the TransactionRequest object.
     * @param request Object containing search parameters.
     * @return The sum of the FinancialTransactions that match the search parameters in request object.
     */
    public float getTransactionSum(TransactionRequest request){
        float sum = 0;
        for (FinancialTransaction t : searchTransactions(request)){
            sum = sum + t.getAmount();
        }
        return Math.abs(sum);
    }

    /**
     * Returns sum of all transactions that is of an income category.
     * @param request Request specifying date.
     * @return Sum of all income in specified date.
     */
    public float getTotalIncome(TransactionRequest request)
    {
        float sum = 0;

        for(FinancialTransaction transaction : getTransactionList())
        {
            LocalDate date = transaction.getDate();

            // skips transaction if date value doesn't match
            if(request.timeIsSpecified())
                if(date.getMonth().getValue() != request.getMonth() || date.getYear() != request.getYear())
                    continue;

            // adds only if income
            if(transaction.getCategory().isIncome())
                sum += transaction.getAmount();
        }

        return sum;
    }

    /**
     * Gets sum of all transactions of type expense.
     * @param request Request specifying date.
     * @return Sum of all expenses in specified date.
     */
    public float getTotalExpense(TransactionRequest request)
    {
        float sum = 0;

        for(FinancialTransaction transaction : getTransactionList())
        {
            LocalDate date = transaction.getDate();

            // skips transaction if date value doesn't match
            if(request.timeIsSpecified())
                if(date.getMonth().getValue() != request.getMonth() || date.getYear() != request.getYear())
                    continue;

            // adds only if expense
            if(!transaction.getCategory().isIncome())
                sum += transaction.getAmount();
        }

        return sum;
    }

    /**
     * Gets balance between total income and total expenses
     * @param request Request specifying date.
     * @return Difference between total income and total expenses.
     */
    public float getTransactionBalance(TransactionRequest request)
    {
        return getTotalIncome(request) + getTotalExpense(request);
    }

    /**
     * Method to search through transactionList with search parameters from a TransactionRequest object.
     * @param request Object containing search parameters.
     * @return A list containing FinancialTransactions matching search parameters.
     */
    public ArrayList<FinancialTransaction> searchTransactions(TransactionRequest request)
    {
        ArrayList<FinancialTransaction> result = new ArrayList<>();

        // loops through every transaction
        for(FinancialTransaction transaction : getTransactionList())
        {
            int transactionYear = transaction.getDate().getYear();
            int transactionMonth = transaction.getDate().getMonthValue();

            // moves on to next transaction if current transaction does not match time specification
            if(request.timeIsSpecified())
                if(request.getYear() != transactionYear || request.getMonth() != transactionMonth)
                    continue;

            // moves on to next transaction if current transaction does not match specified category
            if(request.categoryIsSpecified())
                if(!request.getCategory().Equals(transaction.getCategory()))
                    continue;

            result.add(transaction);
        }

        return result;
    }

    /**
     * Removes categories from list which have zero transactions in a given time period.
     * @param list to be worked on.
     * @param request specifies the time period.
     */
    public void removeEmptyCategories(ArrayList<Category> list, TransactionRequest request){
        for (int i = 0; i < list.size(); i++){
            Category c = list.get(i);
            request.setCategory(c);
            if (searchTransactions(request).isEmpty()){
                list.remove(c);
                i--;
            }
        }
    }

    /**
     * Sorts a given category list based on the sum of transactions belonging to the category i a specific time period.
     * Categories with largest sum gets the lowest index in the list.
     * @param list the list to be sorted.
     * @param request specifies the time period.
     */
    public void sortCategoryListBySum(ArrayList<Category> list, TransactionRequest request){
        for (int x = 0; x < list.size() ; x++){
            for (int i = 0; i < list.size()-1; i++){
                float sum1 = getTransactionSum(new TransactionRequest(list.get(i), request.getMonth(), request.getYear()));
                float sum2 = getTransactionSum(new TransactionRequest(list.get(i+1), request.getMonth(), request.getYear()));
                if (sum1 < sum2){
                    swap(list, i, i+1);
                }
            }
        }
    }

    // methods for sorting categories by most popular (data from 20 recent transactions) -----

    /**
     * Returns a list with the 20 newest transactions from transactionList.
     * If there isn't 20 transactions in transactionList then the ones that exist will be returned.
     * @return list with up to 20 transactions.
     */
    private ArrayList<FinancialTransaction> get20latestTransactions(){

        if (getTransactionList().size() < 20) return getTransactionList();

        ArrayList<FinancialTransaction> list = new ArrayList<>();
        int i = 0;
        while (i < 20){
            list.add(getTransactionList().get(i++));
        }
        return list;
    }

    /**
     * Returns the number of times a category is used inside a list of transactions.
     * @param list the list with transactions.
     * @param category the category to be counted.
     * @return number of times the category was used.
     */
    private int getCategoryCount(ArrayList<FinancialTransaction> list, Category category){
        int count = 0;
        for (FinancialTransaction t : list){
            if (category.Equals(t.getCategory())){
                count++;
            }
        }
        return count;
    }

    /**
     * Sorts a category list based on how many times it was used in the latest 20 transactions.
     * Larger amount means lower list index.
     * @param categoryList list to be sorted
     */
    public void sortCategoryListByPopularity(ArrayList<Category> categoryList){

        ArrayList<FinancialTransaction> data = get20latestTransactions();

        for (int x = 0; x < categoryList.size() ; x++){
            for (int i = 0; i < categoryList.size()-1; i++){
                int count1 = getCategoryCount(data, categoryList.get(i));
                int count2 = getCategoryCount(data, categoryList.get(i+1));
                if (count1 < count2){
                    swap(categoryList, i, i+1);
                }
            }
        }
    }


    // dataBase methods ----

    /**
     * Save a transaction in the database.
     * @param transaction to be saved.
     */
    private void saveTransactionToDatabase(FinancialTransaction transaction){
        database.saveData(transaction);
    }

    /**
     * Delete a transaction from the database.
     * @param transaction to be deleted.
     */
    private void deleteTransactionFromDatabase(FinancialTransaction transaction){
        database.removeData(transaction);
    }



    /**
     * Get all transactions stored in the database.
     * @return list of transactions.
     */
    private ArrayList<FinancialTransaction> getFinancialTransactionsFromDatabase(){

        ArrayList<FinancialTransaction> transactions = database.getFinancialTransactions();

        // transactions may not be in order when retrieved from database, so they must be sorted.
        // lower index means that the transaction has been made more recently.
        sortTransactionsByDate(transactions);

        return transactions; // should be sorted by date
    }

    // sorting ----

    /**
     * Sorts an array list of transactions so that the most recent transactions will have a lower index.
     *
     * Uses the bubble sort algorithm. https://en.wikipedia.org/wiki/Bubble_sort
     *
     * @param transactions List to be sorted.
     */
    private void sortTransactionsByDate(ArrayList<FinancialTransaction> transactions)
    {
        boolean notCompleted = true; // will be set to false in the last loop through the list of transactions

        while (notCompleted)
        {
            notCompleted = false; // will be reset to true if a swap is made

            // will be iterated until the list is sorted
            for(int i = 0; i < transactions.size() - 1; i++)
            {
                FinancialTransaction first = transactions.get(i);
                FinancialTransaction second = transactions.get(i+1);

                // lower index transactions should be most recent
                if (first.getDate().isBefore(second.getDate())) {
                    swap(transactions, i, i + 1); // swaps transactions in array list
                    notCompleted = true;
                }
            }
        }
    }


    /**
     * Swaps the position of two objects in a list.
     *
     * @param list List in which they will be swapped.
     * @param i1 Index of first object.
     * @param i2 Index of second object.
     */
    private <T> void swap(ArrayList<T> list, int i1, int i2)
    {
        T temp = list.get(i1); // stores i1 temporarily

        list.set(i1, list.get(i2)); // sets i1 to i2
        list.set(i2, temp); // sets i2 to temp
    }


}
