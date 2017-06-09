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
import com.civify.activity.fragments.badge.BadgeViewFragment;
import com.civify.adapter.LoginAdapter;
import com.civify.adapter.UserAdapter;
import com.civify.model.User;
import com.civify.utils.AdapterFactory;

public class ProfileFragment extends BasicFragment {

    private LoginAdapter mLoginAdapter;
    private User mUser;

    public ProfileFragment() { }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public static ProfileFragment newInstance(User user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(ProfileInfoFragment.TAG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.PROFILE_ID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUser();
        AdapterFactory adapterFactory = AdapterFactory.getInstance();
        mLoginAdapter = adapterFactory.getLoginAdapter(getContext());
        setHasOptionsMenu(mUser.getUserAuthToken()
                .equals(UserAdapter.getCurrentUser().getUserAuthToken()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        FragmentTabHost tabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        tabHost.setup(getContext(), getChildFragmentManager(), android.R.id.tabcontent);

        ProfileInfoFragment profileInfoFragment = ProfileInfoFragment.newInstance(mUser);
        IssuesProfileFragment issuesFragment = IssuesProfileFragment.newInstance(mUser);
        BadgeViewFragment badgeViewFragment = BadgeViewFragment.newInstance(mUser);

        tabHost.addTab(tabHost.newTabSpec(getString(R.string.tab_spec_issues))
                                .setIndicator(getString(R.string.tab_label_issues), null),
                            issuesFragment.getClass(), issuesFragment.getArguments());
        tabHost.addTab(tabHost.newTabSpec(getString(R.string.tab_spec_badges))
                        .setIndicator(getString(R.string.tab_label_badges), null),
                            badgeViewFragment.getClass(), badgeViewFragment.getArguments());

        getChildFragmentManager().beginTransaction()
                .replace(R.id.profile_info_fragment_placeholder, profileInfoFragment).commit();

        return view;
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

    private void setUser() {
        mUser = getArguments() == null ? UserAdapter.getCurrentUser()
                : (User) getArguments().getSerializable(ProfileInfoFragment.TAG_USER);
    }
}
