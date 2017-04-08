package com.civify.civify.utils;

import android.content.SharedPreferences;

import com.civify.civify.adapter.IssueAdapter;

public class AdapterFactory {

    private static AdapterFactory mAdapterFactory;

    public static AdapterFactory getInstance() {
        if (mAdapterFactory == null) {
            mAdapterFactory = new AdapterFactory();
        }
        return mAdapterFactory;
    }

    public IssueAdapter getIssueAdapter(SharedPreferences sharedPreferences) {
        return new IssueAdapter(sharedPreferences);
    }
}
