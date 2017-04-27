package com.civify.activity.fragments;

import static android.R.attr.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.civify.R;

/**
 * Created by ricardfos on 21/4/17.
 */

public class EditIssueFragment extends Fragment {
    private EditText mDescr;

    public EditIssueFragment() {
        // Required empty public constructor
    }

    public static EditIssueFragment newInstance() {
        EditIssueFragment fragment = new EditIssueFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return new EditIssueFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mDescr = (EditText) getView().findViewById(R.id.descriptionTex);

        return inflater.inflate(R.layout.fragment_edit_issue, container, false);
    }
}
