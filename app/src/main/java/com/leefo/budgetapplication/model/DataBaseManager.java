package com.leefo.budgetapplication.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

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

    public static synchronized DataBaseManager getInstance(@Nullable Context context){

        if (instance == null)
            instance = new DataBaseManager(context);

        return instance;
    }


    private DataBaseManager(@Nullable Context context) {
        super(context, "category_transaction_db", null, 1);
    }




    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createTableCategory = " CREATE TABLE " + CATEGORY_TABLE + " ( " + CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                    + CATEGORY_NAME + " TEXT, " + CATEGORY_COLOR + " TEXT)";

        String createTableTransactions = " CREATE TABLE " + TRANSACTIONS_TABLE + " ( " + TRANSACTIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                        + TRANSACTION_AMOUNT + " REAL, "
                                        + TRANSACTIONS_DESC + " TEXT, "
                                        + TRANSACTION_DATE + " TEXT, "
                                        + CATEGORY_FK_ID + " INTEGER, FOREIGN KEY(" + CATEGORY_FK_ID + ") REFERENCES " + CATEGORY_TABLE + " ( " + CATEGORY_ID + " ) ON DELETE SET NULL)";

        sqLiteDatabase.execSQL(createTableCategory);
        sqLiteDatabase.execSQL(createTableTransactions);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }



    public static boolean addTransaction(String description, Double amount, String date, int categoryID ){

        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TRANSACTIONS_DESC, description);
        cv.put(TRANSACTION_AMOUNT, amount);
        cv.put(TRANSACTION_DATE, date);
        cv.put(CATEGORY_FK_ID, categoryID);

        long insert = db.insert(TRANSACTIONS_TABLE, null, cv);

        return insert != -1;

    }

    public static boolean addCategory(String catName, String catColor){

        SQLiteDatabase db = instance.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_NAME, catName);
        cv.put(CATEGORY_COLOR, catColor);
        long insert = db.insert(CATEGORY_TABLE, null, cv);

        return insert != -1;

    }

    public static boolean deleteTransaction(int transId){

        SQLiteDatabase db = instance.getWritableDatabase();
        String sql = "DELETE FROM " + TRANSACTIONS_TABLE + " WHERE " + TRANSACTIONS_ID + " = " + transId;
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.moveToFirst();

    }


    public static boolean deleteCategory(int catId){

        SQLiteDatabase db = instance.getWritableDatabase();
        String sql = "DELETE FROM " + CATEGORY_TABLE + " WHERE " + CATEGORY_ID + " = " + catId;
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.moveToFirst();

    }

    public static boolean updateTransactionWhenCatDeleted(int catId) {
        SQLiteDatabase db = instance.getWritableDatabase();
        String sql = " UPDATE "+ TRANSACTIONS_TABLE + " SET "+ CATEGORY_FK_ID + " = 20 " + " WHERE " + CATEGORY_FK_ID + " = " + catId;
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.moveToFirst();

    }

    public static List<Category> getEveryCategory(){

        List<Category> returnList = new ArrayList<>();

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


    public static List<Transaction> getTransactionsByMonth(String year, String month){


        List<Transaction> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TRANSACTIONS_TABLE + " WHERE "+ TRANSACTION_DATE + " BETWEEN " + "'" + year + "-"  + month + "-" + "01' "
                            + " AND " + "'" + year + "-"  + month + "-"  + "31' ORDER BY " + TRANSACTION_DATE;

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

    public static List<Transaction> getAllTransactions(){


        List<Transaction> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TRANSACTIONS_TABLE + " ORDER BY " + TRANSACTION_DATE;

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


    public static List<Transaction> getTransactionsByMonthAndCat(String year, String month, int categoryId) {
        List<Transaction> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TRANSACTIONS_TABLE + " WHERE "+ TRANSACTION_DATE + " BETWEEN " + "'" + year + "-"  + month + "-" + "01' "
                + " AND " + "'" + year + "-"  + month + "-"  + "31' ORDER BY " + TRANSACTION_DATE + " AND " + CATEGORY_FK_ID + " = " + categoryId;

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

    public static boolean editCategory(int id, String name, String color) {

        SQLiteDatabase db = instance.getWritableDatabase();
        String sql = " UPDATE " + CATEGORY_TABLE + " SET " + CATEGORY_NAME + " = " + name +" WHERE " + CATEGORY_ID + " = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.moveToFirst();

    }

    public static boolean editTransaction(int id, int amount, String description, String date, int catId) {

        SQLiteDatabase db = instance.getWritableDatabase();
        String sql = " UPDATE " + TRANSACTIONS_TABLE + " SET " + TRANSACTION_AMOUNT + " = " + amount + ","
                    + TRANSACTIONS_DESC + " = " + description + " , "
                    + TRANSACTION_DATE + " = " + date + " , "
                    + CATEGORY_FK_ID + " = " + catId
                    +" WHERE " + CATEGORY_ID + " = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.moveToFirst();

    }
}
