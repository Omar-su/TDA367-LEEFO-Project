package com.leefo.budgetapplication.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Creates the database needed for saving the transactions and categories information
 * and handling inserting, deleting and requesting any information that is in the database
 */
public class DataBaseManager extends SQLiteOpenHelper {

    private static final String CATEGORY_TABLE = "CATEGORY_TABLE";
    private static final String CATEGORY_NAME = "CATEGORY_NAME";
    private static final String CATEGORY_COLOR = "CATEGORY_COLOR";

    private static final String TRANSACTIONS_TABLE = "TRANSACTIONS_TABLE";
    private static final String TRANSACTIONS_ID = "TRANSACTIONS_ID";
    private static final String TRANSACTIONS_DESC = "TRANSACTIONS_DESCRIPTION";
    private static final String TRANSACTION_DATE = "TRANSACTION_DATE";
    private static final String TRANSACTION_AMOUNT = "TRANSACTION_AMOUNT";
    private static final String CATEGORY_FK_NAME = "TRANS_FOREIGN_ID";
    private static final String CATEGORY_IS_INCOME = "CATEGORY_IS_INCOME";


    private static DataBaseManager instance;

    /**
     * @return Instance of database manager
     */
    public static synchronized DataBaseManager getInstance()
    {
        return instance;
    }

    /**
     * Initializes database with given context
     * @param context Main activity context.
     */
    public static void initialize(Context context)
    {
        instance = new DataBaseManager(context);
    }

    private void initOtherCategory(){
        ArrayList<Category> categories = getEveryCategory();
        for (Category c : categories){
            if (c.getName().equals("Other")) return;
        }
        addCategory("Other", "#8A9094", true);
    }


    private DataBaseManager(@Nullable Context context) {
        super(context, "category_transaction_db", null, 1);
    }


    /**
     * Makes foreign key constraint available to implement which makes it possible
     * to have a connection between categories and transactions
     * @param db The class that grants access to the database
     */
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    /**
     * Creates the tables needed to save categories and transactions
     * @param sqLiteDatabase The class that grants access to the database
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createTableCategory = " CREATE TABLE " + CATEGORY_TABLE + " ( " + CATEGORY_NAME + " TEXT PRIMARY KEY, "
                                    + CATEGORY_COLOR + " TEXT, "
                                    + CATEGORY_IS_INCOME + " INTGER " + " )";

        String createTableTransactions = " CREATE TABLE " + TRANSACTIONS_TABLE + " ( " + TRANSACTIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                        + TRANSACTION_AMOUNT + " REAL, "
                                        + TRANSACTIONS_DESC + " TEXT, "
                                        + TRANSACTION_DATE + " TEXT, "
                                        + CATEGORY_FK_NAME + " INTEGER, FOREIGN KEY(" + CATEGORY_FK_NAME + ") REFERENCES " + CATEGORY_TABLE + " ( " + CATEGORY_NAME + " ) ON DELETE SET NULL)";

        sqLiteDatabase.execSQL(createTableCategory);
        sqLiteDatabase.execSQL(createTableTransactions);

    }


    /**
     * Deletes the old database and creates a new database with new changes
     * since there are no changes ín the table's structure and the database itself,
     * this method is not needed and is only here because it needs to be implemented
     * @param sqLiteDatabase The class needed to access the database
     * @param i The old version of the database
     * @param i1 The new version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    /**
     * Adds a transaction into the database
     * @param description The description of the transaction
     * @param amount The amount of money spent or earned
     * @param date The date the transaction was made
     * @param categoryName Which category name the transaction relates to
     */
    public void addTransaction(String description, float amount, String date, int categoryName ){

        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TRANSACTIONS_DESC, description);
        cv.put(TRANSACTION_AMOUNT, amount);
        cv.put(TRANSACTION_DATE, date);
        cv.put(CATEGORY_FK_NAME, categoryName);

