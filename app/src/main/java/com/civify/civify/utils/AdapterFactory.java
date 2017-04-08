package com.civify.civify.utils;

import android.content.SharedPreferences;

import com.civify.civify.adapter.IssueAdapter;

public class AdapterFactory {

    public AdapterFactory getInstance() {
        return this;
    }

    public IssueAdapter getIssueAdapter(SharedPreferences sharedPreferences) {
        return new IssueAdapter(sharedPreferences);
    }
}
