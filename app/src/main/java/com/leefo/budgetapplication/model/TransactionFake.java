package com.leefo.budgetapplication.model;

public class TransactionFake {

    private int amount;
    private String category;
    private String color;

    public TransactionFake(int amount, String category, String color) {
        this.amount = amount;
        this.category = category;
        this.color = color;
    }

    public int getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getColor() {
        return color;
    }
}
