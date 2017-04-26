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
import com.civify.model.User;
import com.mikhaellopez.circularimageview.CircularImageView;

public class ProfileInfoFragment extends Fragment {

    private User mCurrentUser;

    public ProfileInfoFragment() {
        // Required empty public constructor
    }

    public static ProfileInfoFragment newInstance(String param1, String param2) {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentUser = UserAdapter.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_info, container, false);
        setUserInfo(mCurrentUser, view);
        return view;
    }

    private void setUserInfo(User user, View view) {
        // progressBar.setProgress(user.getLevel()/utils.calcMaxLevel(userLevel) * 100);
        if (view != null) {
            TextView name = (TextView) view.findViewById(R.id.user_info_name);
            name.setText(user.getName() + " " + user.getSurname());

            TextView username = (TextView) view.findViewById(R.id.user_info_username);
            username.setText(user.getUsername());

            TextView level = (TextView) view.findViewById(R.id.user_info_level);
            String userLevel = Integer.toString(user.getLevel());
            String showLevel = getString(R.string.level) + ' ' + userLevel;
            level.setText(showLevel);

            TextView xp = (TextView) view.findViewById(R.id.user_info_xp);
            String userExperience = Integer.toString(user.getExperience());
            //xp.setText(userExperience + '/' + utils.calcMaxXp(userLevel));

            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.user_info_progress);
            // int progress = user.getExperience()/Integer.getString(utils.calcMaxXp(userLevel))
            // *100;
            // progressBar.setProgress(progress, true);

            TextView coins = (TextView) view.findViewById(R.id.user_info_coins);
            String userCoins = Integer.toString(user.getCoins());
            coins.setText(userCoins);

            CircularImageView profileImage =
                    (CircularImageView) view.findViewById(R.id.user_info_image);
            //profileImage.setImageBitmap(img); // bitmap
            //profileImage.setImageIcon(img); // icon
        }
    }
}
