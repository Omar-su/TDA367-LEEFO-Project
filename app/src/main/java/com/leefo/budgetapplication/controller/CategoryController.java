package com.leefo.budgetapplication.controller;

import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.CategoryModel;

import java.util.ArrayList;

/**
 * The CategoryController class represents a Controller in the Model-View-Controller pattern.
 * The class uses Singleton design pattern and therefore has getInstance() method and private constructor
 * The class responsibility is to listen to the View and respond by modifying category data and
 * updating the view.
 *
 * @author Felix Edholm, Linus Lundgren, Emelie Edberg, Eugene Dvoryankov
 */
public class CategoryController {

    /**
     * The field for storing singleton instance
     */
    private static CategoryController INSTANCE = null;

    /**
     * Object handling logic for categories
     */
    private static CategoryModel categoryModel;

    /**
     * The singleton's constructor should always be private to avoid direct calls with 'new" operator
     */
    private CategoryController(){}

    /** Returns single instance of the TransactionController class
     *
     * @return INSTANCE
     */
    public static CategoryController getInstance(){
        return INSTANCE;
    }

    /**
     * Edits the name and color of a category with the given id. Color must be a String of a hexadecimal
     * color code with the format: #XXXXXX.
     *
     * @param oldCategory Object of category to be changed.
     */
    public static void editCategoryInfo(Category oldCategory, String newName, String newColor, boolean isIncome, int goal) {
        Category newCategory = new Category(newName, newColor, isIncome, goal);

        categoryModel.editCategory(oldCategory, newCategory);
    }

    /**
     * Edits the name and color of a category with the given id. Color must be a String of a hexadecimal
     * color code with the format: #XXXXXX.
     *
     * @param oldCategory Object of category to be changed.
     */
    public static void editCategoryInfo(Category oldCategory, int goal) {
        Category newCategory = new Category(oldCategory.getName(), oldCategory.getColor(), oldCategory.isIncome(), goal);

        categoryModel.editCategory(oldCategory, newCategory);
    }

    /**
     * Adds a new category to the database. Color must be a String of a hexadecimal color code with
     * the format: #XXXXXX.
     *
     * @param name  The name of the new category.
     * @param color The color of the new category.
     */
    public static void addNewCategory(String name, String color, boolean isIncome) {
        Category newCategory = new Category(name, color, isIncome);

        categoryModel.addCategory(newCategory);
    }

    /**
     * Removes category with the given id from the database. Transactions under the removed category
     * are automatically moved to the Other category.
     *
     * @param category category to removed
     */
    public static void removeCategory(Category category) {
        categoryModel.deleteCategory(category);
    }

    /**
     * Returns a list of all the categories in the database.
     *
     * @return a list of all the categories in the database.
     */
    public static ArrayList<Category> getAllCategories() {
        return categoryModel.getCategoryList();
    }

    /**
     * Returns a list of all income categories in the model.
     *
     * @return A list of all income categories in the model.
     */
    public static ArrayList<Category> getIncomeCategories() {
        return categoryModel.getIncomeCategories();
    }

    /**
     * Returns a list of a all expense categories in the model.
     *
     * @return A list of all expense categories in the model.
     */
    public static ArrayList<Category> getExpenseCategories() {
        return categoryModel.getExpenseCategories();
    }

    /**
     * Sorts category list by alphabet
     *
     * @return A sorted category list .
     */
    public static ArrayList<Category> sortCategoriesByAlphabet(ArrayList<Category> sortList) {
        return categoryModel.sortCategoriesByAlphabet(sortList);
    }

    /**
     * Sorts category list by highest budget
     *
     * @return A sorted category list.
     */
    public static ArrayList<Category> sortCategoriesByBudget(ArrayList<Category> sortList) {
        return categoryModel.sortCategoriesByBudget(sortList);
    }
}
