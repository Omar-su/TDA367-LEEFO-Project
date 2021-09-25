package com.leefo.budgetapplication.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates the database needed for saving the transactions and categories information
 * and handling inserting, deleting and requesting any information that is in the database
 */
public class DataBaseManager extends SQLiteOpenHelper {

    private static final String CATEGORY_TABLE = "CATEGORY_TABLE";
    private static final String CATEGORY_ID = "CATEGORY_ID";
    private static final String CATEGORY_NAME = "CATEGORY_NAME";
    private static final String CATEGORY_COLOR = "CATEGORY_COLOR";

    private static final String TRANSACTIONS_TABLE = "TRANSACTIONS_TABLE";
    private static final String TRANSACTIONS_ID = "TRANSACTIONS_ID";
    private static final String TRANSACTIONS_DESC = "TRANSACTIONS_DESCRIPTION";
    private static final String TRANSACTION_DATE = "TRANSACTION_DATE";
    private static final String TRANSACTION_AMOUNT = "TRANSACTION_AMOUNT";
    private static final String CATEGORY_FK_ID = "TRANS_FOREIGN_ID";


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

        String createTableCategory = " CREATE TABLE " + CATEGORY_TABLE + " ( " + CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                    + CATEGORY_NAME + " TEXT UNIQUE, " + CATEGORY_COLOR + " TEXT)";

        String createTableTransactions = " CREATE TABLE " + TRANSACTIONS_TABLE + " ( " + TRANSACTIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                        + TRANSACTION_AMOUNT + " REAL, "
                                        + TRANSACTIONS_DESC + " TEXT, "
                                        + TRANSACTION_DATE + " TEXT, "
                                        + CATEGORY_FK_ID + " INTEGER, FOREIGN KEY(" + CATEGORY_FK_ID + ") REFERENCES " + CATEGORY_TABLE + " ( " + CATEGORY_ID + " ) ON DELETE SET NULL)";

