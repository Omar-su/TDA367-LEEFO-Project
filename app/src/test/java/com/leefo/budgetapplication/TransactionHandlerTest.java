package com.leefo.budgetapplication;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionHandlerTest {

    @BeforeEach
    public void init(){
        Transaction testTransaction = new Transaction(1, 14.5, "Foor from mcDonalds, 20210203, 3");
        TransactionHandler th = new TransactionHandler();
    }

    @Test
    public void cantSetAmountAsText() {
        Transaction mutatedTransaction = th.editAmount(testTransaction, "ssta");
        assertEquals(amount, mutatedTransaction.getAmount());
    }

    @Test
    public void canChangeAmount() {
        double newAmount = 17.0;
        Transaction mutatedTransaction = th.editAmount(testTransaction, newAmount);
        assertEquals(newAmount, mutatedTransaction.getAmount());
    }

}
