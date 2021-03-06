package com.civify.activity.fragments.wall;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.ToggleButton;

import com.civify.R;
import com.civify.adapter.issue.IssueAdapter;
import com.civify.utils.ConfirmDialog;

import java.util.ArrayList;

public class FilterDialogFragment extends DialogFragment {

    public static final String STATUS = "filter";
    public static final String RISK = "risk";
    public static final String CATEGORIES = "categories";

    public static final String ID_STRING = "id";
    public static final String CHECKBOX = "checkbox_";

    private static final String DIALOG_TAG = "coins_dialog";

    private int mFilterSelected;
    private ArrayList<String> mCategoriesSelected;
    private Intent mIntent;
    private View mView;
    private ArrayList<String> mAllCategories;
    private boolean mCategoriesChecked;
    private ToggleButton mToggleButton;
    private int mRiskSelected;

    public static FilterDialogFragment newInstance(int statusSelected, ArrayList<String> categories,
            int riskSelected) {
        Bundle args = new Bundle();
        args.putInt(STATUS, statusSelected);
        args.putInt(RISK, riskSelected);
        args.putStringArrayList(CATEGORIES, categories);
        FilterDialogFragment sortDialogFragment = new FilterDialogFragment();
        sortDialogFragment.setArguments(args);
        return sortDialogFragment;
    }

    public void show(FragmentActivity activity) {
        show(activity.getSupportFragmentManager(), DIALOG_TAG);
    }

    private void initAllCategories() {
        mAllCategories = new ArrayList<>();
        mAllCategories.add("road_signs");
        mAllCategories.add("illumination");
        mAllCategories.add("grove");
        mAllCategories.add("street_furniture");
        mAllCategories.add("trash_and_cleaning");
        mAllCategories.add("public_transport");
        mAllCategories.add("suggestion");
        mAllCategories.add("other");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mFilterSelected = getArguments().getInt(STATUS);
        mCategoriesSelected = getArguments().getStringArrayList(CATEGORIES);
        mRiskSelected = getArguments().getInt(RISK);
        initAllCategories();
        mIntent = new Intent();



        ConfirmDialog dialog = new ConfirmDialog(getContext());

        dialog.setTitle(R.string.filter_dialog_title);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        mView = inflater.inflate(R.layout.dialog_filter_issues, null);
        setRadioButtonSelected(mView);
        mToggleButton = (ToggleButton) mView.findViewById(R.id.toggle_select_all_categories);
        if (mCategoriesSelected.size() == mAllCategories.size()) {
            mCategoriesChecked = true;
            mToggleButton.setChecked(true);
        } else {
            mCategoriesChecked = false;
            mToggleButton.setChecked(false);
        }
        setupView(mView);

        dialog.setView(mView);
        dialog.setPositiveButton(null, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int w) {
                setupIntent();
                getTargetFragment().onActivityResult(getTargetRequestCode(),
                        Activity.RESULT_OK, mIntent);
            }
        });
        return dialog;
    }

    private void setupIntent() {
        // for status
        mIntent.putExtra(STATUS, mFilterSelected);
        ArrayList<String> newCategories = new ArrayList<>();
        for (String category : mAllCategories) {
            int resId = getResources().getIdentifier(CHECKBOX + category, ID_STRING,
                    getActivity().getPackageName());
            CheckBox checkBox = (CheckBox) mView.findViewById(resId);
            if (checkBox != null && checkBox.isChecked()) {
                newCategories.add(category);
            }
        }
        mIntent.putStringArrayListExtra(CATEGORIES, newCategories);
        mIntent.putExtra(RISK, mRiskSelected);
    }

    private void setupView(View view) {
        RadioButton radioUnresolved = (RadioButton) view.findViewById(R.id.radio_unresolved);
        radioUnresolved.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilterSelected = IssueAdapter.UNRESOLVED;
                Log.d(getTag(), Integer.toString(mFilterSelected));
            }
        });

        RadioButton radioResolved = (RadioButton) view.findViewById(R.id.radio_resolved);
        radioResolved.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilterSelected = IssueAdapter.RESOLVED;
                Log.d(getTag(), Integer.toString(mFilterSelected));
            }
        });

        RadioButton radioAll = (RadioButton) view.findViewById(R.id.radio_all);
        radioAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilterSelected = IssueAdapter.ALL;
                Log.d(getTag(), Integer.toString(mFilterSelected));
            }
        });

        if (mFilterSelected == IssueAdapter.UNRESOLVED) {
            radioUnresolved.setChecked(true);
        } else if (mFilterSelected == IssueAdapter.RESOLVED) {
            radioResolved.setChecked(true);
        } else if (mFilterSelected == IssueAdapter.ALL) {
            radioAll.setChecked(true);
        }
        setupCheckboxes(view);
        setupToggleButton(view);
        setupRiskRadio(view);
    }

    private void setupToggleButton(View view) {
        ToggleButton toggleButton =
                (ToggleButton) view.findViewById(R.id.toggle_select_all_categories);
        if (toggleButton != null) {
            toggleButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCategoriesChecked = !mCategoriesChecked;
                    setCategoriesChecked(mCategoriesChecked);
                }
            });
        }
    }

    private void setupCheckboxes(View view) {
        for (String category : mCategoriesSelected) {
            int resId = getResources().getIdentifier(CHECKBOX + category, ID_STRING,
                    getActivity().getPackageName());
            CheckBox checkBox = (CheckBox) view.findViewById(resId);
            if (checkBox != null) checkBox.setChecked(true);
        }
    }

    private void setupRiskRadio(View view) {
        RadioButton radioRiskYes = (RadioButton) view.findViewById(R.id.radio_risk_yes);
        RadioButton radioRiskNo = (RadioButton) view.findViewById(R.id.radio_risk_no);
        RadioButton radioRiskAll = (RadioButton) view.findViewById(R.id.radio_risk_all);

        radioRiskYes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRiskSelected = IssueAdapter.RISK_YES;
            }
        });

        radioRiskNo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRiskSelected = IssueAdapter.RISK_NO;
            }
        });

        radioRiskAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRiskSelected = IssueAdapter.RISK_ALL;
            }
        });

        switch (mRiskSelected) {
            case IssueAdapter.RISK_YES:
                radioRiskYes.setChecked(true);
                break;
            case IssueAdapter.RISK_NO:
                radioRiskNo.setChecked(true);
                break;
            case IssueAdapter.RISK_ALL:
                radioRiskAll.setChecked(true);
                break;
            default:
                break;
        }
    }

    private void setCategoriesChecked(boolean checked) {
        for (String category : mAllCategories) {
            int resId = getResources().getIdentifier(CHECKBOX + category, ID_STRING,
                    getActivity().getPackageName());
            CheckBox checkBox = (CheckBox) mView.findViewById(resId);
            if (checkBox != null) checkBox.setChecked(checked);
        }
    }

    private void setRadioButtonSelected(View view) {
        Log.d(getTag(), Integer.toString(mFilterSelected));
        RadioButton radioButton;
        switch (mFilterSelected) {
            case WallFragment.ASCENDING:
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
            radioButton.setChecked(true);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
