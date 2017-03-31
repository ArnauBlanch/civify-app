package com.civify.civify.model;

import com.google.gson.annotations.SerializedName;

public class CivifyEmailCredentials {

    @SerializedName("email")
    private String mEmail;
    @SerializedName("password")
    private String mPassword;

    public CivifyEmailCredentials(String emailCredential, String hashedPassword) {
        mEmail = emailCredential;
        mPassword = hashedPassword;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }
}
