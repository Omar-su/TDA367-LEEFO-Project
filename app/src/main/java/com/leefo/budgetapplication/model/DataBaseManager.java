package com.leefo.budgetapplication.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates the database needed for saving the transactions and categories information
 * and handling inserting, deleting and requesting any information that is in the database
 *
 *@author Omar Sulaiman
 */
public class DataBaseManager extends SQLiteOpenHelper implements IDatabase {

    private static final String CATEGORY_TABLE = "CATEGORY_TABLE";
    private static final String CATEGORY_NAME = "CATEGORY_NAME";
    private static final String CATEGORY_COLOR = "CATEGORY_COLOR";

    private static final String TRANSACTIONS_TABLE = "TRANSACTIONS_TABLE";
    private static final String TRANSACTIONS_DESC = "TRANSACTIONS_DESCRIPTION";
    private static final String TRANSACTION_DATE = "TRANSACTION_DATE";
    private static final String TRANSACTION_AMOUNT = "TRANSACTION_AMOUNT";
    private static final String CATEGORY_FK_NAME = "TRANS_FOREIGN_ID";
    private static final String CATEGORY_IS_INCOME = "CATEGORY_IS_INCOME";
    public static final String TRANSACTION_ID = "TRANSACTION_ID";


    public DataBaseManager(@Nullable Context context) {
        super(context, "category_transaction_db_v1", null, 3);
    }

    //New name will create new database file. Sending in a null String object will create
    //an in memory database meaning that no file will be created on disk and the database will be
    //destroyed when the application is closed. This is useful for testing.
    public DataBaseManager(@Nullable Context context, @Nullable String name) {
        super(context, name, null, 3);
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
                                    + CATEGORY_IS_INCOME + " INTEGER " + " )";

        String createTableTransactions = " CREATE TABLE " + TRANSACTIONS_TABLE + " ( "  + TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                        + TRANSACTION_AMOUNT + " REAL, "
                                        + TRANSACTIONS_DESC + " TEXT, "
                                        + TRANSACTION_DATE + " TEXT, "
                                        + CATEGORY_FK_NAME + " TEXT)";

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
     * Saves a financial transactions information in the database
     * @param transaction Transaction to be saved.
     */
    public void saveData(FinancialTransaction transaction)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TRANSACTIONS_DESC, transaction.getDescription());
        cv.put(TRANSACTION_AMOUNT, transaction.getAmount());
        cv.put(TRANSACTION_DATE, transaction.getDate().toString());
        cv.put(CATEGORY_FK_NAME, transaction.getCategory().getName());

        db.insert(TRANSACTIONS_TABLE, null, cv);
        db.close();

    }

    /**
     * Saves the categories information in the database
     * @param category Category to be saved.
     */
    public void saveData(Category category)
    {
        int i = category.isIncome() ? 1 : 0;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_NAME, category.getName());
        cv.put(CATEGORY_COLOR, category.getColor());
        cv.put(CATEGORY_IS_INCOME, i);
        db.insert(CATEGORY_TABLE, null, cv);
        db.close();
    }

    /**
     * Deletes only one transaction from the database that matches the data of the transaction given
     * @param transaction Transaction to be removed.
     */
    public void removeData(FinancialTransaction transaction)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        int deletedTransaction = getDeletedTransaction(transaction, db);

        // Deletes a random transaction from identical transactions and a specific transaction if there is one that satisfies the requirements
        String sql = "DELETE FROM " + TRANSACTIONS_TABLE + " WHERE " + TRANSACTION_ID + " = " + deletedTransaction;
        db.execSQL(sql);

        db.close();

    }

    /**
     * Checks if there are several identical rows that satisfy the requirements of the transaction's data
     * and gets the id of a random one
     * @param transaction The transaction that is to be deleted
     * @param db Access to the database
     * @return Returns an id of one transaction if there are identical transactions
     */
    private int getDeletedTransaction(FinancialTransaction transaction, SQLiteDatabase db) {
        // Gets all the identical rows/transactions in the database
        String queryString = "SELECT * FROM " + TRANSACTIONS_TABLE + " WHERE "  + TRANSACTIONS_DESC + " = '" + transaction.getDescription() + "' AND "
                + TRANSACTION_DATE + " = '" + transaction.getDate().toString() + "' AND " + TRANSACTION_AMOUNT + " = " + transaction.getAmount() + " AND "
                + CATEGORY_FK_NAME + " = '" + transaction.getCategory().getName() + "'";


        Cursor cursor = db.rawQuery(queryString, null);
        cursor.moveToFirst();
        // Saves the id of one of the identical transactions
        int deletedTransaction = cursor.getInt(0);
        cursor.close();

        return deletedTransaction;
    }

    /**
     * Removes a certain removable category's information from the database
     * @param category Category to be removed.
     */
    public void removeData(Category category)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + CATEGORY_TABLE + " WHERE " + CATEGORY_NAME + " = '" + category.getName() + "'";
        db.execSQL(sql);
        db.close();

    }




    /**
     * Created all categories that are in the database
     * @return Returns a list of all categories's information in the database as category objects
     */
    public ArrayList<Category> getCategories(){

        ArrayList<Category> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + CATEGORY_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
                String categoryName = cursor.getString(0);
                String categoryColor = cursor.getString(1);
                int categoryIsIncome = cursor.getInt(2);
                boolean b = categoryIsIncome == 1;
                Category newCategory = new Category(categoryName, categoryColor, b);
                returnList.add(newCategory);

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
    public ArrayList<FinancialTransaction> getFinancialTransactions(){
        ArrayList<Category> categories = getCategories();

        ArrayList<FinancialTransaction> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TRANSACTIONS_TABLE + " ORDER BY " + TRANSACTION_DATE + " DESC ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            do {
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

                String year, month, day;
                year = transactionDate.substring(0,4);
                month = transactionDate.substring(5,7);
                day = transactionDate.substring(8,10);

                LocalDate date = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

                FinancialTransaction newTransaction = new FinancialTransaction(transactionAmount ,transactionDesc, date, category);
                returnList.add(newTransaction);

            }while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return returnList;

    }



}
