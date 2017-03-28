package com.civify.civify.utils;

import com.civify.civify.adapter.UserAdapter;

public class AdapterFactory {
    private static AdapterFactory sInstance;
    private UserAdapter mUserAdapter;

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
}