        sqLiteDatabase.execSQL(createTableCategory);
        sqLiteDatabase.execSQL(createTableTransactions);

    }


    /**
     * Deletes the old database and creates a new database with new changes
     * since there are no changes tín the tables structure and the database it self,
     * this method is not needed and only here because it needs to be implemented
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
     * @param categoryID Which category id the transaction relates to
     * @return Returns true if a transaction was inserted successfully and false if not
     * It can be used for testing if the method has done its job
     */
    public boolean addTransaction(String description, float amount, String date, int categoryID ){

        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TRANSACTIONS_DESC, description);
        cv.put(TRANSACTION_AMOUNT, amount);
        cv.put(TRANSACTION_DATE, date);
        cv.put(CATEGORY_FK_ID, categoryID);

        long insert = db.insert(TRANSACTIONS_TABLE, null, cv);
        db.close();
        return insert != -1;

    }

    /**
     * Adds a category into the database
     * @param catName The name of the category
     * @param catColor The color of the category
     * @return Returns true if a category was inserted successfully and false if not
     * It can be used for testing if the method has done its job
     */
    public boolean addCategory(String catName, String catColor){

        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_NAME, catName);
        cv.put(CATEGORY_COLOR, catColor);
        long insert = db.insert(CATEGORY_TABLE, null, cv);
        db.close();

        return insert != -1;

    }


    /**
     * Deletes a specific transaction from the database
     * @param transId The transaction id needed to know which category to delete
     * @return Returns true if the transaction is deleted and false if it is not found
     */
    public boolean deleteTransaction(int transId){

        SQLiteDatabase db = instance.getWritableDatabase();
        String sql = "DELETE FROM " + TRANSACTIONS_TABLE + " WHERE " + TRANSACTIONS_ID + " = " + transId;
        Cursor cursor = db.rawQuery(sql, null);
        boolean successful = cursor.moveToFirst();
        db.close();
        cursor.close();
        return successful;

    }


    /**
     * Deletes a specific category from the database and calls a method
     * that changes the category id of all transactions with the same category id.
     * @param catId The category id needed to know which category to delete
     * @return Return true if the category has been deleted and false if it is not found
     * It can be used to test if the method has done its job
     */
    public boolean deleteCategory(int catId){

        SQLiteDatabase db = instance.getWritableDatabase();
        String sql = "DELETE FROM " + CATEGORY_TABLE + " WHERE " + CATEGORY_ID + " = " + catId;
        updateTransactionCatID(catId, db); // updates the transactions which category was deleted to the "Other" category ID
        Cursor cursor = db.rawQuery(sql, null);
        boolean successful = cursor.moveToFirst();
        db.close();
        cursor.close();
        return successful;
    }


    private void updateTransactionCatID(int catId, SQLiteDatabase db) {
        String sql = " UPDATE "+ TRANSACTIONS_TABLE + " SET "+ CATEGORY_FK_ID + " = " + getCatOtherID(db) + " WHERE " + CATEGORY_FK_ID + " = " + catId;
        db.execSQL(sql);
    }

    private int getCatOtherID(SQLiteDatabase db) {
        String queryString = "SELECT * FROM " + CATEGORY_TABLE + " WHERE " + CATEGORY_NAME + " = 'Other'";
        Cursor cursor = db.rawQuery(queryString,null);
        int catOtherID = 1;
        if (cursor.moveToFirst()){
            catOtherID = cursor.getInt(0);
        }
        cursor.close();
        return catOtherID;
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
                int categoryID = cursor.getInt(0);
                String categoryName = cursor.getString(1);
                String categoryColor = cursor.getString(2);
                Category newCategory = new Category(categoryID,categoryName, categoryColor);
                returnList.add(newCategory);

            }while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return returnList;


    }


    /**
     * Gets a specific category from the database
     * @param catId The id of the category that is wanted
     * @return returns the wanted category
     */
    public Category getCategoryById(int catId){



        String queryString = " SELECT * FROM " + CATEGORY_TABLE +" WHERE " + CATEGORY_ID + " = " + catId;
        Category category = new Category(catId, "", "") ;
        SQLiteDatabase db = instance.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);


        if (cursor.moveToFirst()){
            do {
                int categoryID = cursor.getInt(0);
                String categoryName = cursor.getString(1);
                String categoryColor = cursor.getString(2);
                category = new Category(categoryID,categoryName, categoryColor);

            }while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return category;

    }



    /**
     * Creates transaction objects with the data that is stored in the database by specifying the date
     * @param year The year the transaction was made
     * @param month The month the transaction was made
     * @return Returns a list of all the transactions that is in a specific month and year
     */
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
                int categoryFKID = cursor.getInt(4);

                Transaction newTransaction = new Transaction(transactionID, transactionAmount ,transactionDesc, transactionDate, categoryFKID);
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
                int categoryFKID = cursor.getInt(4);

                Transaction newTransaction = new Transaction(transactionID, transactionAmount ,transactionDesc, transactionDate, categoryFKID);
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
     * @param categoryId The id of the category that all the wanted transactions have
     * @return Returns a list of all the transactions that is in a specific month and year with a specific category
     */
    public ArrayList<Transaction> getTransactionsByMonthAndCat(String year, String month, int categoryId) {
        ArrayList<Transaction> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TRANSACTIONS_TABLE + " WHERE "+ TRANSACTION_DATE + " BETWEEN " + "'" + year + "-"  + month + "-" + "01' "
                + " AND " + "'" + year + "-"  + month + "-" + "31'" + " AND " + CATEGORY_FK_ID + " = " + categoryId + " ORDER BY " + TRANSACTION_DATE + " DESC " ;

        SQLiteDatabase db = instance.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                int transactionID = cursor.getInt(0);
                int transactionAmount = cursor.getInt(1);
                String transactionDesc = cursor.getString(2);
                String transactionDate = cursor.getString(3);
                int categoryFKID = cursor.getInt(4);

                Transaction newTransaction = new Transaction(transactionID, transactionAmount ,transactionDesc, transactionDate, categoryFKID);
                returnList.add(newTransaction);

            }while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return returnList;


    }


    /**
     * Edits the category in the database
     * @param id The id of the category that needs editing
     * @param name The new name of the category
     * @param color The new color of the category
     * @return Return true if the category has been edited and false if it is not found
     * It can be used to test if the method has done its job
     */
    public boolean editCategory(int id, String name, String color) {

        SQLiteDatabase db = instance.getWritableDatabase();
        String sql = " UPDATE " + CATEGORY_TABLE + " SET " + CATEGORY_NAME + " = " + name +" WHERE " + CATEGORY_ID + " = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        boolean successful = cursor.moveToFirst();
        db.close();
        cursor.close();
        return successful;

    }


    /**
     * Edits the transaction in the database
     * @param id The id of the transaction that needs to change
     * @param amount The transaction new amount
     * @param description Transaction new description
     * @param date The new date of the transaction
     * @param catId The related category id for the transaction
     * @return Return true if the transaction has been edited and false if it is not found
     * It can be used to test if the method has done its job
     */
    public boolean editTransaction(int id, float amount, String description, String date, int catId) {

        SQLiteDatabase db = instance.getWritableDatabase();
        String sql = " UPDATE " + TRANSACTIONS_TABLE + " SET " + TRANSACTION_AMOUNT + " = " + amount + ","
                    + TRANSACTIONS_DESC + " = " + description + " , "
                    + TRANSACTION_DATE + " = " + date + " , "
                    + CATEGORY_FK_ID + " = " + catId
                    +" WHERE " + CATEGORY_ID + " = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        boolean successful = cursor.moveToFirst();
        db.close();
        cursor.close();
        return successful;

    }


}
