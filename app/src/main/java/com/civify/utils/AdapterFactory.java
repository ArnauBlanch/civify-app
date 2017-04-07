package com.civify.utils;

import android.content.SharedPreferences;

import com.civify.adapter.LoginAdapter;
import com.civify.adapter.LoginAdapterImpl;
import com.civify.adapter.UserAdapter;

public class AdapterFactory {
    private static AdapterFactory sInstance;
    private UserAdapter mUserAdapter;
    private LoginAdapter mLoginAdapter;

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
}
