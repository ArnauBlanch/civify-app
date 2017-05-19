package com.civify.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @Expose
    @SerializedName("username")
    private String mUsername;

    @Expose
    @SerializedName("email")
    private String mEmail;

    @Expose
    @SerializedName("first_name")
    private String mName;

    @Expose
    @SerializedName("last_name")
    private String mSurname;

    @Expose(deserialize = false)
    @SerializedName("password")
    private String mPassword;

    @Expose(deserialize = false)
    @SerializedName("password_confirmation")
    private String mPasswordConfirmation;

    @Expose(serialize = false)
    @SerializedName("created_at")
    private String mCreatedAt;

    @Expose(serialize = false, deserialize = false)
    @SerializedName("updated_at")
    private String mUpdatedAt;

    @Expose(serialize = false)
    @SerializedName("user_auth_token")
    private String mUserAuthToken;

    @Expose(serialize = false)
    @SerializedName("lv")
    private int mLevel;

    @Expose(serialize = false)
    @SerializedName("xp")
    private int mExperience;

    @Expose(serialize = false)
    @SerializedName("xp_max")
    private int mExperienceMax;

    @Expose(serialize = false)
    @SerializedName("coins")
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
        mLevel = 1;
        mExperience = 0;
        mExperienceMax = 0;
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

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public int getLevel() {
        return mLevel;
    }

    public int getExperience() {
        return mExperience;
    }

    public int getExperienceMax() {
        return mExperienceMax;
    }

    public int getCoins() {
        return mCoins;
    }

    public void setCoins(int coins) {
        mCoins = coins;
    }

    public String getUserAuthToken() {
        return mUserAuthToken;
    }

    public void setUserAuthToken(String userAuthToken) {
        mUserAuthToken = userAuthToken;
    }

    public boolean willLevelUp(int experience) {
        return experience + getExperience() >= getExperienceMax();
    }
}