        db.insert(TRANSACTIONS_TABLE, null, cv);
        db.close();
    }

    /**
     * Adds a category into the database
     * @param catName The name of the category
     * @param catColor The color of the category
     * @param catIsIncome Decides if the category is an income or an expense
     */
    public void addCategory(String catName, String catColor, boolean catIsIncome){
        int i = catIsIncome ? 1 : 0;

        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_NAME, catName);
        cv.put(CATEGORY_COLOR, catColor);
        cv.put(CATEGORY_IS_INCOME, i);
        db.insert(CATEGORY_TABLE, null, cv);
        db.close();
    }


    /**
     * Deletes a specific transaction from the database
     * @param transId The transaction id needed to know which category to delete
     */
    public void deleteTransaction(int transId){
        SQLiteDatabase db = instance.getWritableDatabase();
        String sql = "DELETE FROM " + TRANSACTIONS_TABLE + " WHERE " + TRANSACTIONS_ID + " = " + transId;
        db.execSQL(sql);
        db.close();
    }


    /**
     * Deletes a specific category from the database and calls a method
     * that changes the category id of all transactions with the same category id.
     * @param catName The category name needed to know which category to delete
     */
    public void deleteCategory(String catName){
        initOtherCategory();
        SQLiteDatabase db = instance.getWritableDatabase();
        if ("Other".equals(catName)) return; // you cannot remove the category called Other
        String sql = "DELETE FROM " + CATEGORY_TABLE + " WHERE " + CATEGORY_NAME + " = " + catName;
        updateTransactionCatID(catName, db); // updates the transactions which category was deleted to the "Other" category ID
        db.execSQL(sql);
        db.close();
    }


    private void updateTransactionCatID(String catName, SQLiteDatabase db) {
        String sql = " UPDATE "+ TRANSACTIONS_TABLE + " SET "+ CATEGORY_FK_NAME + " =  'Other'" + " WHERE " + CATEGORY_FK_NAME + " = " + catName;
        db.execSQL(sql);
    }


    /**
     * Created all categories that are in the database
     * @return Returns a list of all category objects in the database
     */
    public ArrayList<Category> getEveryCategory(){

        ArrayList<Category> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + CATEGORY_TABLE;

        SQLiteDatabase db = instance.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                String categoryName = cursor.getString(0);
                String categoryColor = cursor.getString(1);
                int categoryIsIncome = cursor.getInt(2);
                boolean b = categoryIsIncome == 1;
                Category newCategory = new Category(categoryName, categoryColor, b); //TODO
                returnList.add(newCategory);

            }while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return returnList;
    }


    /*
     * Gets a specific category from the database
     * @param CatName The name of the category that is wanted
     * @return returns the wanted category
     */ /*
    public Category getCategoryByName(String CatName){
        initOtherCategory();

        String queryString = " SELECT * FROM " + CATEGORY_TABLE +" WHERE " + CATEGORY_NAME + " = " + CatName;
        SQLiteDatabase db = instance.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);
        String categoryName = cursor.getString(0);
        String categoryColor = cursor.getString(1);
        int categoryIsIncome = cursor.getInt(2);
        cursor.close();

        Category category = new Category(categoryName, categoryColor, categoryIsIncome); // TODO
        db.close();
        return category;
    }
    */



    /*
     * Creates transaction objects with the data that is stored in the database by specifying the date
     * @param year The year the transaction was made
     * @param month The month the transaction was made
     * @return Returns a list of all the transactions that is in a specific month and year
     */
    /*
    public ArrayList<Transaction> getTransactionsByMonth(String year, String month){


        ArrayList<Transaction> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TRANSACTIONS_TABLE + " WHERE "+ TRANSACTION_DATE + " BETWEEN " + "'" + year + "-"  + month + "-" + "01' "
                            + " AND " + "'" + year + "-"  + month + "-"  + "31' ORDER BY " + TRANSACTION_DATE + " DESC ";

        SQLiteDatabase db = instance.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                int transactionID = cursor.getInt(0);
                int transactionAmount = cursor.getInt(1);
                String transactionDesc = cursor.getString(2);
                String transactionDate = cursor.getString(3);
                String categoryFKName = cursor.getString(4);

                Transaction newTransaction = new Transaction(transactionID, transactionAmount ,transactionDesc, transactionDate, categoryFKName);
                returnList.add(newTransaction);

            }while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return returnList;

    }


    /**
     * Creates transaction objects of all the transactions registered in the database
     * @return Returns a list of all transactions in he database
     */
    public ArrayList<Transaction> getAllTransactions(){
        ArrayList<Category> categories = getEveryCategory();

        ArrayList<Transaction> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TRANSACTIONS_TABLE + " ORDER BY " + TRANSACTION_DATE + " DESC ";

        SQLiteDatabase db = instance.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                int transactionID = cursor.getInt(0);
                int transactionAmount = cursor.getInt(1);
                String transactionDesc = cursor.getString(2);
                String transactionDate = cursor.getString(3);
                String categoryFKName = cursor.getString(4);
                Category category = new Category("", "", true);
                for (Category c : categories){
                    if (categoryFKName.equals(c.getName())){
                        category = c;
                    }
                }

                Transaction newTransaction = new Transaction(transactionAmount ,transactionDesc, transactionDate, category);
                returnList.add(newTransaction);

            }while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return returnList;

    }


    /**
     * Creates transaction objects with the data that is stored in the database by specifying the date and the category
     * @param year The year the transaction was made
     * @param month The month the transaction was made
     * @param categoryName The name of the category that all the wanted transactions have
     * @return Returns a list of all the transactions that is in a specific month and year with a specific category
     */
    /*
    public ArrayList<Transaction> getTransactionsByMonthAndCat(String year, String month, String categoryName) {
        ArrayList<Transaction> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TRANSACTIONS_TABLE + " WHERE "+ TRANSACTION_DATE + " BETWEEN " + "'" + year + "-"  + month + "-" + "01' "
                + " AND " + "'" + year + "-"  + month + "-" + "31'" + " AND " + CATEGORY_FK_NAME + " = '" + categoryName + "' ORDER BY " + TRANSACTION_DATE + " DESC " ;

        SQLiteDatabase db = instance.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                int transactionID = cursor.getInt(0);
                int transactionAmount = cursor.getInt(1);
                String transactionDesc = cursor.getString(2);
                String transactionDate = cursor.getString(3);
                String categoryFKName = cursor.getString(4);

                Transaction newTransaction = new Transaction(transactionID, transactionAmount ,transactionDesc, transactionDate, categoryFKName);
                returnList.add(newTransaction);

            }while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return returnList;


    }


    /**
     * Edits the category in the database
     * @param name The name of the category that needs editing
     * @param color The new color of the category
     * @param catIsIncome Decides if the category is an income or an expense
     */
    /*
    public void editCategory(String name, String color, int catIsIncome) {

        SQLiteDatabase db = instance.getWritableDatabase();
        String sql = " UPDATE " + CATEGORY_TABLE + " SET " + CATEGORY_NAME + " = " + name
                    + CATEGORY_COLOR + " = " + color
                    + CATEGORY_IS_INCOME + " = " + catIsIncome +" WHERE " + CATEGORY_NAME + " = " + name;
        db.execSQL(sql);
        db.close();
    }


    /**
     * Edits the transaction in the database
     * @param id The id of the transaction that needs to change
     * @param amount The transaction new amount
     * @param description Transaction new description
     * @param date The new date of the transaction
     * @param catName The related category name for the transaction
     */
    /*
    public void editTransaction(int id, float amount, String description, String date, int catName) {

        SQLiteDatabase db = instance.getWritableDatabase();
        String sql = " UPDATE " + TRANSACTIONS_TABLE + " SET " + TRANSACTION_AMOUNT + " = " + amount + ","
                    + TRANSACTIONS_DESC + " = " + description + " , "
                    + TRANSACTION_DATE + " = " + date + " , "
                    + CATEGORY_FK_NAME + " = " + catName
                    +" WHERE " + TRANSACTIONS_ID + " = " + id;

        db.execSQL(sql);
        db.close();

    }


     */

}
