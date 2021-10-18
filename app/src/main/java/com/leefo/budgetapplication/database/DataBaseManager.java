package com.leefo.budgetapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.leefo.budgetapplication.model.Category;
import com.leefo.budgetapplication.model.FinancialTransaction;
import com.leefo.budgetapplication.model.IDatabase;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Creates the database needed for saving the information of the transactions and categories
 * and handling inserting, deleting and requesting any information that is in the database
 *
 * @author Omar Sulaiman
 */
public class DataBaseManager extends SQLiteOpenHelper implements IDatabase {

    /**
     * The name of the category table used to simplify the referencing of the table
     */
    private static final String CATEGORY_TABLE = "CATEGORY_TABLE";

    /**
     * The name of the first column in the category table used to store category names
     */
    private static final String CATEGORY_NAME = "CATEGORY_NAME";

    /**
     * The name of the second column in the category table used to store colors
     */
    private static final String CATEGORY_COLOR = "CATEGORY_COLOR";

    /**
     * The name of the third column in the category table used to store a value which indicates if the category has income transactions or expenses
     */
    private static final String CATEGORY_IS_INCOME = "CATEGORY_IS_INCOME";

    /**
     * The name of the fourth column in the category table used to store a value for the category budget
     */
    private static final String CATEGORY_BUDGET = "CATEGORY_BUDGET";



    /**
     * The name of the transaction's table used to simplify the referencing of the table
     */
    private static final String TRANSACTIONS_TABLE = "TRANSACTIONS_TABLE";

    /**
     * The name of the first column in the transaction table used for storing a brief description of the transaction
     */
    private static final String TRANSACTIONS_DESC = "TRANSACTIONS_DESCRIPTION";

    /**
     * The name of the second column in the transaction table used for storing the date the transaction was made
     */
    private static final String TRANSACTION_DATE = "TRANSACTION_DATE";

    /**
     * The name of the third column in the transaction table used for storing the amount of money spent on a single transaction
     */
    private static final String TRANSACTION_AMOUNT = "TRANSACTION_AMOUNT";

    /**
     * The name of the fourth column in the transaction table used for storing the name of the category a transaction is related to
     */
    private static final String CATEGORY_FK_NAME = "TRANS_FOREIGN_ID";

    /**
     * The name of the fifth column in the transaction table used for storing the id of every transaction
     * which helps in separating the transaction and safely deleting a specific transaction without worrying about deleting multiple similar transactions
     */
    private static final String TRANSACTION_ID = "TRANSACTION_ID";




    /**
     * Creates a database in which the tables created are stored in
     * @param context The main activity of the program
     */
    public DataBaseManager(@Nullable Context context) {
        super(context, "category_transaction_new4", null, 1);
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

        createCategoryTable(sqLiteDatabase);
        createTransactionTable(sqLiteDatabase);

    }


    private void createCategoryTable(SQLiteDatabase sqLiteDatabase) {
        String createTableCategory = " CREATE TABLE " + CATEGORY_TABLE + " ( " + CATEGORY_NAME + " TEXT PRIMARY KEY , "
                                    + CATEGORY_COLOR + " TEXT, "
                                    + CATEGORY_IS_INCOME + " INTEGER, "
                                    + CATEGORY_BUDGET + " INTEGER " + " )";


        sqLiteDatabase.execSQL(createTableCategory);
    }




    private void createTransactionTable(SQLiteDatabase sqLiteDatabase) {
        String createTableTransactions = " CREATE TABLE " + TRANSACTIONS_TABLE + " ( "  + TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TRANSACTION_AMOUNT + " REAL, "
                + TRANSACTIONS_DESC + " TEXT, "
                + TRANSACTION_DATE + " TEXT, "
                + CATEGORY_FK_NAME + " TEXT)";

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
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    /**
     * Saves a financial transactions information in the database
     * @param transaction Transaction to be saved.
     */
    public void saveData(FinancialTransaction transaction)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = getContentValues(transaction);

        db.insert(TRANSACTIONS_TABLE, null, cv);
        db.close();

    }


