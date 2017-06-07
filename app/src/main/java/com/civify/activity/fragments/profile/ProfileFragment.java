package com.civify.activity.fragments.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.MainActivity;
import com.civify.activity.fragments.BasicFragment;
import com.civify.adapter.LoginAdapter;
import com.civify.utils.AdapterFactory;

public class ProfileFragment extends BasicFragment {

    private LoginAdapter mLoginAdapter;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.PROFILE_ID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdapterFactory adapterFactory = AdapterFactory.getInstance();
        mLoginAdapter = adapterFactory.getLoginAdapter(getContext());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        FragmentTabHost tabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        tabHost.setup(getContext(), getChildFragmentManager(), android.R.id.tabcontent);

        // TODO: Pass User to IssuesProfileFragment
        IssuesProfileFragment issuesFragment = new IssuesProfileFragment();

        tabHost.addTab(tabHost.newTabSpec(getString(R.string.tab_spec_issues))
                                .setIndicator(getString(R.string.tab_label_issues), null),
                            issuesFragment.getClass(), null);
        tabHost.addTab(tabHost.newTabSpec(getString(R.string.tab_spec_badges))
                        .setIndicator(getString(R.string.tab_label_badges), null),
                            TabHostFragment.class, null);

        return view;
    }

    // TODO: Disable sign out if not is the current user

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
