package com.civify.activity.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.model.achievement.Achievement;
import com.civify.service.achievement.ListAchievementsSimpleCallback;
import com.civify.utils.AdapterFactory;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AchievementsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AchievementsFragment extends BasicFragment {

    public static final String API_TEST = "api_test";

    public AchievementsFragment() {
    }

    public static AchievementsFragment newInstance() {
        AchievementsFragment fragment = new AchievementsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.ACHIEVEMENTS_ID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        testApi();
        return inflater.inflate(R.layout.fragment_achievements, container, false);
    }

    private void testApi() {
        Log.d(API_TEST, "Init testApi");
        AdapterFactory.getInstance().getAchievementAdapter(getContext())
                .getAchievements(new ListAchievementsSimpleCallback() {
                    @Override
                    public void onSuccess(List<Achievement> achievements) {
                        Log.d(API_TEST, "Success");
                        Log.d(API_TEST, achievements.toString());
                    }

                    @Override
                    public void onFailure() {
                        Log.d(API_TEST, "Failure");
                    }
                    });
        Log.d(API_TEST, "End testApi");
    }
}
