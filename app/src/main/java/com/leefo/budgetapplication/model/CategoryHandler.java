package com.leefo.budgetapplication.model;

import java.util.List;

/**
* Contains methods for handling categories in the database (getting, adding, modifying).
* */
public class CategoryHandler {

    /**
     * Method for getting all categories from the database.
     * @return A list of categories.
     */
    public List<Category> getCategories()
    {
        return DataBaseManager.getEveryCategory();
    }

    /**
     * Adds a new category to the database.
     * @param name Name of the category.
     * @param color Color of the category.
     */
    public void addCategory(String name, String color)
    {
        DataBaseManager.addCategory(name, color);
    }

    /**
     * Removes a category from the database given a specific category id.
     * @param id Id of category to remove.
     */
    public void removeCategory(int id)
    {
        DataBaseManager.deleteCategory(id);
    }

    /**
     * Used to edit a category already stored in database.
     * @param id Id of category to edit.
     * @param name New name of category.
     * @param color New color of category.
     */
    public void editCategory(int id, String name, String color)
    {
        DataBaseManager.editCategory( id,  name,  color);
    }
}
