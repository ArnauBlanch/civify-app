package com.civify.activity.fragments.wall;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.civify.R;
import com.civify.adapter.issue.IssueAdapter;

import java.util.ArrayList;

public class FilterDialogFragment extends DialogFragment {

    private static final String DIALOG_TAG = "coins_dialog";
    public static final String STATUS = "filter";
    private static final int DIALOG_FRAGMENT = 9;
    public static final String CATEGORIES = "categories";
    private int mFilterSelected;
    private ArrayList<String> mCategoriesSelected;


    public static FilterDialogFragment newInstance(int statusSelected, ArrayList<String> categories) {
        FilterDialogFragment sortDialogFragment = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putInt(STATUS, statusSelected);
        args.putStringArrayList(CATEGORIES, categories);
        sortDialogFragment.setArguments(args);
        return sortDialogFragment;
    }

    public void show(FragmentActivity activity) {
        show(activity.getSupportFragmentManager(), DIALOG_TAG);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mFilterSelected = getArguments().getInt(STATUS);
        mCategoriesSelected = getArguments().getStringArrayList(CATEGORIES);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.filter_dialog_title).setPositiveButton(android.R.string.ok, null);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_filter_issues, null);
        setRadioButtonSelected(view);
        setupView(view);

        return builder.setView(view).create();
    }

    private void setupView(View view) {
        switch (mFilterSelected) {
            case IssueAdapter.UNRESOLVED:
                RadioButton radioUnresolved = (RadioButton) view.findViewById(R.id.radio_unresolved);
                radioUnresolved.setChecked(true);
                break;
            case IssueAdapter.RESOLVED:
                RadioButton radioResolved = (RadioButton) view.findViewById(R.id.radio_resolved);
                radioResolved.setChecked(true);
                break;
            case IssueAdapter.ALL:
                RadioButton radioAll = (RadioButton) view.findViewById(R.id.radio_all);
                radioAll.setChecked(true);
                break;
        }
        for (String category : mCategoriesSelected) {
            int resId = getResources().getIdentifier(category, "id", getActivity().getPackageName());
            CheckBox checkBox = (CheckBox) view.findViewById(resId);
            checkBox.setChecked(true);
        }
    }

    private void setRadioButtonSelected(View view) {
        Log.d(getTag(), Integer.toString(mFilterSelected));
        RadioButton radioButton;
        switch (mFilterSelected) {
            case WallFragment.ASCENDING:
                Log.d(getTag(), "Im here ascending");
                radioButton = (RadioButton) view.findViewById(R.id.radio_ascending);
                break;
            case WallFragment.DESCENDING:
                radioButton = (RadioButton) view.findViewById(R.id.radio_descending);
                break;
            case WallFragment.PROXIMITY:
                radioButton = (RadioButton) view.findViewById(R.id.radio_proximity);
                break;
            case WallFragment.NUM_CONFIRM:
                radioButton = (RadioButton) view.findViewById(R.id.radio_confirm);
                break;
            default:
                radioButton = null;
                break;
        }
        if (radioButton != null) {
            Log.d(getTag(), "Im here selecting");
            radioButton.setChecked(true);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
