package com.leefo.budgetapplication.controller;

import com.leefo.budgetapplication.model.CategoryHandler;
import com.leefo.budgetapplication.model.TransactionHandler;

public class Controller {

    TransactionHandler transactionHandler;
    CategoryHandler categoryHandler;
    //View view;


    public Controller() {
        this.transactionHandler = new TransactionHandler();
        this.categoryHandler = new CategoryHandler();
    }

    public void editCategoryInfo(int id, String name, String color){
        categoryHandler.editCategory(id, name, color);
    }

    public void addNewCategory(String name, String color){
        categoryHandler.addCategory(name, color);
    }

    public void removeCategory(int id){
        categoryHandler.removeCategory(id);
    }


}
