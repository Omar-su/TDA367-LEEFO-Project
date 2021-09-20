package com.leefo.budgetapplication;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CategoryHandlerTest {

    @BeforeEach
    public void init() {
        Category testCategory = new Category(1, "Test Name", "#FFFF00");
        CategoryHandler ch = new CategoryHandler();
    }

    @Test
    public void canEditCategoryName() {
        Category mutatedCategory = ch.editTransactionName(testCategory, "New Name");
        assertEquals("New Name", mutatedCategory.getName());
    }
}
