package com.example.vserp.viewpager.utils;

import android.net.Uri;

/**
 * Created by vserp on 6/17/2016.
 */

public class Prefs {

    public static final String LOG_TAG = "MyLogs";

    //public static final String RAW_QUERY_ALL_EXPENSES = "SELECT expense_names.name, expenses.volume, expense_names.critical, expenses.shortDate FROM expenses INNER JOIN expense_names ON expense_names._id = expenses.id_passive;";
    public static final String RAW_QUERY_ALL_EXPENSES =
            Prefs.TABLE_NAME_EXPENSES + " INNER JOIN " +
            Prefs.TABLE_NAME_EXPENSE_NAMES + " ON (" +
            Prefs.TABLE_NAME_EXPENSE_NAMES + "." +
            Prefs.FIELD_ID + " = " +
            Prefs.TABLE_NAME_EXPENSES + "." +
            Prefs.EXPENSES_FIELD_ID_PASSIVE + ");";

    public static final String RAW_QUERY_ALL_INCOMES =
            Prefs.TABLE_NAME_INCOMES + " INNER JOIN " +
            Prefs.TABLE_NAME_INCOME_NAMES + " ON (" +
            Prefs.TABLE_NAME_INCOME_NAMES + "." +
            Prefs.FIELD_ID + " = " +
            Prefs.TABLE_NAME_INCOMES + "." +
            Prefs.INCOMES_FIELD_ID_PASSIVE + ");";

    //The Database constants:
    public static final String DB_NAME = "Money_Flow_DB";
    public static final int DB_CURRENT_VERSION = 1;
    public static final String FIELD_ID = "_id";

    //The Table Expenses constants:
    public static final String TABLE_NAME_EXPENSES = "expenses";
    public static final String EXPENSES_FIELD_ID_PASSIVE = "id_passive";
    public static final String EXPENSES_FIELD_VOLUME = "expense";
    public static final String EXPENSES_FIELD_DATE = "shortDate";

    //The Table Expense Names constants:
    public static final String TABLE_NAME_EXPENSE_NAMES = "expense_names";
    public static final String EXPENSE_NAMES_FIELD_NAME = "name";
    public static final String EXPENSE_NAMES_FIELD_CRITICAL = "critical";

    //The Table Incomes constants:
    public static final String TABLE_NAME_INCOMES = "incomes";
    public static final String INCOMES_FIELD_ID_PASSIVE = "id_passive";
    public static final String INCOMES_FIELD_VOLUME = "income";
    public static final String INCOMES_FIELD_DATE = "shortDate";

    //The Table Income_names constants:
    public static final String TABLE_NAME_INCOME_NAMES = "income_names";
    public static final String INCOME_NAMES_FIELD_NAME = "name";
    public static final String INCOME_NAMES_FIELD_CONSTANT_PAYMENT = "constant_payment";

    //The table Cash Flow Monthly constants:
    public static final String TABLE_CASH_FLOW_MONTHLY_NAME = "cash_flow_monthly";
    public static final String CASH_FLOW_MONTHLY_FIELD_MONTH = "month";
    public static final String CASH_FLOW_MONTHLY_FIELD_YEAR = "year";
    public static final String CASH_FLOW_MONTHLY_FIELD_INCOME = "income";
    public static final String CASH_FLOW_MONTHLY_FIELD_EXPENSE = "expense";
    public static final String CASH_FLOW_MONTHLY_FIELD_CASH_FLOW = "month_cash_flow";
    public static final String CASH_FLOW_MONTHLY_FIELD_BALANCE = "balance";
    public static final String CASH_FLOW_MONTHLY_FIELD_INCOME_PLAN = "next_month_income_plan";
    public static final String CASH_FLOW_MONTHLY_FIELD_EXPENSE_PLAN = "next_month_expense_plan";

    //Last month positions:

    public static final String LAST_MONTH_EXPENSE = "last_month_expense";
    public static final String LAST_MONTH_INCOME = "last_month_income";
    //The provider constants:
    private static final String URI_SCHEMA = "content://";
    public static final String URI_AUTHORITIES = "com.example.vserp.viewpager.provider";

    //Expenses URI Constants:
    public static final String URI_EXPENSES_PATH = "expenses";
    public static final String URI_INCOMES_PATH = "incomes";
    public static final Uri URI_EXPENSES = Uri.parse(URI_SCHEMA + URI_AUTHORITIES + "/" + URI_EXPENSES_PATH);
    public static final Uri URI_INCOMES = Uri.parse(URI_SCHEMA + URI_AUTHORITIES + "/" + URI_INCOMES_PATH);

    //Expense Names URI Constants:
    public static final String URI_EXPENSE_NAMES_PATH = "expense_names";
    public static final String URI_INCOME_NAMES_PATH = "income_names";
    public static final Uri URI_EXPENSE_NAMES = Uri.parse(URI_SCHEMA + URI_AUTHORITIES + "/" + URI_EXPENSE_NAMES_PATH);
    public static final Uri URI_INCOME_NAMES = Uri.parse(URI_SCHEMA + URI_AUTHORITIES + "/" + URI_INCOME_NAMES_PATH);

    //Cash Flow Monthly:
    public static final String URI_CASH_FLOW_MONTHLY_PATH = "monthly_cash";
    public static final Uri URI_CASH_FLOW_MONTHLY = Uri.parse(URI_SCHEMA + URI_AUTHORITIES + "/" + URI_CASH_FLOW_MONTHLY_PATH);

    //Expenses Plan:
    public static final String URI_MONTHLY_EXPENSES_PLAN_PATH = "expenses_plan";
    public static final Uri URI_MONTHLY_EXPENSES_PLAN = Uri.parse(URI_SCHEMA + URI_AUTHORITIES + "/" + URI_MONTHLY_EXPENSES_PLAN_PATH);

    //Incomes Plan:
    public static final String URI_MONTHLY_INCOMES_PLAN_PATH = "incomes_plan";
    public static final Uri URI_MONTHLY_INCOMES_PLAN = Uri.parse(URI_SCHEMA + URI_AUTHORITIES + "/" + URI_MONTHLY_INCOMES_PLAN_PATH);

    //Total cash flow URI constants:
    public static final String URI_ALL_EXPENSES_PATH = "all_expenses";
    public static final String URI_ALL_INCOMES_PATH = "all_incomes";
    public static final Uri URI_ALL_EXPENSES = Uri.parse(URI_SCHEMA + URI_AUTHORITIES + "/" + URI_ALL_EXPENSES_PATH);
    public static final Uri URI_ALL_INCOMES = Uri.parse(URI_SCHEMA + URI_AUTHORITIES + "/" + URI_ALL_INCOMES_PATH);

    //Loader Constants
    public static final int ID_LOADER_EXPENSE_NAMES = 1;
    public static final int ID_LOADER_INCOME_NAMES = 2;
}
