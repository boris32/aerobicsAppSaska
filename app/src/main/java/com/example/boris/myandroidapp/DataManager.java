package com.example.boris.myandroidapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.nio.file.Path;

/**
 * Created by Boris on 5/18/2018.
 */

public class DataManager {

    private SQLiteDatabase db;

    //Column names BUSINESS MONTH TABLE:
    static final String TABLE_ROW_ID = "_id";
    static final String TABLE_ROW_MONTH = "month_name";
    static final String TABLE_ROW_YEAR = "year";
    static final String TABLE_ROW_IS_CURRENT = "is_current";
    static final String TABLE_ROW_IS_ENABLED = "is_enabled";

    //Column names CUSTOMER TABLE:
    static final String TABLE_ROW_CUSTOMER_ID = "customer_id";
    static final String TABLE_ROW_FIRST_NAME = "first_name";
    static final String TABLE_ROW_LAST_NAME = "last_name";
    static final String TABLE_ROW_USUAL_FEE= "usual_fee";

    //Column names BUSINES_MONTH_CUSTOMERS:
    //static final String TABLE_ROW_ID = "_id";
    static final String TABLE_ROW_MONTH_ID = "month_id";
    //static final String TABLE_ROW_CUSTOMER_ID = "customer_id";
    static final String TABLE_ROW_AMOUNT_PAID = "amount_paid";



    //DB info:
    static final String DB_NAME = "myapp";
    static int dbVersion = 1;
    static final String BUSINESS_MONTH_TABLE_NAME = "business_month";
    static final String CUSTOMER_TABLE_NAME = "customer";
    static final String BUSINES_MONTH_CUSTOMERS = "business_month_customers";

    //Constructor:
    public DataManager(Context context) {
        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
        db = helper.getWritableDatabase();

        //TEMP: Remove this
        //String s= db.getPath();
        //String dbname = "myapp.db";
        //Path p = context.getDatabasePath(dbname);

        //insertMockDataIntoDb(db);
    }

    //Insert mock data
    /*public void insertMockDataIntoDb (SQLiteDatabase db) {

    }*/

    //Get all records:
    public Cursor getAllBusinessMonthRecords() {
        return db.rawQuery("select " + TABLE_ROW_MONTH + ", " + TABLE_ROW_YEAR + ", " + TABLE_ROW_ID + " from " + BUSINESS_MONTH_TABLE_NAME, null);
    }

    //Get number of months added so far
    public int howManyMonthsHaveBeenConfigured() {
        String query = "select count("
                + TABLE_ROW_ID
                + ") from "
                + BUSINESS_MONTH_TABLE_NAME;
                /*+ " where "
                + TABLE_ROW_IS_ENABLED
                + " = 1";*/

        Cursor c = db.rawQuery(query, null);
        c.moveToNext();

        return c.getInt(0);
    }

    //Get month id by name and year:
    public Cursor getMonthId(String month, String year) {
        return db.rawQuery("select " + TABLE_ROW_ID + " from " + BUSINESS_MONTH_TABLE_NAME + " where " + TABLE_ROW_MONTH + "=" + month + " and " + TABLE_ROW_YEAR + "=" + year, null);
    }

    //Get MAX busyness month id:
    public Cursor getMaxMonthId() {
        return db.rawQuery("select max(" + TABLE_ROW_ID + ") from " + BUSINESS_MONTH_TABLE_NAME, null);
    }

    //TODO Refactor the query
    //Get customers for month:
    public Cursor getCustomersForMonth(int id) {
        return db.rawQuery("select first_name, last_name, amount_paid from customer c join business_month_customers bmc on c.customer_id = bmc.customer_id where bmc.month_id = " + id, null);
        //return db.rawQuery("select first_name, last_name, amount_paid from customer c join business_month_customers bmc on c.customer_id = bmc.customer_id", null);
    }

    //Select all existing customers and return a cursor
    public Cursor getAllExistingCustomersCursor() {
        String query;
        query = "select "
        + TABLE_ROW_LAST_NAME
        + ", "
        + TABLE_ROW_FIRST_NAME
        + ", "
        + TABLE_ROW_CUSTOMER_ID
        + ", "
        + TABLE_ROW_USUAL_FEE
        + " from "
        + CUSTOMER_TABLE_NAME;
        return db.rawQuery(query, null);
    }

    //Add EXISTING customer to busyness month
    public void addExistingCustomerToBusinessMonth(int month_id, int customer_id, int fee) {
        String query = "insert into "
                + BUSINES_MONTH_CUSTOMERS
                + " ("
                + TABLE_ROW_MONTH_ID
                + ", "
                + TABLE_ROW_CUSTOMER_ID
                + ", "
                + TABLE_ROW_AMOUNT_PAID
                + ") values ('"
                + month_id
                + "', '"
                + customer_id
                + "', '"
                + fee
                + "')"
                ;

        db.execSQL(query);
    }

