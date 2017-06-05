package com.civify.activity.fragments.wall;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.civify.R;

public class SortDialogFragment extends DialogFragment {

    private static final String DIALOG_TAG = "coins_dialog";
    private static final String SORT = "sort";
    private int mSortSelected;

    public static void show(FragmentActivity activity, int sortingSelected) {
        SortDialogFragment fragment = new SortDialogFragment();
        Bundle args = new Bundle();
        Log.d("lala", Integer.toString(sortingSelected));
        args.putInt(SORT, sortingSelected);
        fragment.setArguments(args);
        fragment.show(activity.getSupportFragmentManager(), DIALOG_TAG);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mSortSelected = getArguments().getInt(SORT);
        Log.d(getTag(), Integer.toString(mSortSelected));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.sort_dialog_title).setPositiveButton(android.R.string.ok, null);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_sort_issues, null);
        setRadioButtonSelected(view);
        setupView(view);

        return builder.setView(view).create();
    }

    private void setupView(View view) {
        view.findViewById(R.id.radio_ascending).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                WallFragment parent = (WallFragment) getParentFragment();
                parent.dialogSelectedSort(WallFragment.ASCENDING);
                dismiss();
            }
        });
        view.findViewById(R.id.radio_descending).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                WallFragment parent = (WallFragment) getParentFragment();
                parent.dialogSelectedSort(WallFragment.DESCENDING);
                dismiss();
            }
        });
        view.findViewById(R.id.radio_proximity).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                WallFragment parent = (WallFragment) getParentFragment();
                parent.dialogSelectedSort(WallFragment.PROXIMITY);
                dismiss();
            }
        });
        view.findViewById(R.id.radio_confirm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                WallFragment parent = (WallFragment) getParentFragment();
                parent.dialogSelectedSort(WallFragment.NUM_CONFIRM);
                dismiss();
            }
        });
    }

    private void setRadioButtonSelected(View view) {
        Log.d(getTag(), Integer.toString(mSortSelected));
        RadioButton radioButton;
        switch (mSortSelected) {
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
