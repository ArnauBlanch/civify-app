package com.civify.activity.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.civify.R;
import com.civify.activity.DrawerActivity;

public class AboutFragment extends BasicFragment {

    public AboutFragment() {
        // Required empty public constructor
    }

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getActivity().getString(R.string.about));
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.ABOUT_ID;
    }
}
