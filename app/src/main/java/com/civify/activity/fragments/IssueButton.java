package com.civify.activity.fragments;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.civify.R;

enum IssueButton {

    CONFIRM(R.drawable.green_bg_button, R.string.confirm, R.string.confirm_message,
            R.string.confirm_error_message, R.color.white),

    UNCONFIRM(R.drawable.green_border_button, R.string.unconfirm, R.string.unconfirm_message,
            R.string.unconfirm_error_message, R.color.green),

    REPORT(R.drawable.red_bg_button, R.string.report, R.string.report_message,
            R.string.report_error_message, R.color.white),

    UNREPORT(R.drawable.red_border_button, R.string.unreport, R.string.unreport_message,
            R.string.unreport_error_message, R.color.red);

    @DrawableRes
    private final int mDrawable;
    @StringRes
    private final int mText;
    @StringRes
    private final int mMessage;
    @StringRes
    private final int mErrorMessage;
    @ColorRes
    private final int mTextColor;

    IssueButton(@DrawableRes int drawable, @StringRes int text, @StringRes int message,
            @StringRes int errorMessage, @ColorRes int textColor) {
        mDrawable = drawable;
        mText = text;
        mMessage = message;
        mErrorMessage = errorMessage;
        mTextColor = textColor;
    }

    @DrawableRes
    public int getDrawable() {
        return mDrawable;
    }

    @StringRes
    public int getText() {
        return mText;
    }

    @StringRes
    public int getMessage() {
        return mMessage;
    }

    @StringRes
    public int getErrorMessage() {
        return mErrorMessage;
    }

    @ColorRes
    public int getTextColor() {
        return mTextColor;
    }
}
