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

    @SerializedName("password_digest")
    private String mPassworddigest;

    @SerializedName("created_at")
    private String mCreatedat;

    @SerializedName("updated_at")
    private String mUpdatedat;

    private int mLevel;
    private int mExperience;
    private int mCoins;

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

    public String getPassword_digest() {
        return mPassworddigest;
    }

    public void setPassword_digest(String passworddigest) {
        this.mPassworddigest = passworddigest;
    }

    public String getCreated_at() {
        return mCreatedat;
    }

    public void setCreated_at(String createdat) {
        this.mCreatedat = createdat;
    }

    public String getUpdatedat() {
        return mUpdatedat;
    }

    public void setUpdated_at(String updatedat) {
        this.mUpdatedat = updatedat;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        mLevel = level;
    }

    public int getExperience() {
        return mExperience;
    }

    public void setExperience(int experience) {
        mExperience = experience;
    }

    public int getCoins() {
        return mCoins;
    }

    public void setCoins(int coins) {
        mCoins = coins;
    }
}
