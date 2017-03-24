package com.civify.civify.utils;

import com.civify.civify.adapter.UserAdapter;

public class AdapterFactory {
    private static AdapterFactory mInstance;
    private UserAdapter mUserAdapter;

    public static AdapterFactory getInstance() {
        if (mInstance == null) {
            mInstance = new AdapterFactory();
        }
        return mInstance;
    }

    public UserAdapter getUserController() {
        if (mUserAdapter == null) {
            mUserAdapter = new UserAdapter();
        }
        return mUserAdapter;
    }
}
