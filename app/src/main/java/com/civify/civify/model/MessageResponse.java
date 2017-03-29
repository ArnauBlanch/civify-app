package com.civify.civify.model;

import com.google.gson.annotations.SerializedName;

public class MessageResponse {
    @SerializedName("message")
    private String mMessage;

    public void setMessage(@SuppressWarnings("SameParameterValue") String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
