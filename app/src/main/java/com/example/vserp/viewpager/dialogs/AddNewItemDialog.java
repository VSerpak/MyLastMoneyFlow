package com.example.vserp.viewpager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.vserp.viewpager.R;
import com.example.vserp.viewpager.services.MyIntentService;
import com.example.vserp.viewpager.activities.MainActivity;
import com.example.vserp.viewpager.adapters.MyPagerAdapter;

public class AddNewItemDialog extends DialogFragment {

    private AutoCompleteTextView acNameOfItem;
    private EditText etVolumeOfItem;
    private CheckBox chbIsConstant;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_new_item, null);

        acNameOfItem = (AutoCompleteTextView) view.findViewById(R.id.acTitleOfItem);
        etVolumeOfItem = (EditText) view.findViewById(R.id.etVolumeOfItem);
        chbIsConstant = (CheckBox) view.findViewById(R.id.chbCriticalExpense);

        String itemTitle;

        if (MainActivity.sTabPosition == MyPagerAdapter.FRAGMENT_EXPENSES) {
            itemTitle = getString(R.string.dialog_item_title_input_expense);
            chbIsConstant.setText(R.string.isCritical);
        } else {
            itemTitle = getString(R.string.dialog_item_title_input_income);
            chbIsConstant.setText(R.string.isConstant);
        }
        builder.setView(view)
                .setMessage(R.string.dialog_item_message)
                .setTitle(itemTitle)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addNewItem();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    private void addNewItem() {

        String title = acNameOfItem.getText().toString();
        double volume = Double.parseDouble(etVolumeOfItem.getText().toString());
        int constant = chbIsConstant.isChecked() ? 1 : 0;

        MyIntentService.startActionAddNewItem(getActivity(), title, volume, constant);
    }
}
