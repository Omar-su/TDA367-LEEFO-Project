package com.leefo.budgetapplication;

import org.junit.Test;

public class TransactionHandlerTest {

    @Test
    public void cantSetAmountAsText() {
        double amount = 14.5;
        Transaction testTransaction = new Transaction(1, amount, "Food from mcDonalds", 20210203, 3);
        TransactionHandler th = new TransactionHandler();
        th.editAmount("ssta");
        assertEquals(amount, testTransaction.getAmount());
    }

    @Test
    public void canChangeAmount() {
        double oldAmount = 14.5;
        Transaction testTransaction = new Transaction(1, oldAmount, "Food from mcDonalds", 20210203, 3);
        TransactionHandler th = new TransactionHandler();
        double newAmount = 17.0;
        th.editAmount(newAmount);
        assertEquals(newAmount, testTransaction.getAmount());

    }
}
