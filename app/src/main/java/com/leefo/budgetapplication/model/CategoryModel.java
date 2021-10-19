package com.leefo.budgetapplication.model;


import java.util.ArrayList;

public class CategoryModel
{

    /**
     * The list of Categories used in the application
     */
    private final ArrayList<Category> categoryList;

    private final IDatabase database;

    /**
     * Interface for transaction model. Only used for replacing deleted/edited categories
     * in transactions.
     */
    private final ITransactionModel transactionModel;

    public CategoryModel(IDatabase database, ITransactionModel transactionModel)
    {
        this.database = database;
        this.transactionModel = transactionModel;
        categoryList = getCategoriesFromDatabase();

        initDefaultCategories();
    }


    private void initDefaultCategories() {
        for (Category c : getCategoryList()){
            if (c.getName().equals("Other income")){
                return;
            }
        }
        setDefaultCategories();
    }

    private void setDefaultCategories(){
        // Other
        addCategory(new Category("Other income", "#C4C4C4", true,0));
        addCategory(new Category("Other expense", "#C4C4C4", false,0));

        // Expenses
        addCategory(new Category("Home", "#FF6464", false,0));
        addCategory(new Category("Food", "#64FF7D", false,0));
        addCategory(new Category("Transportation", "#64BEFF", false,0));
        addCategory(new Category("Clothes", "#FF64DD", false,0));
        addCategory(new Category("Entertainment", "#FFAE64", false,0));
        addCategory(new Category("Electronics", "#64FFEC",false,0));

        //Income
        addCategory(new Category("Salary", "#FCFF64", true,0));
        addCategory(new Category("Gift", "#6473FF", true,0));
    }

    /**
     * Adds a category to the list of categories. Also saves the changes to the persistence storage.
     * @param category The category to be added.
     */
    public void addCategory(Category category) {
        categoryList.add(category);

        saveCategoryToDatabase(category);

        ObserverHandler.updateObservers();
    }

    /**
     * Deletes a category from the list of categories. If a FinancialTransaction is of the deleted
     * category, that FinancialTransactions category is switched to a default category.
     * Also saves the changes to the persistence storage.
     * @param category The category to be deleted.
     */
    public void deleteCategory(Category category) {
        if (category == getOtherExpenseCategory()) return; // not allowed to remove
        if (category == getOtherIncomeCategory()) return; // not allowed to remove

        if (category.isIncome()) {
            transactionModel.replaceCatForTransactions(category, getOtherIncomeCategory());
        } else {
            transactionModel.replaceCatForTransactions(category, getOtherExpenseCategory());
        }
        categoryList.remove(category);

        deleteCategoryFromDatabase(category);

        ObserverHandler.updateObservers();
    }

    /**
     * Edits the information of a category.
     * Also saves the changes to the persistence storage.
     * @param oldCategory The category with the information to be edited.
     * @param editedCategory The category with the edited information.
     */
    public void editCategory(Category oldCategory, Category editedCategory){
        transactionModel.replaceCatForTransactions(oldCategory, editedCategory);
        updateCategory(oldCategory,editedCategory);
    }

    private void updateCategory(Category oldCategory, Category editedCategory) {
        deleteCategoryBudget(oldCategory);
        addCategoryBudget(editedCategory);
        ObserverHandler.updateObservers();
    }

    private void addCategoryBudget(Category category) {
        categoryList.add(category);
        saveCategoryToDatabase(category);
    }

    private void deleteCategoryBudget(Category category) {
        categoryList.remove(category);
        deleteCategoryFromDatabase(category);
    }


    // getters -------------

    /**
     * Returns a copy of the categoryList
     * @return copy if categoryList
     */
    public ArrayList<Category> getCategoryList() {
        return new ArrayList<>(categoryList);
    }

    /**
     * Returns a list of income categories.
     * @return a list of income categories.
     */
    public ArrayList<Category> getIncomeCategories(){
        ArrayList<Category> list = new ArrayList<>();
        for (Category c : getCategoryList()){
            if (c.isIncome()){
                list.add(c);
            }
        }
        return list;
    }

    /**
     * Returns a list of expense categories.
     * @return a list of expense categories.
     */
    public ArrayList<Category> getExpenseCategories(){
        ArrayList<Category> list = new ArrayList<>();
        for (Category c : getCategoryList()){
            if (!c.isIncome()){
                list.add(c);
            }
        }

        return list;
    }



    /**
     * Sorts category list by alphabet
     * @param categoryList The list to be sorted
     * @return A sorted category list by alphabet
     */
    public ArrayList<Category> sortCategoriesByAlphabet(ArrayList<Category> categoryList) {
        for (int j = 0; j<categoryList.size();j++){
            for (int i =j+1; i<categoryList.size(); i++){
                if (getFirstLetter(categoryList.get(j)) > getFirstLetter(categoryList.get(i))){
                    switchCategoryPlaces(categoryList, j, i);
                }
            }
        }
        return categoryList;


    }

    /**
     * Switches places between two specific categories in a list
     * @param categoryList The category list
     * @param j Which index the first category has
     * @param i Which index the second category has
     */
    private void switchCategoryPlaces(ArrayList<Category> categoryList, int j, int i) {
        Category tmpCat = categoryList.get(i);
        categoryList.set(i, categoryList.get(j));
        categoryList.set(j, tmpCat);
    }

    /**
     * Gets and makes the first letter a lower case to make it comparable
     * @param category The category that is to be compared
     * @return The first letter of the category name as a char in lowercase
     */
    private char getFirstLetter(Category category) {
        return category.getName().toLowerCase().charAt(0);
    }

    /**
     * Sorts category by highest budget
     * @param categoryList List to be sorted
     * @return A category list sorted by highest budget
     */
    public ArrayList<Category> sortCategoriesByBudget(ArrayList<Category> categoryList) {
        for (int i = 0; i<categoryList.size();i++){
            for (int j =i+1; j<categoryList.size(); j++){
                if (categoryList.get(i).getBudgetGoal()<categoryList.get(j).getBudgetGoal()){
                    switchCategoryPlaces(categoryList, i, j);
                }
            }
        }
        return categoryList;
    }


    private Category getOtherIncomeCategory(){
        for (Category c : getCategoryList()){
            if (c.getName().equals("Other income")){
                return c;
            }
        }
        return null; // will never happen
    }

    private Category getOtherExpenseCategory(){
        for (Category c : getCategoryList()){
            if (c.getName().equals("Other expense")){
                return c;
            }
        }
        return null; // will never happen
    }




    // database methods --------

    /**
     * Get all categories stored in the database.
     * @return list of categories.
     */
    private ArrayList<Category> getCategoriesFromDatabase()
    {
        return database.getCategories();
    }

    /**
     * Save a category in the database.
     * @param category to be saved.
     */
    private void saveCategoryToDatabase(Category category){
        database.saveData(category);
    }

    /**
     * Delete a category from the database.
     * @param category to be deleted.
     */
    private void deleteCategoryFromDatabase(Category category){
        database.removeData(category);
    }

}
