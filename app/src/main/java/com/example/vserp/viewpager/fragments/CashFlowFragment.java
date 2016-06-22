package com.example.vserp.viewpager.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vserp.viewpager.R;
import com.example.vserp.viewpager.activities.MainActivity;
import com.example.vserp.viewpager.adapters.MyPagerAdapter;
import com.example.vserp.viewpager.utils.Prefs;
import com.example.vserp.viewpager.views.RoundChart;

import java.util.HashMap;

/**
 * Created by vserp on 6/16/2016.
 */

public class CashFlowFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<HashMap<String, String>> {

    public static String LAST_MONTH_ITEM = "last_month_result";
    public static String CURRENT_MONTH_ITEM = "current_month";
    public static String NEXT_MONTH_ITEM_PLAN = "next_month_plan";

    private TextView tvLastMonth;
    private TextView tvCurentMonth;
    private EditText etPlanNextMonth;


    static Uri URI_ITEM;
    static String ITEM_FIELD_VOLUME;

    private RoundChart rcCashFlow;
    private int mItemPlan;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((MainActivity) getActivity()).setFragmentInfo(MyPagerAdapter.FRAGMENT_EXPENSES);

        View view = inflater.inflate(R.layout.fragment_cash_flow, container, false);

        tvLastMonth = (TextView) view.findViewById(R.id.tv_last_month);
        tvCurentMonth = (TextView) view.findViewById(R.id.tv_current_month);
        etPlanNextMonth = (EditText) view.findViewById(R.id.et_plan_next_month);
        rcCashFlow = (RoundChart) view.findViewById(R.id.rcRoundDiagram);

        getActivity().getSupportLoaderManager().initLoader(1, null, this);

        return view;
    }

    @Override
    public Loader<HashMap<String, String>> onCreateLoader(int id, Bundle args) {
        return new HashMapLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<HashMap<String, String>> loader, HashMap<String, String> data) {

        tvCurentMonth.setText(data.get(CURRENT_MONTH_ITEM));
        tvLastMonth.setText(data.get(CURRENT_MONTH_ITEM));

        try {
            mItemPlan = Integer.parseInt(data.get(NEXT_MONTH_ITEM_PLAN));
        }catch (NumberFormatException e){
            mItemPlan = 5000;
        }

        int percent = 0;
        int item = Integer.parseInt(data.get(CURRENT_MONTH_ITEM));
        if (mItemPlan != 0)
        percent = item * 100 / mItemPlan;
        rcCashFlow.setValues(percent);
        rcCashFlow.invalidate();
    }

    @Override
    public void onLoaderReset(Loader<HashMap<String, String>> loader) {

    }

    private static class HashMapLoader extends Loader<HashMap<String, String>> {

        HashMap<String, String> result;

        HashMapLoader(Context context) {
            super(context);
            result = new HashMap<>();
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            onForceLoad();
        }

        @Override
        protected void onForceLoad() {
            super.onForceLoad();

            naming(MainActivity.sTabPosition);

            getItemCursor(URI_ITEM,ITEM_FIELD_VOLUME);

            getMonthlyCursor(Prefs.URI_CASH_FLOW_MONTHLY);

        }
        private void naming(int position){

            if (MainActivity.sTabPosition == MyPagerAdapter.FRAGMENT_EXPENSES) {
                URI_ITEM = Prefs.URI_EXPENSES;
                ITEM_FIELD_VOLUME = Prefs.EXPENSES_FIELD_VOLUME;
            } else {
                URI_ITEM = Prefs.URI_INCOMES;
                ITEM_FIELD_VOLUME = Prefs.INCOMES_FIELD_VOLUME;
            }

        }

        private void getItemCursor(Uri uriItem, String itemFieldVolume){

            Cursor cursor = getContext().getContentResolver()
                    .query(uriItem, new String[]{itemFieldVolume}, null, null, null, null);

            if (cursor != null) {

                cursor.moveToFirst();

                if (cursor.getCount() != 0) {

                    int value = 0;

                    do {
                        value = value + cursor.getInt(cursor.getColumnIndex(ITEM_FIELD_VOLUME));
                    } while (cursor.moveToNext());

                    result.put(CURRENT_MONTH_ITEM, String.valueOf(value));

                    deliverResult(result);
                } else {
                    result.put(CURRENT_MONTH_ITEM, "0");
                }
                cursor.close();
            }
        }

        private void getMonthlyCursor(Uri uriCashFlowMonthly) {

            Cursor cursor = getContext().getContentResolver()
                    .query(uriCashFlowMonthly, null, null, null, null, null);

            if (cursor != null) {

                cursor.moveToFirst();

                if (cursor.getCount() != 0) {

                    int expensePlan = 0;

                        expensePlan = expensePlan + cursor.getInt(cursor.getColumnIndex(Prefs.CASH_FLOW_MONTHLY_FIELD_EXPENSE_PLAN));

                    result.put(NEXT_MONTH_ITEM_PLAN, String.valueOf(expensePlan));

                    deliverResult(result);
                } else {
                    result.put(NEXT_MONTH_ITEM_PLAN, "10000");
                }
                cursor.close();
            }
        }
    }
}
