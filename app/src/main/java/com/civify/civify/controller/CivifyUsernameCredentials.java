package com.civify.civify.controller;

import com.google.gson.annotations.SerializedName;

class CivifyUsernameCredentials {

    @SerializedName("username")
    private String mUsername;
    @SerializedName("password")
    private String mPassword;

    public CivifyUsernameCredentials(String usernameCredential, String hashedPassword) {
        mUsername = usernameCredential;
        mPassword = hashedPassword;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }
}
