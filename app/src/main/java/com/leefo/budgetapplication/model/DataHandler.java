package com.leefo.budgetapplication.model;

import java.util.List;

public class DataHandler {

    private final List<Transaction> transactionList;

    private final List<Category> categoryList;

    private final Category otherIncome = new Category(0,"Other income", "#13702A", true);

    private final Category otherExpense = new Category(1,"Other expense", "701313", false);


    public DataHandler(List<Transaction> transactionList, List<Category> categoryList) {
        this.transactionList = transactionList;
        this.categoryList = categoryList;
    }

    public void addTransaction(Transaction transaction){
        transactionList.add(transaction);
    }

    public void deleteTransaction(Transaction transaction){
        transactionList.remove(transaction);
    }

    public void addCategory(Category category){
        categoryList.add(category);
    }

    public void deleteCategory(Category category){
        if(category.isIncome()){
            for(Transaction t : transactionList){
                if(t.getCategory().isEqual(category)){
                    t.setCategory(otherIncome);
                }
            }
        }else{
            for(Transaction t : transactionList){
                if(t.getCategory().isEqual(category)){
                    t.setCategory(otherExpense);
                }
            }
        }
        categoryList.remove(category);
    }
}
