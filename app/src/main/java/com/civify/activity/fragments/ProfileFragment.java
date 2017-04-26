package com.civify.activity.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.MainActivity;
import com.civify.adapter.LoginAdapter;
import com.civify.model.User;
import com.civify.utils.AdapterFactory;
import com.mikhaellopez.circularimageview.CircularImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static String mTitle;

    private LoginAdapter mLoginAdapter;
    private FragmentTabHost mTabHost;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdapterFactory adapterFactory = AdapterFactory.getInstance();
        SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences("USERPREFS", Context.MODE_PRIVATE);
        mLoginAdapter = adapterFactory.getLoginAdapter(sharedPreferences);
        setHasOptionsMenu(true);
        mTitle = getResources().getString(R.string.profile_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(mTitle);

        mTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        mTabHost.setup(getContext(), getChildFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.tab_spec_issues))
                        .setIndicator(getString(R.string.tab_label_issues), null),
                            TabHostFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.tab_spec_badges))
                        .setIndicator(getString(R.string.tab_label_badges), null),
                            TabHostFragment.class, null);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signout:
                signOut();
                return false;
            default:
                break;
        }

        return false;
    }

    private void signOut() {
        mLoginAdapter.logout();
        Context applicationContext = getActivity().getApplicationContext();
        Intent intent = new Intent(applicationContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
