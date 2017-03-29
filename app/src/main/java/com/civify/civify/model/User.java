package com.civify.civify.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("ElementOnlyUsedFromTestCode")
public class User {

    @SerializedName("username")
    private String mUsername;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("first_name")
    private String mName;

    @SerializedName("last_name")
    private String mSurname;

    @SerializedName("password")
    private String mPassword;

    @SerializedName("password_confirmation")
    private String mPasswordConfirmation;

    public User(@NonNull String username, @NonNull String name,
                @NonNull String surname, @NonNull String email,
                @NonNull String password, @NonNull String passwordConfirmation) {
        mUsername = username;
        mName = name;
        mSurname = surname;
        mEmail = email;
        mPassword = password;
        mPasswordConfirmation = passwordConfirmation;
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

    public String getPasswordConfirmation() {
        return mPasswordConfirmation;
    }

    public void setPasswordConfirmation(@NonNull String passwordConfirmation) {
        mPasswordConfirmation = passwordConfirmation;
    }
}
