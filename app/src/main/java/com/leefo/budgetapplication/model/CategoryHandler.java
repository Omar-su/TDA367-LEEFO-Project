package com.leefo.budgetapplication.model;

import java.util.ArrayList;

/**
* Contains methods for handling categories in the database (getting, adding, modifying).
* */
public class CategoryHandler extends ObserverHandler {

    /**
     * Manager for handling database requests.
     */
    private final DataBaseManager database;

    public CategoryHandler()
    {
        database = DataBaseManager.getInstance();
    }

    /**
     * Method for getting all categories from the database.
     * @return A list of categories.
     */
    public ArrayList<Category> getEveryCategory()
    {
        return database.getEveryCategory();
    }

    /**
     * Adds a new category to the database.
     * @param name Name of the category.
     * @param color Color of the category.
     */
    public void addCategory(String name, String color)
    {
        database.addCategory(name, color);

        updateObservers(); // updates views
    }

    /**
     * Removes a category from the database given a specific category id.
     * @param id Id of category to remove.
     */
    public void removeCategory(int id)
    {
        database.deleteCategory(id);

        updateObservers(); // updates views
    }

    /**
     * Used to edit a category already stored in database.
     * @param id Id of category to edit.
     * @param name New name of category.
     * @param color New color of category.
     */
    public void editCategory(int id, String name, String color)
    {
        database.editCategory( id,  name,  color);

        updateObservers(); // updates views
    }

    /**
     * Gets the category corresponding to a given category id.
     * @param id id of the category wished to get.
     * @return the Category corresponding to the given id.
     */
    public Category getCategoryFromId(int id){
        for (Category c : getEveryCategory()){
            if (c.getId() == id){
                return c;
            }
        }
        return new Category(0, "Other", "#C4C4C4");
    }
}
