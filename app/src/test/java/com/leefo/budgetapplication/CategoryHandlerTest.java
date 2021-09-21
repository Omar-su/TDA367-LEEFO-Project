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
        String newName = "New name";
        ch.editCategory(testCategory.getId(), newName , testCategory.getColor());
        List<Category> catList = ch.getCategories();
        for (Category cat : catList){
            if(cat.getId() == testCategory.getId()){
                assertEquals(newName, cat.getName());
                break;
            }
        }
    }

    @Test
    public void canEditCategoryColor() {
        String newColor = "#7393B3";
        ch.editCategory(testCategory.getId(), testCategory.getName(), newColor);
        List<Category> catList = ch.getCategories();
        for (Category cat : catList) {
            if (cat.getId() == testCategory.getId()) {
                assertEquals(newName, cat.getName());
                break;
            }
        }
    }
}
