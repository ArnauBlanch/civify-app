package com.civify.activity.fragments.reward;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.civify.R;

public class ShowQrFragment extends Fragment {

    public ShowQrFragment() {
        // Required empty public constructor
    }

    public static ShowQrFragment newInstance() {
        ShowQrFragment fragment = new ShowQrFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_qr, container, false);
    }
}
