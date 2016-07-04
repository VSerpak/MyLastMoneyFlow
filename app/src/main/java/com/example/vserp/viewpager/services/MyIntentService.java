package com.example.vserp.viewpager.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;

import com.example.vserp.viewpager.activities.MainActivity;
import com.example.vserp.viewpager.fragments.IncomesFragment;
import com.example.vserp.viewpager.fragments.ExpensesFragment;
import com.example.vserp.viewpager.utils.Prefs;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyIntentService extends IntentService {

    private static final String ACTION_ADD_NEW_EXPENSE = "com.example.vserp.viewpager.Services.action.ADD_NEW_EXPENSE";
    private static final String ACTION_ADD_NEW_INCOME = "com.example.vserp.viewpager.Services.action.ADD_NEW_INCOME";

    public static final String ACTION_ADD_EXPENSE_PLAN = "com.example.vserp.viewpager.Services.action.ADD_EXPENSE_PLAN";
    public static final String ACTION_ADD_INCOME_PLAN = "com.example.vserp.viewpager.Services.action.ADD_INCOME_PLAN";

    private static final String EXTRA_TITLE = "com.example.vserp.viewpager.Services.extra.TITLE";
    private static final String EXTRA_VOLUME = "com.example.vserp.viewpager.Services.extra.VOLUME";
    private static final String EXTRA_CONSTANT = "com.example.vserp.viewpager.Services.extra.CONSTANT";

    public static final String EXTRA_EXPENSE_PLAN = "com.example.vserp.viewpager.Services.extra.EXPENSE_PLAN";
    public static final String EXTRA_INCOME_PLAN = "com.example.vserp.viewpager.Services.extra.INCOME_PLAN";

    private boolean needToAddNewExpenseName = true;
    private int position = 0;//cursor position
    public static int mCurrentItemValue;

    public MyIntentService() {
        super("MyIntentService");
    }

    public static void startActionAddNewExpense(Context context, String title, double volume, int constant) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_ADD_NEW_EXPENSE);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_VOLUME, volume);
        intent.putExtra(EXTRA_CONSTANT, constant);
        context.startService(intent);
    }

    public static void startActionAddNewIncome(Context context, String title, double volume, int constant) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_ADD_NEW_INCOME);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_VOLUME, volume);
        intent.putExtra(EXTRA_CONSTANT, constant);
        context.startService(intent);
    }

    public static void startActionAddExpensePlan(Context context, String plan) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_ADD_EXPENSE_PLAN);
        intent.putExtra(EXTRA_EXPENSE_PLAN, plan);
        context.startService(intent);
    }

    public static void startActionAddIncomePlan(Context context, String plan) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_ADD_INCOME_PLAN);
        intent.putExtra(EXTRA_INCOME_PLAN, plan);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action) {
                case ACTION_ADD_NEW_EXPENSE:
                    final String expenseTitle = intent.getStringExtra(EXTRA_TITLE);
                    final double expenseVolume = intent.getDoubleExtra(EXTRA_VOLUME, 0);
                    final int expenseConstant = intent.getIntExtra(EXTRA_CONSTANT, 0);
                    handleActionAddExpense(expenseTitle, expenseVolume, expenseConstant);
                    break;
                case ACTION_ADD_NEW_INCOME:
                    final String incomeTitle = intent.getStringExtra(EXTRA_TITLE);
                    final double incomeVolume = intent.getDoubleExtra(EXTRA_VOLUME, 0);
                    final int incomeConstant = intent.getIntExtra(EXTRA_CONSTANT, 0);
                    handleActionAddIncome(incomeTitle, incomeVolume, incomeConstant);
                    break;
                case ACTION_ADD_EXPENSE_PLAN:
                    final String expensePlan = intent.getStringExtra(EXTRA_EXPENSE_PLAN);
                    handleActionAddExpensePlan(expensePlan);
                    break;
                case ACTION_ADD_INCOME_PLAN:
                    final String incomePlan = intent.getStringExtra(EXTRA_INCOME_PLAN);
                    handleActionAddIncomePlan(incomePlan);
                    break;
            }
        }
    }

    private void handleActionAddExpensePlan(String itemPlan) {

        int mCurrentExpensePlan;
        int mNewExpensePlanValue = Integer.parseInt(ExpensesFragment.sPlan);

        Calendar mCalendar = Calendar.getInstance();

        Date mDate = new Date(mCalendar.getTimeInMillis());

        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

        ContentValues cv = new ContentValues();

        Cursor mCursorExpensePlan = getContentResolver().query(Prefs.URI_CASH_FLOW_MONTHLY
                , null, Prefs.CASH_FLOW_MONTHLY_FIELD_MONTH + " = " + monthFormat.format(mDate), null, null);

        if (mCursorExpensePlan != null) {
            if (mCursorExpensePlan.getCount() == 0) {

                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_MONTH, monthFormat.format(mDate));
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_YEAR, yearFormat.format(mDate));
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_INCOME, 0);
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_INCOME_PLAN, 0);
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_EXPENSE,0);
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_EXPENSE_PLAN, mNewExpensePlanValue);
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_BALANCE, 0);
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_CASH_FLOW, 0);

                getContentResolver().insert(Prefs.URI_CASH_FLOW_MONTHLY, cv);
            } else {

                mCursorExpensePlan.moveToFirst();

                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_EXPENSE_PLAN, mNewExpensePlanValue);

                getContentResolver().update(Prefs.URI_CASH_FLOW_MONTHLY, cv,
                        Prefs.CASH_FLOW_MONTHLY_FIELD_MONTH + " = " + monthFormat.format(mDate), null);
            }
        }
        if (mCursorExpensePlan != null) {
            mCursorExpensePlan.close();
        }
    }

    private void handleActionAddIncomePlan(String itemPlan) {

        int mCurrentIncomePlan;
        int mNewIncomePlanValue = Integer.parseInt(IncomesFragment.sPlan);

        Calendar mCalendar = Calendar.getInstance();

        Date mDate = new Date(mCalendar.getTimeInMillis());

        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

        ContentValues cv = new ContentValues();


        Cursor mCursorIncomePlan = getContentResolver().query(Prefs.URI_CASH_FLOW_MONTHLY
                , null, Prefs.CASH_FLOW_MONTHLY_FIELD_MONTH + " = " + monthFormat.format(mDate), null, null);

        if (mCursorIncomePlan != null) {
            if (mCursorIncomePlan.getCount() == 0) {

                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_MONTH, monthFormat.format(mDate));
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_YEAR, yearFormat.format(mDate));
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_INCOME, mNewIncomePlanValue);
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_INCOME_PLAN, 0);
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_EXPENSE,0);
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_EXPENSE_PLAN,0);
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_BALANCE, 0);
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_CASH_FLOW, 0);

                getContentResolver().insert(Prefs.URI_CASH_FLOW_MONTHLY, cv);
            } else {

                mCursorIncomePlan.moveToFirst();

                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_INCOME_PLAN, mNewIncomePlanValue);

                getContentResolver().update(Prefs.URI_CASH_FLOW_MONTHLY, cv,
                        Prefs.CASH_FLOW_MONTHLY_FIELD_MONTH + " = " + monthFormat.format(mDate), null);
            }
        }
        if (mCursorIncomePlan != null) {
            mCursorIncomePlan.close();
        }
    }

    private void handleActionAddExpense(String title, double volume, int constant) {

        ContentValues cv = new ContentValues();

        cv.put(Prefs.EXPENSE_NAMES_FIELD_NAME, title);
        cv.put(Prefs.EXPENSE_NAMES_FIELD_CRITICAL, constant);

//Input new values of expense/income names into the corresponding tables
        getContentResolver().insert(Prefs.URI_EXPENSE_NAMES, cv);

//Manage duplicates
        Cursor mCursorItem = getContentResolver().query(Prefs.URI_EXPENSE_NAMES, null, null, null, null);

        if (mCursorItem != null) {
            checkExpenseNameAvailability(mCursorItem, cv, title);

            passiveExpenseIdApplication(mCursorItem, cv);
        }
//Manage corresponding passive_Id and input new values into the corresponding tables
        Calendar mCalendar = Calendar.getInstance();

        Date mDate = new Date(mCalendar.getTimeInMillis());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

        cv.put(Prefs.EXPENSES_FIELD_VOLUME, volume);
        cv.put(Prefs.EXPENSES_FIELD_DATE, dateFormat.format(mDate));

        getContentResolver().insert(Prefs.URI_EXPENSES, cv);

//Clear ContentValues except ITEM_FIELD_VOLUME for the further usage
// with new cursor of Cash Flow Monthly table
        cv.remove(Prefs.EXPENSE_NAMES_FIELD_NAME);
        cv.remove(Prefs.EXPENSE_NAMES_FIELD_CRITICAL);
        cv.remove(Prefs.EXPENSES_FIELD_DATE);
        cv.remove(Prefs.EXPENSES_FIELD_ID_PASSIVE);

        Cursor mCursorCashFlowMonthly = getContentResolver().query(Prefs.URI_CASH_FLOW_MONTHLY
                , null, Prefs.CASH_FLOW_MONTHLY_FIELD_MONTH + " = " + monthFormat.format(mDate), null, null);

        int mCashFlowMonthly;
        int tabPosition = MainActivity.sTabPosition;
        int mNewItemValue = cv.getAsInteger(Prefs.EXPENSES_FIELD_VOLUME);

        if (mCursorCashFlowMonthly != null) {
            if (mCursorCashFlowMonthly.getCount() == 0) {

                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_MONTH, monthFormat.format(mDate));
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_YEAR, yearFormat.format(mDate));
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_EXPENSE, mNewItemValue);
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_EXPENSE_PLAN, 0);
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_BALANCE, 0);
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_CASH_FLOW, -mNewItemValue);

                getContentResolver().insert(Prefs.URI_CASH_FLOW_MONTHLY, cv);
            } else {

                mCursorCashFlowMonthly.moveToFirst();

                mCashFlowMonthly = mCursorCashFlowMonthly
                        .getInt(mCursorCashFlowMonthly
                                .getColumnIndex(Prefs.CASH_FLOW_MONTHLY_FIELD_CASH_FLOW));

                mCurrentItemValue = mCursorCashFlowMonthly
                        .getInt(mCursorCashFlowMonthly
                                .getColumnIndex(Prefs.CASH_FLOW_MONTHLY_FIELD_EXPENSE));

                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_EXPENSE, mCurrentItemValue + mNewItemValue);
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_CASH_FLOW, mCashFlowMonthly - mNewItemValue);

                getContentResolver().update(Prefs.URI_CASH_FLOW_MONTHLY, cv,
                        Prefs.CASH_FLOW_MONTHLY_FIELD_MONTH + " = " + monthFormat.format(mDate), null);
            }
        }
        if (mCursorItem != null) {
            mCursorItem.close();
        }
        if (mCursorCashFlowMonthly != null) {
            mCursorCashFlowMonthly.close();
        }
    }

    private void handleActionAddIncome(String title, double volume, int constant) {

        ContentValues cv = new ContentValues();

        cv.put(Prefs.INCOME_NAMES_FIELD_NAME, title);
        cv.put(Prefs.INCOME_NAMES_FIELD_CONSTANT_PAYMENT, constant);

//Input new values of expense/income names into the corresponding tables
        getContentResolver().insert(Prefs.URI_INCOME_NAMES, cv);

//Manage duplicates
        Cursor mCursorItem = getContentResolver().query(Prefs.URI_INCOME_NAMES, null, null, null, null);

        if (mCursorItem != null) {
            checkIncomeNameAvailability(mCursorItem, cv, title);

            passiveIncomeIdApplication(mCursorItem, cv);
        }
//Manage corresponding passive_Id and input new values into the corresponding tables
        Calendar mCalendar = Calendar.getInstance();

        Date mDate = new Date(mCalendar.getTimeInMillis());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

        cv.put(Prefs.INCOMES_FIELD_VOLUME, volume);
        cv.put(Prefs.INCOMES_FIELD_DATE, dateFormat.format(mDate));

        getContentResolver().insert(Prefs.URI_INCOMES, cv);

//Clear ContentValues except ITEM_FIELD_VOLUME for the further usage
// with new cursor of Cash Flow Monthly table
        cv.remove(Prefs.INCOME_NAMES_FIELD_NAME);
        cv.remove(Prefs.INCOME_NAMES_FIELD_CONSTANT_PAYMENT);
        cv.remove(Prefs.INCOMES_FIELD_DATE);
        cv.remove(Prefs.INCOMES_FIELD_ID_PASSIVE);

        Cursor mCursorCashFlowMonthly = getContentResolver().query(Prefs.URI_CASH_FLOW_MONTHLY
                , null, Prefs.CASH_FLOW_MONTHLY_FIELD_MONTH + " = " + monthFormat.format(mDate), null, null);

        int mCashFlowMonthly;
        int tabPosition = MainActivity.sTabPosition;
        int mNewItemValue = cv.getAsInteger(Prefs.INCOMES_FIELD_VOLUME);

        if (mCursorCashFlowMonthly != null) {
            if (mCursorCashFlowMonthly.getCount() == 0) {

                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_MONTH, monthFormat.format(mDate));
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_YEAR, yearFormat.format(mDate));
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_INCOME, mNewItemValue);
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_INCOME_PLAN, 0);
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_BALANCE, 0);
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_CASH_FLOW, mNewItemValue);

                getContentResolver().insert(Prefs.URI_CASH_FLOW_MONTHLY, cv);
            } else {

                mCursorCashFlowMonthly.moveToFirst();

                mCashFlowMonthly = mCursorCashFlowMonthly
                        .getInt(mCursorCashFlowMonthly
                                .getColumnIndex(Prefs.CASH_FLOW_MONTHLY_FIELD_CASH_FLOW));

                mCurrentItemValue = mCursorCashFlowMonthly
                        .getInt(mCursorCashFlowMonthly
                                .getColumnIndex(Prefs.CASH_FLOW_MONTHLY_FIELD_INCOME));

                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_INCOME, mCurrentItemValue + mNewItemValue);
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_CASH_FLOW, mCashFlowMonthly + mNewItemValue);

                getContentResolver().update(Prefs.URI_CASH_FLOW_MONTHLY, cv,
                        Prefs.CASH_FLOW_MONTHLY_FIELD_MONTH + " = " + monthFormat.format(mDate), null);
            }
        }
        if (mCursorItem != null) {
            mCursorItem.close();
        }
        if (mCursorCashFlowMonthly != null) {
            mCursorCashFlowMonthly.close();
        }
    }


    //Check if the input expense exist in the table expense_name
    //If yes - it will not include it into the table
    private void checkExpenseNameAvailability(Cursor c, ContentValues cv, String name) {

        //    naming(MainActivity.sTabPosition);

        if (c.moveToFirst()) {
            do {
                if ((c.getString(c.getColumnIndex(Prefs.EXPENSE_NAMES_FIELD_NAME))).equals(name)) {
                    needToAddNewExpenseName = false;
                    position = c.getPosition();
                }
            } while (c.moveToNext());
            if (needToAddNewExpenseName) {
                getContentResolver().insert(Prefs.URI_EXPENSE_NAMES, cv);
            }
        } else
            getContentResolver().insert(Prefs.URI_EXPENSE_NAMES, cv);
        cv.clear();
    }

    //Coordinate the _id from the "table_expenses" table with the adding id_passive
    //depending of the existing expenses name in the "table_expense_names"
    private void passiveExpenseIdApplication(Cursor c, ContentValues cv) {

        //  naming(MainActivity.sTabPosition);

        try {
            if (needToAddNewExpenseName) {
                c.moveToLast();
                cv.put(Prefs.EXPENSES_FIELD_ID_PASSIVE, Integer.valueOf(c.getString(c.getColumnIndex(Prefs.FIELD_ID))) + 1);
            } else {
                c.moveToPosition(position);
                cv.put(Prefs.EXPENSES_FIELD_ID_PASSIVE, Integer.valueOf(c.getString(c.getColumnIndex(Prefs.FIELD_ID))));
            }
        } catch (android.database.CursorIndexOutOfBoundsException e) {
            cv.put(Prefs.EXPENSES_FIELD_ID_PASSIVE, 1);
        }
    }

    private void checkIncomeNameAvailability(Cursor c, ContentValues cv, String name) {

        //    naming(MainActivity.sTabPosition);

        if (c.moveToFirst()) {
            do {
                if ((c.getString(c.getColumnIndex(Prefs.INCOME_NAMES_FIELD_NAME))).equals(name)) {
                    needToAddNewExpenseName = false;
                    position = c.getPosition();
                }
            } while (c.moveToNext());
            if (needToAddNewExpenseName) {
                getContentResolver().insert(Prefs.URI_INCOME_NAMES, cv);
            }
        } else
            getContentResolver().insert(Prefs.URI_INCOME_NAMES, cv);
        cv.clear();
    }

    //Coordinate the _id from the "table_expenses" table with the adding id_passive
    //depending of the existing expenses name in the "table_expense_names"
    private void passiveIncomeIdApplication(Cursor c, ContentValues cv) {

        //  naming(MainActivity.sTabPosition);

        try {
            if (needToAddNewExpenseName) {
                c.moveToLast();
                cv.put(Prefs.INCOMES_FIELD_ID_PASSIVE, Integer.valueOf(c.getString(c.getColumnIndex(Prefs.FIELD_ID))) + 1);
            } else {
                c.moveToPosition(position);
                cv.put(Prefs.INCOMES_FIELD_ID_PASSIVE, Integer.valueOf(c.getString(c.getColumnIndex(Prefs.FIELD_ID))));
            }
        } catch (android.database.CursorIndexOutOfBoundsException e) {
            cv.put(Prefs.INCOMES_FIELD_ID_PASSIVE, 1);
        }
    }

}