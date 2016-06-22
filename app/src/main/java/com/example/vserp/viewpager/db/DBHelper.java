package com.example.vserp.viewpager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vserp.viewpager.utils.Prefs;

/**
 * Created by vserp on 6/17/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    /*
    Table expenses:
    - _id
    - id_passive (id from table passive)
    - volume (volume of money)
    - shortDate (shortDate when expenses made)
    */
    private static final String CREATE_TABLE_EXPENSES = String.format(
            "create table " +
                    "%s  ( " +
                    "%s integer primary key autoincrement, " +
                    "%s integer, " +
                    "%s double, " +
                    "%s text);",
            Prefs.TABLE_NAME_EXPENSES,
            Prefs.FIELD_ID,
            Prefs.EXPENSES_FIELD_ID_PASSIVE,
            Prefs.EXPENSES_FIELD_VOLUME,
            Prefs.EXPENSES_FIELD_DATE);
    /*
    Table incomes:
    - _id
    - id_income_name (id from table income_name)
    - volume (volume of money)
    - shortDate (shortDate when income made)
    */
    private static final String CREATE_TABLE_INCOMES = String.format(
            "create table " +
                    "%s ( " +
                    "%s integer primary key autoincrement, " +
                    "%s integer, " +
                    "%s double, " +
                    "%s text);",
            Prefs.TABLE_NAME_INCOMES,
            Prefs.FIELD_ID,
            Prefs.INCOMES_FIELD_ID_PASSIVE,
            Prefs.INCOMES_FIELD_VOLUME,
            Prefs.INCOMES_FIELD_DATE);
    /*
    Table income_names:
    - _id
    - name (name of expense)
    - constant (monthly standard income)
    */
    private static final String CREATE_TABLE_INCOME_NAMES = String.format(
            "create table " +
                    "%s ( " +
                    "%s integer primary key autoincrement, " +
                    "%s text, " +
                    "%s integer);",
            Prefs.TABLE_NAME_INCOME_NAMES,
            Prefs.FIELD_ID,
            Prefs.INCOME_NAMES_FIELD_NAME,
            Prefs.INCOME_NAMES_FIELD_CONSTANT_PAYMENT);
    /*
    Table expense_name:
    - _id
    - name (name of expense)
    - critical (monthly constant payment)
    */
    private static final String CREATE_TABLE_EXPENSE_NAMES = String.format(
            "create table " +
                    "%s ( " +
                    "%s integer primary key autoincrement, " +
                    "%s text, " +
                    "%s integer);",
            Prefs.TABLE_NAME_EXPENSE_NAMES,
            Prefs.FIELD_ID,
            Prefs.EXPENSE_NAMES_FIELD_NAME,
            Prefs.EXPENSE_NAMES_FIELD_CRITICAL);

    public DBHelper(Context context, int version) {
        super(context, Prefs.DB_NAME, null, Prefs.DB_CURRENT_VERSION);
    }
    /*
    Table Cash Flow Monthly:
    - current month
    - current year
    - overall current incomes
    - overall current expenses
    - current cash flow
    - current balance
    - plan of incomes
    - plan of expenses
     */
    private static final String CREATE_TABLE_CASH_FLOW_MONTHLY = String.format(
            "create table " +
                    "%s ( " +
                    "%s integer primary key autoincrement, " +
                    "%s integer, " +
                    "%s integer, " +
                    "%s integer, " +
                    "%s integer, " +
                    "%s integer, " +
                    "%s integer, " +
                    "%s integer, " +
                    "%s integer);",
            Prefs.TABLE_CASH_FLOW_MONTHLY_NAME,
            Prefs.FIELD_ID,
            Prefs.CASH_FLOW_MONTHLY_FIELD_MONTH,
            Prefs.CASH_FLOW_MONTHLY_FIELD_YEAR,
            Prefs.CASH_FLOW_MONTHLY_FIELD_INCOMES,
            Prefs.CASH_FLOW_MONTHLY_FIELD_EXPENSE,
            Prefs.CASH_FLOW_MONTHLY_FIELD_CASH_FLOW,
            Prefs.CASH_FLOW_MONTHLY_FIELD_BALANCE,
            Prefs.CASH_FLOW_MONTHLY_FIELD_INCOME_PLAN,
            Prefs.CASH_FLOW_MONTHLY_FIELD_EXPENSE_PLAN);

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EXPENSES);
        db.execSQL(CREATE_TABLE_EXPENSE_NAMES);
        db.execSQL(CREATE_TABLE_INCOMES);
        db.execSQL(CREATE_TABLE_INCOME_NAMES);
        db.execSQL(CREATE_TABLE_CASH_FLOW_MONTHLY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
    }
}
