package com.example.vserp.viewpager.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.vserp.viewpager.activities.MainActivity;
import com.example.vserp.viewpager.adapters.MyPagerAdapter;
import com.example.vserp.viewpager.utils.Prefs;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyIntentService extends IntentService {

    private String CASH_FLOW_MONTHLY_FIELD_ITEM;
    private String ITEM_FIELD_ID_PASSIVE;
    private String ITEM_NAMES_FIELD_CONSTANT_PAYMENT;
    private String ITEM_NAMES_FIELD_NAME;
    private Uri URI_ITEM_NAMES;

    private String ITEM_FIELD_VOLUME;
    private String ITEM_FIELD_DATE;
    private Uri URI_ITEM;

    private static final String ACTION_ADD_NEW_ITEM = "com.example.vserp.viewpager.Services.action.ADD_NEW_ITEM";

    private static final String EXTRA_TITLE = "com.example.vserp.viewpager.Services.extra.TITLE";
    private static final String EXTRA_VOLUME = "com.example.vserp.viewpager.Services.extra.VOLUME";
    private static final String EXTRA_CONSTANT = "com.example.vserp.viewpager.Services.extra.CONSTANT";

    private boolean needToAddNewExpenseName = true;
    private int position = 0;//cursor position
    public static int mCurrentItemValue;
public static int proba;
    public MyIntentService() {
        super("MyIntentService");
    }

    public static void startActionAddNewItem(Context context, String title, double volume, int constant) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_ADD_NEW_ITEM);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_VOLUME, volume);
        intent.putExtra(EXTRA_CONSTANT, constant);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action) {
                case ACTION_ADD_NEW_ITEM:
                    final String itemTitle = intent.getStringExtra(EXTRA_TITLE);
                    final double itemVolume = intent.getDoubleExtra(EXTRA_VOLUME, 0);
                    final int itemConstant = intent.getIntExtra(EXTRA_CONSTANT, 0);
                    handleActionAddItem(itemTitle, itemVolume, itemConstant);
                    break;
            }
        }
    }

    private void handleActionAddItem(String title, double volume, int constant) {

        ContentValues cv = new ContentValues();

        naming(MainActivity.sTabPosition);

        cv.put(ITEM_NAMES_FIELD_NAME, title);
        cv.put(ITEM_NAMES_FIELD_CONSTANT_PAYMENT, constant);

//Input new values of expense/income names into the corresponding tables
        getContentResolver().insert(URI_ITEM_NAMES, cv);

//Manage duplicates
        Cursor mCursorItem = getContentResolver().query(URI_ITEM_NAMES, null, null, null, null);

        if (mCursorItem != null) {
            checkItemNameAvailability(mCursorItem, cv, title);

            passiveIdApplication(mCursorItem, cv);
        }
//Manage corresponding passive_Id and input new values into the corresponding tables
        Calendar mCalendar = Calendar.getInstance();

        Date mDate = new Date(mCalendar.getTimeInMillis());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

        cv.put(ITEM_FIELD_VOLUME, volume);
        cv.put(ITEM_FIELD_DATE, dateFormat.format(mDate));

        getContentResolver().insert(URI_ITEM, cv);

//Clear ContentValues except ITEM_FIELD_VOLUME for the further usage
// with new cursor of Cash Flow Monthly table
        cv.remove(ITEM_NAMES_FIELD_NAME);
        cv.remove(ITEM_NAMES_FIELD_CONSTANT_PAYMENT);
        cv.remove(ITEM_FIELD_DATE);
        cv.remove(ITEM_FIELD_ID_PASSIVE);

        Cursor mCursorCashFlowMonthly = getContentResolver().query(Prefs.URI_CASH_FLOW_MONTHLY
                , null, Prefs.CASH_FLOW_MONTHLY_FIELD_MONTH + " = " + monthFormat.format(mDate), null, null);

        int mCashFlowMonthly;
        int tabPosition = MainActivity.sTabPosition;
        int mNewItemValue = cv.getAsInteger(ITEM_FIELD_VOLUME);

        if (mCursorCashFlowMonthly != null) {
            if (mCursorCashFlowMonthly.getCount() == 0) {

                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_MONTH, monthFormat.format(mDate));
                cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_YEAR, yearFormat.format(mDate));
                cv.put(CASH_FLOW_MONTHLY_FIELD_ITEM, mNewItemValue);

                if (tabPosition == MyPagerAdapter.FRAGMENT_EXPENSES) {
                    cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_CASH_FLOW, - mNewItemValue);
                }else {
                    cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_CASH_FLOW, mNewItemValue);
                }

                getContentResolver().insert(Prefs.URI_CASH_FLOW_MONTHLY, cv);
            } else {

                mCursorCashFlowMonthly.moveToFirst();

                mCashFlowMonthly = mCursorCashFlowMonthly.getInt(mCursorCashFlowMonthly.getColumnIndex(
                        Prefs.CASH_FLOW_MONTHLY_FIELD_CASH_FLOW));

                mCurrentItemValue = mCursorCashFlowMonthly.getInt(mCursorCashFlowMonthly.getColumnIndex(
                        CASH_FLOW_MONTHLY_FIELD_ITEM));

                cv.put(CASH_FLOW_MONTHLY_FIELD_ITEM, mCurrentItemValue + mNewItemValue);

                if (tabPosition == MyPagerAdapter.FRAGMENT_EXPENSES) {

                    cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_CASH_FLOW,mCashFlowMonthly - mNewItemValue);
                }else {
                    cv.put(Prefs.CASH_FLOW_MONTHLY_FIELD_CASH_FLOW,mCashFlowMonthly + mNewItemValue);
                }

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
    private void checkItemNameAvailability(Cursor c, ContentValues cv, String name) {

        naming(MainActivity.sTabPosition);

        if (c.moveToFirst()) {
            do {
                if ((c.getString(c.getColumnIndex(ITEM_NAMES_FIELD_NAME))).equals(name)) {
                    needToAddNewExpenseName = false;
                    position = c.getPosition();
                }
            } while (c.moveToNext());
            if (needToAddNewExpenseName) {
                getContentResolver().insert(URI_ITEM_NAMES, cv);
            }
        } else
            getContentResolver().insert(URI_ITEM_NAMES, cv);
        cv.clear();
    }

    //Coordinate the _id from the "table_expenses" table with the adding id_passive
    //depending of the existing expenses name in the "table_expense_names"
    private void passiveIdApplication(Cursor c, ContentValues cv) {

        naming(MainActivity.sTabPosition);

        try {
            if (needToAddNewExpenseName) {
                c.moveToLast();
                cv.put(ITEM_FIELD_ID_PASSIVE, Integer.valueOf(c.getString(c.getColumnIndex(Prefs.FIELD_ID))) + 1);
            } else {
                c.moveToPosition(position);
                cv.put(ITEM_FIELD_ID_PASSIVE, Integer.valueOf(c.getString(c.getColumnIndex(Prefs.FIELD_ID))));
            }
        } catch (android.database.CursorIndexOutOfBoundsException e) {
            cv.put(ITEM_FIELD_ID_PASSIVE, 1);
        }
    }

    private void naming(int position) {

        if (position == MyPagerAdapter.FRAGMENT_EXPENSES) {
            ITEM_NAMES_FIELD_NAME = Prefs.EXPENSE_NAMES_FIELD_NAME;
            URI_ITEM_NAMES = Prefs.URI_EXPENSE_NAMES;

            ITEM_NAMES_FIELD_CONSTANT_PAYMENT = Prefs.EXPENSE_NAMES_FIELD_CRITICAL;
            ITEM_FIELD_ID_PASSIVE = Prefs.EXPENSES_FIELD_ID_PASSIVE;
            ITEM_FIELD_VOLUME = Prefs.EXPENSES_FIELD_VOLUME;
            ITEM_FIELD_DATE = Prefs.EXPENSES_FIELD_DATE;
            URI_ITEM = Prefs.URI_EXPENSES;

            CASH_FLOW_MONTHLY_FIELD_ITEM = Prefs.CASH_FLOW_MONTHLY_FIELD_EXPENSE;
        } else {
            ITEM_NAMES_FIELD_CONSTANT_PAYMENT = Prefs.INCOME_NAMES_FIELD_CONSTANT_PAYMENT;
            ITEM_NAMES_FIELD_NAME = Prefs.INCOME_NAMES_FIELD_NAME;
            URI_ITEM_NAMES = Prefs.URI_INCOME_NAMES;

            ITEM_FIELD_VOLUME = Prefs.INCOMES_FIELD_VOLUME;
            ITEM_FIELD_ID_PASSIVE = Prefs.INCOMES_FIELD_ID_PASSIVE;
            ITEM_FIELD_DATE = Prefs.INCOMES_FIELD_DATE;
            URI_ITEM = Prefs.URI_INCOMES;

            CASH_FLOW_MONTHLY_FIELD_ITEM = Prefs.CASH_FLOW_MONTHLY_FIELD_INCOMES;
        }
    }
}