    /**
     * Saves the categories information in the database
     * @param category Category to be saved.
     */
    public void saveData(Category category)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = getContentValues(category);
        db.insert(CATEGORY_TABLE, null, cv);
        db.close();
    }

    @NonNull
    private ContentValues getContentValues(FinancialTransaction transaction) {
        ContentValues cv = new ContentValues();
        cv.put(TRANSACTIONS_DESC, transaction.getDescription());
        cv.put(TRANSACTION_AMOUNT, transaction.getAmount());
        cv.put(TRANSACTION_DATE, transaction.getDate().toString());
        cv.put(CATEGORY_FK_NAME, transaction.getCategory().getName());
        return cv;
    }


    @NonNull
    private ContentValues getContentValues(Category category) {
        int i = category.isIncome() ? 1 : 0;
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_NAME, category.getName());
        cv.put(CATEGORY_COLOR, category.getColor());
        cv.put(CATEGORY_IS_INCOME, i);
        cv.put(CATEGORY_BUDGET, category.getBudgetGoal());
        return cv;
    }

    /**
     * Deletes only one transaction from the database that matches the data of the transaction given
     * @param transaction Transaction to be removed.
     */
    public void removeData(FinancialTransaction transaction)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        int deletedTransaction = getDeletedTransaction(transaction, db);

        //Deletes a random transaction from identical transactions and a specific transaction if there is one that satisfies the requirements
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
     * @return Returns a list of all categories information in the database as category objects
     */
    public ArrayList<Category> getCategories(){

        ArrayList<Category> returnList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = getCategoryTable(db);

        cursorDataToObjects(returnList, cursor);

        cursor.close();
        db.close();
        return returnList;
    }

    private void cursorDataToObjects(ArrayList<Category> returnList, Cursor cursor) {
        if (cursor.moveToFirst()){
            do {
                String categoryName = cursor.getString(0);
                String categoryColor = cursor.getString(1);
                int categoryIsIncome = cursor.getInt(2);
                boolean b = categoryIsIncome == 1;
                int categoryBudgetGoal = cursor.getInt(3);
                Category newCategory = new Category(categoryName, categoryColor, b, categoryBudgetGoal);
                returnList.add(newCategory);

            }while (cursor.moveToNext());

        }
    }

    private Cursor getCategoryTable(SQLiteDatabase db) {

        String queryString = "SELECT * FROM " + CATEGORY_TABLE;

        return db.rawQuery(queryString, null);
    }


    /**
     * Creates transaction objects of all the transactions registered in the database
     * @return Returns a list of all transactions in he database
     */
    public ArrayList<FinancialTransaction> getFinancialTransactions(){
        ArrayList<Category> categories = getCategories();

        ArrayList<FinancialTransaction> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = getTransactionTable(db);


        cursorDataToObjects(categories, returnList, cursor);

        cursor.close();
        db.close();
        return returnList;

    }

    private void cursorDataToObjects(ArrayList<Category> categories, ArrayList<FinancialTransaction> returnList, Cursor cursor) {
        // Loops through every row in the cursor and gets the columns
        if (cursor.moveToFirst()){
            do {
                float transactionAmount = cursor.getFloat(1);
                String transactionDesc = cursor.getString(2);
                String transactionDate = cursor.getString(3);
                String categoryFKName = cursor.getString(4);

                Category category = getCategory(categories, categoryFKName);

                LocalDate date = getLocalDate(transactionDate);

                FinancialTransaction newTransaction = new FinancialTransaction(transactionAmount ,transactionDesc, date, category);
                returnList.add(newTransaction);

            }while (cursor.moveToNext());

        }
    }

    private Cursor getTransactionTable(SQLiteDatabase db) {
        String queryString = "SELECT * FROM " + TRANSACTIONS_TABLE + " ORDER BY " + TRANSACTION_DATE + " DESC ";
        return db.rawQuery(queryString, null);
    }

    @NonNull
    private Category getCategory(ArrayList<Category> categories, String categoryFKName) {
        Category category = new Category("", "", true, 0);
        for (Category c : categories){
            if (categoryFKName.equals(c.getName())){
                category = c;
            }
        }
        return category;
    }

    private LocalDate getLocalDate(String transactionDate) {
        String year, month, day;
        year = transactionDate.substring(0,4);
        month = transactionDate.substring(5,7);
        day = transactionDate.substring(8,10);

        return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
    }


}
