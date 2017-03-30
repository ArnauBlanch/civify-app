package com.civify.civify.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username")
    private String mUsername;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("password_digest")
    private String mPassworddigest;
    @SerializedName("created_at")
    private String mCreatedat;
    @SerializedName("updated_at")
    private String mUpdatedat;

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

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
}
