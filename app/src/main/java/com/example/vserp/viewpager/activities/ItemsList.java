package com.example.vserp.viewpager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.vserp.viewpager.R;
import com.example.vserp.viewpager.adapters.ItemAdapter;
import com.example.vserp.viewpager.adapters.MyPagerAdapter;
import com.example.vserp.viewpager.utils.Prefs;

/**
 * Created by vserp on 6/17/2016.
 */

public class ItemsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView rvItemsList = (RecyclerView) findViewById(R.id.rv_items_list);

        rvItemsList.setLayoutManager(new LinearLayoutManager(this));

        switch (MainActivity.sTabPosition) {

            case MyPagerAdapter.FRAGMENT_EXPENSES:
                rvItemsList.setAdapter(new ItemAdapter(this, getContentResolver()
                        .query(Prefs.URI_ALL_EXPENSES, null, null, null, null)));
                break;
            case MyPagerAdapter.FRAGMENT_INCOMES:
                rvItemsList.setAdapter(new ItemAdapter(this, getContentResolver()
                        .query(Prefs.URI_ALL_INCOMES, null, null, null, null)));
                break;
        }
    }
}
