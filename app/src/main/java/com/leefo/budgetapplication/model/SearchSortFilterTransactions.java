package com.leefo.budgetapplication.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The class handles searching, sorting and filtering of transactions.
 * A user of this class provides the class with a source list. And uses methods to choose options for sorting anf filtering
 * and choosing a search string. From the chosen source list and control options the user can get the result.
 *
 * The class uses Enumeration SortOption and FilterOption.
 * The class is used by HomeListViewFragment.
 * @author Emelie Edberg
 */
public class SearchSortFilterTransactions {

    /**
     * The list of transactions to be searched, sorted and filtered.
     */
    private ArrayList<FinancialTransaction> sourceData;

    /**
     * The current chosen sorting option.
     */
    private SortOption sortOption = SortOption.NEWEST_DATE;
    /**
     * The current chosen filter.
     */
    private FilterOption filterOption = FilterOption.ALL_CATEGORIES;

    /**
     * String to be searched on.
     */
    private String searchString = "";


    public SearchSortFilterTransactions(ArrayList<FinancialTransaction> sourceData) {
        this.sourceData = sourceData;
    }

    /**
     * Set a new list of source data.
     * @param sourceData The new list.
     */
    public void updateSourceData(ArrayList<FinancialTransaction> sourceData) {
        this.sourceData = sourceData;
    }

    /**
     * Chose a sort option.
     * @param sortOption chosen sort option.
     */
    public void sortBy(SortOption sortOption){
        this.sortOption = sortOption;
    }

    /**
     * Chose a filter.
     * @param filterOption chosen filter option.
     */
    public void setFilter(FilterOption filterOption){
        this.filterOption = filterOption;
    }

    /**
     * Set string for searching.
     * @param searchString String to be searched on.
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    /**
     * Get the result from chosen search, sort and filter params and source data.
     * @return List containing result of search, sort and filter.
     */
    public ArrayList<FinancialTransaction> getResult(){
        ArrayList<FinancialTransaction> result = new ArrayList<>(sourceData);

        switch (sortOption){
            case NEWEST_DATE: sortByNewestDate(result); break;
            case OLDEST_DATE: sortByOldestDate(result); break;
            case LARGEST_AMOUNT: sortByLargestAmount(result); break;
            case SMALLEST_AMOUNT: sortBySmallestAmount(result); break;
        }

        switch (filterOption){
            case ALL_CATEGORIES: break;
            case EXPENSE: removeIncomeTransactions(result); break;
            case INCOME: removeExpenseCategories(result); break;
        }

        ArrayList<FinancialTransaction> searchResultNote;
        ArrayList<FinancialTransaction> searchResultAmount = new ArrayList<>();

        searchResultNote = searchOnNote(result, searchString);

        try {
            Float amount = Float.valueOf(searchString);
            searchResultAmount = searchOnAmount(result, amount);

        } catch (NumberFormatException e){}

        return getListUnion(searchResultNote, searchResultAmount);
    }

    /**
     * Method that crates a new list with the contents from two lists. No duplicates.
     * @param list1 First list.
     * @param list2 Second list.
     * @return The union of the two lists.
     */
    private <T> ArrayList<T> getListUnion(ArrayList<T> list1, ArrayList<T> list2){
        ArrayList<T> union = new ArrayList<>(list1);
        for (T t : list2){
            if (!union.contains(t)){
                union.add(t);
            }
        }
        return union;
    }


    /**
     * Searches the transaction list to find transactions that match the searched text by note description.
     * @param list the the list of transactions to check.
     * @param note the searched note description.
     * @return the list with transactions that are matching the note description.
     * @author Eugene Dvorankov
     */
    private ArrayList<FinancialTransaction> searchOnNote(ArrayList <FinancialTransaction> list, String note){
        ArrayList<FinancialTransaction> result = new ArrayList<>();
        for(FinancialTransaction transaction : list){
            if(transaction.getDescription().toLowerCase().contains(note.toLowerCase())){
                result.add(transaction);
            }
        }
        return result;
    }

    /**
     * Searches the transaction list to find transactions that match the searched text by amount
     * @param list the the list of transactions to check
     * @param amount the searched amount
     * @return the list with the transactions that are matching the amount
     * @author Eugene Dvorankov
     */
    private ArrayList<FinancialTransaction> searchOnAmount(ArrayList <FinancialTransaction> list, Float amount){
        ArrayList<FinancialTransaction> result = new ArrayList<>();
        for(FinancialTransaction transaction : list){
            float amount1 = Math.abs(transaction.getAmount());
            if(amount1==amount)
                result.add(transaction);
        }
        return result;
    }

    /**
     * Sorts an array list of transactions so that the most recent transactions will have a lower index.
     *
     * Uses the bubble sort algorithm. https://en.wikipedia.org/wiki/Bubble_sort
     *
     * @param list List to be sorted.
     * @author Linus Lundgren
     */
    private void sortByNewestDate(ArrayList<FinancialTransaction> list) {
        boolean notCompleted = true; // will be set to false in the last loop through the list of transactions
        while (notCompleted)
        {
            notCompleted = false; // will be reset to true if a swap is made

            // will be iterated until the list is sorted
            for(int i = 0; i < list.size() - 1; i++)
            {
                FinancialTransaction first = list.get(i);
                FinancialTransaction second = list.get(i+1);

                // lower index transactions should be most recent
                if (first.getDate().isBefore(second.getDate())) {
                    swap(list, i, i + 1); // swaps transactions in array list
                    notCompleted = true;
                }
            }
        }
    }

    /**
     * Sorts a transaction list based on the date.
     * Older date means lower list index.
     * @param list list to be sorted
     */
    private void sortByOldestDate(ArrayList<FinancialTransaction> list){
        sortByNewestDate(list);
        Collections.reverse(list);
    }

    /**
     * Sorts a transaction list based on the transaction amount.
     * Larger amount means lower list index.
     * @param list list to be sorted
     */
    private void sortByLargestAmount(ArrayList<FinancialTransaction> list){

        for (int x = 0; x < list.size() ; x++){
            for (int i = 0; i < list.size()-1; i++){
                float amount1 = Math.abs(list.get(i).getAmount());
                float amount2 = Math.abs(list.get(i+1).getAmount());
                if (amount1 < amount2){
                    swap(list, i, i+1);
                }
            }
        }
    }

    /**
     * Sorts a transaction list based on the transaction amount.
     * Smaller amount means lower list index.
     * @param list list to be sorted
     */
    private void sortBySmallestAmount(ArrayList<FinancialTransaction> list){
        sortByLargestAmount(list);
        Collections.reverse(list);
    }

    /**
     * Removes all income transactions.
     * @param list The list to be filtered.
     */
    private void removeIncomeTransactions(ArrayList <FinancialTransaction> list){
        for(int i = 0; i < list.size(); i++){
            FinancialTransaction transaction = list.get(i);
            if(transaction.getCategory().isIncome()){
                list.remove(transaction);
                i--;
            }
        }
    }

    /**
     * Removes all expense transactions.
     * @param list The list to be filtered.
     */
    private void removeExpenseCategories(ArrayList <FinancialTransaction> list){
        for(int i = 0; i < list.size(); i++){
            FinancialTransaction transaction = list.get(i);
            if(!transaction.getCategory().isIncome()){
                list.remove(transaction);
                i--;
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
