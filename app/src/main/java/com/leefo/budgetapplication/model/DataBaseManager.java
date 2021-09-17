package com.leefo.budgetapplication.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseManager extends SQLiteOpenHelper {


    public static final String CATEGORY_ID = "CATEGORY_ID";
    public static final String CATEGORY_NAME = "CATEGORY_NAME";
    public static final String CATEGORY_COLOR = "CATEGORY_COLOR";
    public static final String TRANSACTIONS_ID = "TRANSACTIONS_ID";
    public static final String TRANSACTIONS_NAME = "TRANSACTIONS_NAME";
    public static final String TRANSACTION_DATE = "TRANSACTION_DATE";
    public static final String CATEGORY_TABLE = "CATEGORY_TABLE";
    public static final String TRANSACTIONS_TABLE = "TRANSACTIONS_TABLE";

    public DataBaseManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createTableCategory = " CREATE TABLE " + CATEGORY_TABLE + " ( " + CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                    + CATEGORY_NAME + " TEXT, " + CATEGORY_COLOR + " TEXT)";

        String createTableTransactions = " CREATE TABLE " + TRANSACTIONS_TABLE + " ( " + TRANSACTIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                        + TRANSACTIONS_NAME + " TEXT, "
                                        + TRANSACTION_DATE + " TEXT, " + CATEGORY_ID
                                        + " INTEGER, FOREIGN KEY(CATEGORY_ID) REFERENCES " + CATEGORY_TABLE + "(CATEGORY_ID) ON DELETE SET NULL)";

        sqLiteDatabase.execSQL(createTableCategory);
        sqLiteDatabase.execSQL(createTableTransactions);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addTransaction(Transactions transaction){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TRANSACTIONS_NAME, transaction.getName());
        cv.put(TRANSACTION_DATE, transaction.getAge());
        long insert = db.insert(TRANSACTIONS_TABLE, null, cv);

        return insert != -1;

    }

    public boolean addCategory(Category category){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_NAME, category.getName());
        cv.put(CATEGORY_COLOR, category.getColor());
        long insert = db.insert(CATEGORY_TABLE, null, cv);

        return insert != -1;

    }

    public boolean deleteTransaction(Transactions transaction){

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + TRANSACTIONS_TABLE + " WHERE " + TRANSACTIONS_ID + " = " + transaction.getId();
        Cursor cursor = db.rawQuery(sql, null);
        return cursor.moveToFirst();

    }


    public boolean deleteCategory(Category category){

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + CATEGORY_TABLE + " WHERE " + CATEGORY_ID + " = " + category.getId();
        Cursor cursor = db.rawQuery(sql, null);
        updateTransactionTable(db);
        return cursor.moveToFirst();

    }

    private void updateTransactionTable(SQLiteDatabase db) {
        String sql = " UPDATE "+ TRANSACTIONS_TABLE + " SET "+ CATEGORY_ID + " = 7 WHERE " + CATEGORY_ID + " IS NULL ";
        db.rawQuery(sql, null);

    }


}
