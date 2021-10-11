package com.leefo.budgetapplication.model;


import java.util.ArrayList;

public class CategoryModel
{

    /**
     * The list of Categories used in the application
     */
    private final ArrayList<Category> categoryList;

    private final IDatabase database;

    public CategoryModel(IDatabase database)
    {
        this.database = database;
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
        if (category == getOtherExpenseCategory()) return; // not allowed tp remove that one
        if (category == getOtherIncomeCategory()) return; // not allowed to remove that one

        if (category.isIncome()) {
            for (int i = 0; i < getTransactionList().size(); i++) {
                FinancialTransaction t = getTransactionList().get(i);

                if (category.transactionBelongs(t)) {
                    editTransaction(t, new FinancialTransaction(t.getAmount(), t.getDescription(),
                            t.getDate(), getOtherIncomeCategory()));
                }
            }
        } else {
            for (int i = 0; i < getTransactionList().size(); i++) {
                FinancialTransaction t = getTransactionList().get(i);

                if (category.transactionBelongs(t)) {
                    editTransaction(t, new FinancialTransaction(t.getAmount(), t.getDescription(),
                            t.getDate(), getOtherExpenseCategory()));
                }
            }
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
        addCategory(editedCategory);
        replaceTransactionsCategory(oldCategory, editedCategory);
        deleteCategory(oldCategory);
    }

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




    // database methods ------

    private ArrayList<Category> getCategoriesFromDatabase()
    {
        return database.getCategories();
    }

    private void saveCategoryToDatabase(Category category)
    {
        database.saveData(category);
    }

    private void deleteCategoryFromDatabase(Category category)
    {
        database.removeData(category);
    }

}
