package com.example.vserp.viewpager.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vserp.viewpager.fragments.CashFlowFragment;
import com.example.vserp.viewpager.R;

/**
 * Created by vserp on 6/16/2016.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    public static final int FRAGMENT_CASH_FLOW = 0;
    public static final int FRAGMENT_EXPENSES = 1;
    public static final int FRAGMENT_INCOMES = 2;

    Context context;

    public MyPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        DefaultFragment defaultFragment;
        Bundle argBundle;

        switch (position) {
            case FRAGMENT_CASH_FLOW:
                defaultFragment = new DefaultFragment();
                argBundle = new Bundle();
                argBundle.putString(DefaultFragment.KEY_NAME, context.getString(R.string.title_cash_flow));
                defaultFragment.setArguments(argBundle);
                return defaultFragment;
            case FRAGMENT_EXPENSES:
/*
                defaultFragment = new DefaultFragment();
                argBundle = new Bundle();
                argBundle.putString(DefaultFragment.KEY_NAME, context.getResources().getString(R.string.title_expenses));
                defaultFragment.setArguments(argBundle);
*/
                return new CashFlowFragment();
            case FRAGMENT_INCOMES:
/*
                defaultFragment = new DefaultFragment();
                argBundle = new Bundle();
                argBundle.putString(DefaultFragment.KEY_NAME, context.getResources().getString(R.string.title_incomes));
                defaultFragment.setArguments(argBundle);
*/

                return new CashFlowFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case FRAGMENT_CASH_FLOW:
                return context.getResources().getString(R.string.title_cash_flow);
            case FRAGMENT_EXPENSES:
                return context.getResources().getString(R.string.title_expenses);
            case FRAGMENT_INCOMES:
                return context.getResources().getString(R.string.title_incomes);
        }
        return super.getPageTitle(position);
    }

    public static class DefaultFragment extends Fragment {

        private static final String KEY_NAME = "name";

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(android.R.layout.simple_list_item_1, container, false);
            String name = getArguments().getString(KEY_NAME);
            ((TextView) view.findViewById(android.R.id.text1)).setText(name);

            return view;
        }
    }
}
