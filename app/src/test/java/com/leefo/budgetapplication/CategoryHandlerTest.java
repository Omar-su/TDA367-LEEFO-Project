package com.leefo.budgetapplication;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;



import android.content.Context;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.leefo.budgetapplication.controller.Controller;
import com.leefo.budgetapplication.model.Category;


import java.util.List;
import com.leefo.budgetapplication.view.MainActivity;
import org.mockito.Mock;
import static org.mockito.Mockito.when;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

public class CategoryHandlerTest {
/*
    @Mock
    private Context mockAppContext;


    @BeforeEach
    public void init(){
        Context c = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Controller.InitializeBackend(c);
    }
    @Test
    public void canEditCategoryName() {
        Category testCategory = new Category(1, "Test Name", "#FFFF00");
        CategoryHandler ch = new CategoryHandler();
        String newName = "New name";
        ch.editCategory(testCategory.getId(), newName , testCategory.getColor());
        List<Category> catList = ch.getEveryCategory();
        for (Category cat : catList){
            if(cat.getId() == testCategory.getId()){
                assertEquals(newName, cat.getName());
                break;
            }
        }
    }
/*
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
    }*/
}