    //Check if month/ year combo already exists in the DB
    public boolean isTheProposedMonthYearComboUnique(String month, String year) {
        boolean result = false;

        String query = "select "
                + TABLE_ROW_ID
                + " from "
                + BUSINESS_MONTH_TABLE_NAME
                + " where "
                + TABLE_ROW_MONTH
                + " = '"
                + month
                + "' AND "
                + TABLE_ROW_YEAR
                + " = '"
                + year
                + "'";

        Cursor c = db.rawQuery(query, null);
        if (c.getCount() <= 0)
            result = true;

        return result;
    }

    //Add new month
    //TODO: create check if such month already exists
    public void addNewMonthEntry(String month, String year) {
        String querry = "insert into "
                + BUSINESS_MONTH_TABLE_NAME
                + " ("
                + TABLE_ROW_MONTH
                + ", "
                + TABLE_ROW_YEAR
                + ", "
                + TABLE_ROW_IS_CURRENT
                + ", "
                + TABLE_ROW_IS_ENABLED
                + ") values ('"
                + month
                + "', '"
                + year
                + "', 'false', 'true')";
        db.execSQL(querry);
    }

    //Add new customer
    public void createNewCustomer(String fname, String lname, String fee) {
        String querry = "insert into "
                + CUSTOMER_TABLE_NAME
                + " ("
                + TABLE_ROW_FIRST_NAME
                + ", "
                + TABLE_ROW_LAST_NAME
                + ", "
                + TABLE_ROW_USUAL_FEE
                + ") values ('"
                + fname
                + "', '"
                + lname
                + "', '"
                + fee
                + "')";
        db.execSQL(querry);
    }

    public String getLatestCreatedCustomer() {
        String querry = "select MAX("
                + TABLE_ROW_CUSTOMER_ID
                + ") from "
                + CUSTOMER_TABLE_NAME;

        String result = "";
        Cursor c =db.rawQuery(querry, null);
        while (c.moveToNext())
            result = c.getString(0);

        return result;
    }


    class CustomSQLiteOpenHelper extends SQLiteOpenHelper {

        public CustomSQLiteOpenHelper(Context context) {
            super(context, DB_NAME, null, dbVersion);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "create table "
                    + BUSINESS_MONTH_TABLE_NAME
                    + " ("
                    + TABLE_ROW_ID
                    + " integer primary key autoincrement not null"
                    + ", "
                    + TABLE_ROW_MONTH
                    + " text not null"
                    + ", "
                    + TABLE_ROW_YEAR
                    + " int not null"
                    + ", "
                    + TABLE_ROW_IS_CURRENT
                    + " boolean default true"
                    + ", "
                    + TABLE_ROW_IS_ENABLED
                    + " boolean default true)";
            db.execSQL(query);

            query = "create table "
                    + CUSTOMER_TABLE_NAME
                    + " ("
                    + TABLE_ROW_CUSTOMER_ID
                    + " integer primary key autoincrement not null"
                    + ", "
                    + TABLE_ROW_FIRST_NAME
                    + " text not null"
                    + ", "
                    + TABLE_ROW_LAST_NAME
                    + " text not null"
                    + ", "
                    + TABLE_ROW_USUAL_FEE
                    + " int default 1000)";
            db.execSQL(query);

            query = "create table "
                    + BUSINES_MONTH_CUSTOMERS
                    + " ("
                    + TABLE_ROW_ID
                    + " integer primary key autoincrement not null"
                    + ", "
                    + TABLE_ROW_MONTH_ID
                    + " integer not null"
                    + ", "
                    + TABLE_ROW_CUSTOMER_ID
                    + " integer not null"
                    + ", "
                    + TABLE_ROW_AMOUNT_PAID
                    + " int default 0,"
                    /*+ " constraint pk_bmc primary key ("
                    + TABLE_ROW_MONTH_ID
                    + ", "
                    + TABLE_ROW_CUSTOMER_ID
                    + ") "*/
                    + " foreign key ("//" constraint fk_mid foreign key ("
                    + TABLE_ROW_MONTH_ID
                    + ") "
                    + "references "
                    + BUSINESS_MONTH_TABLE_NAME
                    + " ("
                    + TABLE_ROW_MONTH_ID
                    + ")"
                    + ", foreign key (" //", constraint fk_cid foreign key ("
                    + TABLE_ROW_CUSTOMER_ID
                    + ")"
                    + " references "
                    + CUSTOMER_TABLE_NAME
                    + " ("
                    + TABLE_ROW_CUSTOMER_ID
                    + "))";
            db.execSQL(query);


        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            //TODO upgradeDbMethod
        }
    }
}
