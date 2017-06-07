package com.civify.activity.fragments.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.civify.R;
import com.civify.adapter.UserAdapter;
import com.civify.adapter.UserAttacher;
import com.civify.adapter.UserSimpleCallback;
import com.civify.model.User;
import com.civify.utils.AdapterFactory;

public class ProfileInfoFragment extends Fragment {

    public ProfileInfoFragment() {
        // Required empty public constructor
    }

    public static ProfileInfoFragment newInstance() {
        return new ProfileInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile_info, container, false);
        // TODO: Get user from ProfileFragment and set info
        setUserInfo(view);
        AdapterFactory.getInstance().getUserAdapter(getContext()).updateCurrentUser(
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
            UserAttacher.get(getContext(), UserAdapter.getCurrentUser())
                    .setFullName((TextView) view.findViewById(R.id.user_info_name))
                    .setUsername((TextView) view.findViewById(R.id.user_info_username))
                    .setLevel((TextView) view.findViewById(R.id.user_info_level))
                    .setExperienceWithMax((TextView) view.findViewById(R.id.user_info_xp))
                    .setProgress((ProgressBar) view.findViewById(R.id.user_info_progress))
                    .setCoins((TextView) view.findViewById(R.id.user_info_coins));
        }
    }
}
