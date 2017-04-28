package com.civify.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

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

    private SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("USERPREFS", Context.MODE_PRIVATE);
    }
}
