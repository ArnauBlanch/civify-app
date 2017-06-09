package com.civify.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.civify.adapter.AchievementsEventsAdapter;
import com.civify.adapter.LoginAdapter;
import com.civify.adapter.LoginAdapterImpl;
import com.civify.adapter.UserAdapter;
import com.civify.adapter.achievement.AchievementAdapter;
import com.civify.adapter.achievement.AchievementAdapterImpl;
import com.civify.adapter.award.AwardAdapter;
import com.civify.adapter.award.AwardAdapterImpl;
import com.civify.adapter.badge.BadgeAdapter;
import com.civify.adapter.badge.BadgeAdapterImpl;
import com.civify.adapter.event.EventAdapter;
import com.civify.adapter.event.EventAdapterImpl;
import com.civify.adapter.issue.IssueAdapter;

public class AdapterFactory {

    private static AdapterFactory sInstance;

    private UserAdapter mUserAdapter;
    private LoginAdapter mLoginAdapter;
    private IssueAdapter mIssueAdapter;
    private AwardAdapter mAwardAdapter;
    private AchievementAdapter mAchievementAdapter;
    private AchievementsEventsAdapter mAchievementsEventsAdapter;
    private EventAdapter mEventAdapter;
    private BadgeAdapter mBadgeAdapter;

    public static AdapterFactory getInstance() {
        if (sInstance == null) {
            sInstance = new AdapterFactory();
        }
        return sInstance;
    }

    public UserAdapter getUserAdapter() {
        if (mUserAdapter == null) {
            mUserAdapter = new UserAdapter();
        }
        return mUserAdapter;
    }

    public UserAdapter getUserAdapter(@NonNull Context context) {
        if (mUserAdapter == null) {
            mUserAdapter = new UserAdapter(getSharedPreferences(context));
        }
        return mUserAdapter;
    }

    public LoginAdapter getLoginAdapter(@NonNull Context context) {
        if (mLoginAdapter == null) {
            mLoginAdapter = new LoginAdapterImpl(getSharedPreferences(context));
        }
        return mLoginAdapter;
    }

    public IssueAdapter getIssueAdapter(@NonNull Context context) {
        if (mIssueAdapter == null) {
            mIssueAdapter = new IssueAdapter(getSharedPreferences(context));
        }
        return mIssueAdapter;
    }

    public AwardAdapter getAwardAdapter(@NonNull Context context) {
        if (mAwardAdapter == null) {
            mAwardAdapter = new AwardAdapterImpl(getSharedPreferences(context));
        }
        return mAwardAdapter;
    }

    public AchievementAdapter getAchievementAdapter(@NonNull Context context) {
        if (mAchievementAdapter == null) {
            mAchievementAdapter = new AchievementAdapterImpl(getSharedPreferences(context));
        }
        return mAchievementAdapter;
    }

    public AchievementsEventsAdapter getAchievementsEventsAdapter(@NonNull Context context) {
        if (mAchievementsEventsAdapter == null) {
            mAchievementsEventsAdapter = new AchievementsEventsAdapter(
                    getSharedPreferences(context));
        }
        return mAchievementsEventsAdapter;
    }

    public EventAdapter getEventAdapter(@NonNull Context context) {
        if (mEventAdapter == null) {
            mEventAdapter = new EventAdapterImpl(getSharedPreferences(context));
        }
        return mEventAdapter;
    }

    public BadgeAdapter getBadgeAdapter(@NonNull Context context) {
        if (mBadgeAdapter == null) {
            mBadgeAdapter = new BadgeAdapterImpl(getSharedPreferences(context));
        }
        return mBadgeAdapter;
    }

    public SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("USERPREFS", Context.MODE_PRIVATE);
    }

    public void resetAdapterFactory() {
        this.mUserAdapter = null;
        this.mAchievementsEventsAdapter = null;
        this.mIssueAdapter = null;
        this.mAchievementAdapter = null;
        this.mAwardAdapter = null;
        this.mBadgeAdapter = null;
        this.mEventAdapter = null;
        this.mLoginAdapter = null;
    }
}
