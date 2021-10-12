package com.leefo.budgetapplication.model;

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
    public void replaceCategory(Category oldCategory, Category newCategory){
        for(int i = 0; i < getTransactionList().size(); i++){
            FinancialTransaction t = getTransactionList().get(i);

            if(oldCategory.transactionBelongs(t)){
                editTransaction(t, new FinancialTransaction(t.getAmount(), t.getDescription(), t.getDate(),
                        newCategory));
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


    //TODO implement these methods

    /**
     * Gets the sum of the transactions that match the parameters in the TransactionRequest object.
     * @param request Object containing search parameters.
     * @return The sum of the FinancialTransactions that match the search parameters in request object.
     */
    public float getTransactionSum(TransactionRequest request){
        //  finished i think, just wrote this quick.

        float sum = 0;
        for (FinancialTransaction t : searchTransactions(request)){
            sum = sum + t.getAmount();
        }
        return Math.abs(sum);
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

            // adds transaction if category matches any of the ones in the request
            // if no categories are specified, belongsToCategories() returns true
            if(request.belongsToCategories(transaction))
                result.add(transaction);
        }

        return result;
    }

    public ArrayList<Category> removeEmptyCategories(ArrayList<Category> list, TransactionRequest request){
        ArrayList<Category> notEmpty = new ArrayList<>();
        for (Category c : list){
            request.setCategory(c);
            if (!searchTransactions(request).isEmpty()){
                notEmpty.add(c);
            }
        }
        return notEmpty;
    }

    // largest first
    public ArrayList<Category> sortCategoryListBySum(ArrayList<Category> list, TransactionRequest request){
        for (int x = 0; x < list.size() ; x++){
            for (int i = 0; i < list.size()-1; i++){
                float sum1 = getTransactionSum(new TransactionRequest(list.get(i), request.getMonth(), request.getYear()));
                float sum2 = getTransactionSum(new TransactionRequest(list.get(i+1), request.getMonth(), request.getYear()));
                if (sum1 < sum2){
                    swap(list, i, i+1);
                }
            }
        }
        return list;
    }


    // dataBase methods ----
    private void saveTransactionToDatabase(FinancialTransaction transaction){
        database.saveData(transaction);
    }

    private void deleteTransactionFromDatabase(FinancialTransaction transaction){
        database.removeData(transaction);
    }


    /**
     * Method for getting all transactions in database.
     * @return Returns all transactions currently stored in database.
     */
    private ArrayList<FinancialTransaction> getFinancialTransactionsFromDatabase(){

        ArrayList<FinancialTransaction> transactions = database.getFinancialTransactions();

        // transactions may not be in order when retrieved from database, so they must be sorted.
        // lower index means that the transaction has been made more recently.
        bubbleSortTransactions(transactions);

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
    private void bubbleSortTransactions(ArrayList<FinancialTransaction> transactions)
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
