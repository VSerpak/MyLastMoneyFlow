package com.example.vserp.viewpager.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.vserp.viewpager.R;
import com.example.vserp.viewpager.adapters.MyPagerAdapter;
import com.example.vserp.viewpager.dialogs.AddNewItemDialog;
import com.example.vserp.viewpager.utils.Prefs;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener,
        ViewPager.OnPageChangeListener{

    private MyPagerAdapter mSectionsPagerAdapter;

    private Toolbar toolbar;
    private ViewPager mViewPager;
    private FloatingActionButton fab;

    public static int sTabPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Money Flow");
        //Without support it will not change the title
        //setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        //we have to create new sample of MyPagerAdapter instead of default
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter = new MyPagerAdapter(this, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        if (mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.addOnPageChangeListener(this);
        }

        //assign tab titles
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDashBoard);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(mViewPager);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        switch (sTabPosition) {
            case MyPagerAdapter.FRAGMENT_CASH_FLOW:
                getMenuInflater().inflate(R.menu.toolbar_menu_cash_flow, menu);
                break;
            case MyPagerAdapter.FRAGMENT_EXPENSES:
                getMenuInflater().inflate(R.menu.toolbar_menu_expenses, menu);
                break;
            case MyPagerAdapter.FRAGMENT_INCOMES:
                getMenuInflater().inflate(R.menu.toolbar_menu_incomes, menu);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intentItems = new Intent(this, ItemsList.class);

        switch (item.getItemId()) {
            case R.id.menu_item_user_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.menu_item_list_expenses:
                startActivity(intentItems);
                break;
            case R.id.menu_item_list_incomes:
                startActivity(intentItems);
                break;

        }
        return true;
    }

    public void setFragmentInfo(int position) {
        switch (position) {
            case MyPagerAdapter.FRAGMENT_CASH_FLOW:
                toolbar.setTitle(R.string.title_cash_flow);

                break;
            case MyPagerAdapter.FRAGMENT_EXPENSES:
                toolbar.setTitle(R.string.title_expenses);
                break;
            case MyPagerAdapter.FRAGMENT_INCOMES:
                toolbar.setTitle(R.string.title_incomes);
                break;
        }
        setSupportActionBar(toolbar);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {

        sTabPosition = position;

        switch (position) {
            case MyPagerAdapter.FRAGMENT_CASH_FLOW:
                toolbar.setTitle(R.string.title_cash_flow);
                fab.setVisibility(View.GONE);
                break;
            case MyPagerAdapter.FRAGMENT_EXPENSES:
                toolbar.setTitle(R.string.title_expenses);
                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(this);
                break;
            case MyPagerAdapter.FRAGMENT_INCOMES:
                toolbar.setTitle(R.string.title_incomes);
                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(this);
                break;
        }
        setSupportActionBar(toolbar);

        //will call onPrepareOptionMenu() -
        invalidateOptionsMenu();
    }

    @Override
    public void onClick(View v) {

        AddNewItemDialog addNewItemDialog = new AddNewItemDialog();

        switch (v.getId()) {
            case R.id.fab:
                switch (sTabPosition) {
                    case MyPagerAdapter.FRAGMENT_EXPENSES:
                        addNewItemDialog.show(getSupportFragmentManager(), "addItem");
                        Cursor c;

                        Log.d(Prefs.LOG_TAG, "--- EXPENSES_NAMES Table ---");
                        c = getContentResolver().query(Prefs.URI_EXPENSE_NAMES, null, null, null, null);
                        logCursor(c);
                        c = getContentResolver().query(Prefs.URI_EXPENSES, null, null, null, null);
                        logCursor(c);
                        c = getContentResolver().query(Prefs.URI_CASH_FLOW_MONTHLY, null, null, null, null);
                        logCursor(c);

                        c.close();
                        break;
                    case MyPagerAdapter.FRAGMENT_INCOMES:
                        addNewItemDialog.show(getSupportFragmentManager(), "addItem");

                        Cursor c1;


                        Log.d(Prefs.LOG_TAG, "--- INCOMES Table ---");
                        c1 = getContentResolver().query(Prefs.URI_INCOME_NAMES, null, null, null, null);
                        logCursor(c1);
                        c1 = getContentResolver().query(Prefs.URI_INCOMES, null, null, null, null);
                        logCursor(c1);
                        c1 = getContentResolver().query(Prefs.URI_CASH_FLOW_MONTHLY, null, null, null, null);
                        logCursor(c1);

                        c1.close();
                        break;
                }
        }
    }

    void logCursor(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat("\n" + cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d(Prefs.LOG_TAG, str);
                } while (c.moveToNext());
            } else
                Log.d(Prefs.LOG_TAG, "Cursor is null - " + c.getNotificationUri());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



/*      Everything was moved to MyPagerAdapter
     **//**
     * A placeholder fragment containing a simple view.
     *//*
    public static class PlaceholderFragment extends Fragment {
        *//**
     * The fragment argument representing the section number for this
     * fragment.
     *//*
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        *//**
     * Returns a new instance of this fragment for the given section
     * number.
     *//*
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_cash_flow, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    *//**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     *//*
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }*/
}
