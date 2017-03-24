package com.civify.civify.model;

import android.support.annotation.NonNull;

public class User {

    private String mUsername;
    private String mName;
    private String mSurname;
    private String mEmail;
    private String mPassword;

    public User(@NonNull String username, @NonNull String name,
                @NonNull String surname, @NonNull String email,
                @NonNull String password) {
        mUsername = username;
        mName = name;
        mSurname = surname;
        mEmail = email;
        mPassword = password;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(@NonNull String username) {
        mUsername = username;
    }

    public String getName() {
        return mName;
    }

    public void setName(@NonNull String name) {
        mName = name;
    }

    public String getSurname() {
        return mSurname;
    }

    public void setSurname(@NonNull String surname) {
        mSurname = surname;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(@NonNull String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(@NonNull String password) {
        mPassword = password;
    }
}
