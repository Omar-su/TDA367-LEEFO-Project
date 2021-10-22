package com.leefo.budgetapplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.FilterOption;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.model.SearchSortFilterTransactions;
import com.leefo.budgetapplication.model.SortOption;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SearchSortFilterTest {

    Category testCategoryExpense;
    SearchSortFilterTransactions ssf;
    FinancialTransaction t1;
    FinancialTransaction t2;
    FinancialTransaction t3;
    FinancialTransaction t4;

    @BeforeEach
    public void init() {
        LocalDate d1 = LocalDate.now();
        LocalDate d2 = d1.minusDays(2);
        LocalDate d3 = d1.plusDays(2);
        Category testCategoryExpense = new Category("test", "#FFFFFF", false);
        Category testCategoryIncome = new Category("test", "#FFFFFF", true);

         t1 = new FinancialTransaction((float) 10, "candy", d1, testCategoryExpense);
         t2 = new FinancialTransaction((float) 50, "socks", d2, testCategoryExpense);
         t3 = new FinancialTransaction((float) 5, "taxi", d1, testCategoryExpense);
         t4 = new FinancialTransaction((float) 20, "rent", d3, testCategoryIncome);

        List<FinancialTransaction> list = new ArrayList<>();

        list.add(t1);
        list.add(t2);
        list.add(t3);
        list.add(t4);

        ssf = new SearchSortFilterTransactions(list);
    }
    @Test
    public void CanFilterOnlyExpense(){
        ssf.setFilter(FilterOption.EXPENSE);
        List<FinancialTransaction> result = ssf.getResult();

        assertEquals(result.size(), 3);
    }
    @Test
    public void CanFilterOnlyIncome(){
        ssf.setFilter(FilterOption.INCOME);
        List<FinancialTransaction> result = ssf.getResult();

        assertEquals(result.size(), 1);
    }
    @Test
    public void CanSortByOldestDate(){
        List<FinancialTransaction> expected = new ArrayList<>();
        expected.add(t2);
        expected.add(t3);
        expected.add(t1);
        expected.add(t4);

        ssf.sortBy(SortOption.OLDEST_DATE);
        List<FinancialTransaction> result = ssf.getResult();

        assertTrue(expected.get(0).equals(result.get(0)) && expected.get(1).equals(result.get(1)) && expected.get(2).equals(result.get(2))&& expected.get(3).equals(result.get(3)));
    }
    @Test
    public void CanSortByNewestDate(){
        List<FinancialTransaction> expected = new ArrayList<>();
        expected.add(t4);
        expected.add(t1);
        expected.add(t3);
        expected.add(t2);

        ssf.sortBy(SortOption.NEWEST_DATE);
        List<FinancialTransaction> result = ssf.getResult();

        assertTrue(expected.get(0).equals(result.get(0)) && expected.get(1).equals(result.get(1)) && expected.get(2).equals(result.get(2))&& expected.get(3).equals(result.get(3)));
    }
    @Test
    public void CanSortBySmallestAmount(){
        List<FinancialTransaction> expected = new ArrayList<>();
        expected.add(t3);
        expected.add(t1);
        expected.add(t4);
        expected.add(t2);

        ssf.sortBy(SortOption.SMALLEST_AMOUNT);
        List<FinancialTransaction> result = ssf.getResult();

        assertTrue(expected.get(0).equals(result.get(0)) && expected.get(1).equals(result.get(1)) && expected.get(2).equals(result.get(2))&& expected.get(3).equals(result.get(3)));
    }
    @Test
    public void CanSortByLargestAmount(){
        List<FinancialTransaction> expected = new ArrayList<>();
        expected.add(t2);
        expected.add(t4);
        expected.add(t1);
        expected.add(t3);

        ssf.sortBy(SortOption.LARGEST_AMOUNT);
        List<FinancialTransaction> result = ssf.getResult();

        assertTrue(expected.get(0).equals(result.get(0)) && expected.get(1).equals(result.get(1)) && expected.get(2).equals(result.get(2))&& expected.get(3).equals(result.get(3)));
    }

    @Test
    public void CanSearchByAmount(){
        ssf.setSearchString("50");
        List<FinancialTransaction> result = ssf.getResult();

        assertEquals(result.size(), 1);
        assertEquals(t2, result.get(0));
    }

    @Test
    public void CanSearchByNote(){
        ssf.setSearchString("taxi");
        List<FinancialTransaction> result = ssf.getResult();

        assertEquals(result.size(), 1);
        assertEquals(t3, result.get(0));
    }
}
