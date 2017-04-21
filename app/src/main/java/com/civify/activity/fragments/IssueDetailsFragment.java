package com.civify.activity.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.civify.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IssueDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IssueDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IssueDetailsFragment extends Fragment {

    public IssueDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IssueDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IssueDetailsFragment newInstance(String param1, String param2) {
        return new IssueDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_issue_details, container, false);
    }
}
