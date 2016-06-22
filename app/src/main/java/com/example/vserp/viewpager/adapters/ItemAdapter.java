package com.example.vserp.viewpager.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vserp.viewpager.R;
import com.example.vserp.viewpager.activities.MainActivity;
import com.example.vserp.viewpager.utils.Prefs;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.InnerViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public ItemAdapter(Context mContext, Cursor mCursor) {
        this.mContext = mContext;
        this.mCursor = mCursor;
    }

    @Override
    public InnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_adapter, parent, false);
        return new InnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InnerViewHolder holder, int position) {

        switch (MainActivity.sTabPosition) {

            case MyPagerAdapter.FRAGMENT_EXPENSES:

                mCursor.moveToPosition(position);

                holder.tvTitle.setText(mCursor.getString(mCursor.getColumnIndex(Prefs.EXPENSE_NAMES_FIELD_NAME)));
                holder.tvVolume.setText(mCursor.getString(mCursor.getColumnIndex(Prefs.EXPENSES_FIELD_VOLUME)));
                holder.tvDate.setText(mCursor.getString(mCursor.getColumnIndex(Prefs.EXPENSES_FIELD_DATE)));
                break;

            case MyPagerAdapter.FRAGMENT_INCOMES:

                mCursor.moveToPosition(position);

                holder.tvTitle.setText(mCursor.getString(mCursor.getColumnIndex(Prefs.INCOME_NAMES_FIELD_NAME)));
                holder.tvVolume.setText(mCursor.getString(mCursor.getColumnIndex(Prefs.INCOMES_FIELD_VOLUME)));
                holder.tvDate.setText(mCursor.getString(mCursor.getColumnIndex(Prefs.INCOMES_FIELD_DATE)));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class InnerViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvVolume;
        TextView tvDate;

        InnerViewHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.tvItemTitle);
            tvVolume = (TextView) view.findViewById(R.id.tvItemVolume);
            tvDate = (TextView) view.findViewById(R.id.tvItemDate);

        }
    }
}
