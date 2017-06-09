package com.civify.activity.fragments.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;
import com.civify.adapter.UserAdapter;
import com.civify.adapter.UserAttacher;
import com.civify.adapter.UserSimpleCallback;
import com.civify.model.User;
import com.civify.utils.AdapterFactory;

public class ProfileInfoFragment extends BasicFragment {

    public static final String TAG_USER = "user";

    private User mUser;

    public ProfileInfoFragment() {
        // Required empty public constructor
    }

    public static ProfileInfoFragment newInstance() {
        return new ProfileInfoFragment();
    }

    public static ProfileInfoFragment newInstance(User user) {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(TAG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile_info, container, false);
        setUserInfo(view);
        AdapterFactory.getInstance().getUserAdapter(getContext()).updateUser(mUser,
                new UserSimpleCallback() {
                    @Override
                    public void onSuccess(User user) {
                        setUserInfo(view);
                    }

                    @Override
                    public void onFailure() { }
                });
        return view;
    }

    private void setUserInfo(View view) {
        if (view != null) {
            UserAttacher.get((DrawerActivity) getActivity(), null, mUser)
                    .setFullName((TextView) view.findViewById(R.id.user_info_name))
                    .setUsername((TextView) view.findViewById(R.id.user_info_username))
                    .setLevel((TextView) view.findViewById(R.id.user_info_level))
                    .setExperienceWithMax((TextView) view.findViewById(R.id.user_info_xp))
                    .setProgress((ProgressBar) view.findViewById(R.id.user_info_progress))
                    .setCoins((TextView) view.findViewById(R.id.user_info_coins))
                    .setAvatar((ImageView) view.findViewById(R.id.user_info_image))
                    .setBadges((TextView) view.findViewById(R.id.user_info_rewards));
        }
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.PROFILE_ID;
    }

    private void setUser() {
        mUser = getArguments() == null ? UserAdapter.getCurrentUser()
                : (User) getArguments().getSerializable(TAG_USER);
    }
}
