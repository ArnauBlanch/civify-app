package com.civify.controller;

public class LoginError {

    private ErrorType mType;
    private String mMessage;

    public LoginError(ErrorType type, String message) {
        mType = type;
        mMessage = message;
    }

    public ErrorType getType() {
        return mType;
    }

    public void setType(ErrorType type) {
        mType = type;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public enum ErrorType {
        USER_NOT_EXISTS,
        INVALID_CREDENTIALS,
        NOT_LOGGED_IN
    }

}
