package com.civify.activity.fragments.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.civify.R;
import com.civify.adapter.UserAdapter;
import com.civify.adapter.UserSimpleCallback;
import com.civify.model.User;
import com.civify.utils.AdapterFactory;

public class ProfileInfoFragment extends Fragment {

    private static final int PERCENT = 100;

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
        User user = UserAdapter.getCurrentUser();
        if (view != null) {
            TextView name = (TextView) view.findViewById(R.id.user_info_name);
            name.setText(user.getName() + ' ' + user.getSurname());

            TextView username = (TextView) view.findViewById(R.id.user_info_username);
            username.setText(user.getUsername());

            TextView level = (TextView) view.findViewById(R.id.user_info_level);
            String userLevel = Integer.toString(user.getLevel());
            String showLevel = getString(R.string.level) + ' ' + userLevel;
            level.setText(showLevel);

            TextView xp = (TextView) view.findViewById(R.id.user_info_xp);
            String userExperience = Integer.toString(user.getExperience());
            xp.setText(userExperience + '/' + user.getExperienceMax());

            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.user_info_progress);
            int progress = (int)
                    (((double) user.getExperience() / user.getExperienceMax()) * PERCENT);
            progressBar.setProgress(progress);

            TextView coins = (TextView) view.findViewById(R.id.user_info_coins);
            String userCoins = Integer.toString(user.getCoins());
            coins.setText(userCoins);

            //CircularImageView profileImage =
            //        (CircularImageView) view.findViewById(R.id.user_info_image);
            //profileImage.setImageBitmap(img); // bitmap
            //profileImage.setImageIcon(img); // icon
        }
    }
}
