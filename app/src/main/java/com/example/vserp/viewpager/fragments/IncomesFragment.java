package com.example.vserp.viewpager.fragments;

import android.content.Context;
import android.database.Cursor;

import android.os.Bundle;

import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import android.text.Editable;
import android.text.TextWatcher;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vserp.viewpager.R;
import com.example.vserp.viewpager.activities.MainActivity;
import com.example.vserp.viewpager.adapters.MyPagerAdapter;
import com.example.vserp.viewpager.services.MyIntentService;
import com.example.vserp.viewpager.utils.Prefs;
import com.example.vserp.viewpager.views.RoundChart;

/**
 * Created by vserp on 6/16/2016.
 */

public class IncomesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static String CURRENT_MONTH_ITEM;
    private static String NEXT_MONTH_ITEM_PLAN;
    private static String LAST_MONTH_ITEM;

    private static TextView tvLastMonth;
    private static TextView tvCurrentMonth;
    private static EditText etPlanNextMonth;

    public static String sPlan;

    private static RoundChart rcCashFlow;
    private boolean isOpened;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((MainActivity) getActivity()).setFragmentInfo(MyPagerAdapter.FRAGMENT_INCOMES);

        final View view = inflater.inflate(R.layout.fragment_cash_flow, container, false);

        tvLastMonth = (TextView) view.findViewById(R.id.tv_last_month);
        tvCurrentMonth = (TextView) view.findViewById(R.id.tv_current_month);
        etPlanNextMonth = (EditText) view.findViewById(R.id.et_plan_next_month);
        rcCashFlow = (RoundChart) view.findViewById(R.id.rcRoundDiagram);

        etPlanNextMonth.setOnKeyListener(
                new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            // сохраняем текст, введенный до нажатия Enter в переменную
                            sPlan = etPlanNextMonth.getText().toString();
                            MyIntentService.startActionAddIncomePlan(getActivity(), sPlan);

                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            return true;
                        }
                        return false;
                    }
                }
        );

        getActivity().getSupportLoaderManager().initLoader(Prefs.ID_LOADER_INCOME_NAMES, null, this);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Prefs.URI_CASH_FLOW_MONTHLY, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null) {

            data.moveToLast();

            if (data.getCount() != 0) {

                NEXT_MONTH_ITEM_PLAN = String.valueOf(data.getInt(data
                        .getColumnIndex(Prefs.CASH_FLOW_MONTHLY_FIELD_INCOME_PLAN)));
                CURRENT_MONTH_ITEM = String.valueOf(data.getInt(data
                        .getColumnIndex(Prefs.CASH_FLOW_MONTHLY_FIELD_INCOME)));
            } else {
                NEXT_MONTH_ITEM_PLAN = "0";
                CURRENT_MONTH_ITEM = "0";
                LAST_MONTH_ITEM = "0";
            }

            data.moveToPrevious();
            try {
                LAST_MONTH_ITEM = String.valueOf(data.getInt(data
                        .getColumnIndex(Prefs.CASH_FLOW_MONTHLY_FIELD_INCOME)));
            }catch (android.database.CursorIndexOutOfBoundsException e){
                LAST_MONTH_ITEM = "0";
            }
        }
        drawDiagram();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public static void drawDiagram() {

        int percentOfPlan;

        tvLastMonth.setText(LAST_MONTH_ITEM);
        tvCurrentMonth.setText(CURRENT_MONTH_ITEM);
        etPlanNextMonth.setText(NEXT_MONTH_ITEM_PLAN);

        if (!NEXT_MONTH_ITEM_PLAN.equals("0")) {
            if (Integer.parseInt(NEXT_MONTH_ITEM_PLAN) != 0) {
                percentOfPlan = Integer.parseInt(CURRENT_MONTH_ITEM)
                        * 100 / Integer.parseInt(NEXT_MONTH_ITEM_PLAN);
            } else {
                percentOfPlan = 0;
            }
            rcCashFlow.setValues(percentOfPlan);
            rcCashFlow.invalidate();
        }
    }
}
