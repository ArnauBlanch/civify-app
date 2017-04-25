package com.civify.utils;

import android.content.SharedPreferences;

import com.civify.adapter.LoginAdapter;
import com.civify.adapter.LoginAdapterImpl;
import com.civify.adapter.UserAdapter;
import com.civify.adapter.issue.IssueAdapter;

public class AdapterFactory {
    private static AdapterFactory sInstance;
    private UserAdapter mUserAdapter;
    private LoginAdapter mLoginAdapter;
    private IssueAdapter mIssueAdapter;

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

    public LoginAdapter getLoginAdapter(SharedPreferences sharedPreferences) {
        if (mLoginAdapter == null) {
            mLoginAdapter = new LoginAdapterImpl(sharedPreferences);
        }
        return mLoginAdapter;
    }

    public IssueAdapter getIssueAdapter(SharedPreferences sharedPreferences) {
        if (mIssueAdapter == null) {
            mIssueAdapter = new IssueAdapter(sharedPreferences);
        }
        return mIssueAdapter;
    }
}
