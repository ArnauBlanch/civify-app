package com.civify.utils;

import android.content.SharedPreferences;

import com.civify.adapter.LoginAdapter;
import com.civify.adapter.LoginAdapterImpl;
import com.civify.adapter.RegisterAdapter;

public class AdapterFactory {
    private static AdapterFactory sInstance;
    private RegisterAdapter mRegisterAdapter;
    private LoginAdapter mLoginAdapter;

    public static AdapterFactory getInstance() {
        if (sInstance == null) {
            sInstance = new AdapterFactory();
        }
        return sInstance;
    }

    public RegisterAdapter getRegisterAdapter() {
        if (mRegisterAdapter == null) {
            mRegisterAdapter = new RegisterAdapter();
        }
        return mRegisterAdapter;
    }

    public LoginAdapter getLoginAdapter(SharedPreferences sharedPreferences) {
        if (mLoginAdapter == null) {
            mLoginAdapter = new LoginAdapterImpl(sharedPreferences);
        }
        return mLoginAdapter;
    }
}
