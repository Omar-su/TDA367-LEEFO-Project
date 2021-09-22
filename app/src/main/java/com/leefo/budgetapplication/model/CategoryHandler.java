package com.leefo.budgetapplication.model;

import java.util.List;

/**
* Contains static methods for handling categories in the database (getting, adding, modifying).
* */
public class CategoryHandler {

    // TODO MAKE METHODS NOT STATIC AND CREATE INSTANCES OF CATEGORYHANDLER IN CONTROLLER

    /**
     * Method for getting all categories from the database.
     * @return A list of categories.
     */
    public static List<Category> getCategories()
    {
        return DataBaseManager.getEveryCategory();
    }

    /**
     * Adds a new category to the database.
     * @param name Name of the category.
     * @param color Color of the category.
     */
    public static void addCategory(String name, String color)
    {
        Category cat = new Category(name, color);// todo change so that a new category object doesn't have to be made

        DataBaseManager.addCategory(cat.getName(), cat.getColor());
    }

    /**
     * Removes a category from the database given a specific category id.
     * @param id Id of category to remove.
     */
    public static void removeCategory(int id)
    {
        DataBaseManager.deleteCategory(id);
    }

    /**
     * Used to edit a category already stored in database.
     * @param id Id of category to edit.
     * @param name New name of category.
     * @param color New color of category.
     */
    public static void editCategory(int id, String name, String color)
    {
        Category editedCategory = new Category(id, name, color);

        DataBaseManager.editCategory( id,  name,  color); // todo don't use category objects
    }
}