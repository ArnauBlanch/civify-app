package com.civify.civify.activity.registration;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.civify.civify.R;

@SuppressWarnings({ "SameParameterValue", "ElementOnlyUsedFromTestCode" })
class PasswordMatchTextWatcher implements TextWatcher {
    private final ImageView mIcon;
    private final TextView mMessageView;
    private final Context mContext;
    private final EditText mPasswordInput;

    PasswordMatchTextWatcher(View view, int iconResource,
            int messageResource, int passwordInput) {
        this.mContext = view.getContext();
        this.mIcon = (ImageView) view.findViewById(iconResource);
        this.mMessageView = (TextView) view.findViewById(messageResource);
        this.mPasswordInput = (EditText) view.findViewById(passwordInput);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String password = mPasswordInput.getText().toString();
        if (s.toString().equals(password)) {
            setIconAndMessage(R.drawable.ic_checked, R.string.matching_passwords, R.color.green);
        } else {
            setIconAndMessage(R.drawable.ic_cancel, R.string.not_matching_passwords, R.color.red);
        }
    }

    private void setIconAndMessage(int iconDrawable, int message, int messageColor) {
        mIcon.setImageResource(iconDrawable);
        mMessageView.setText(mContext.getString(message));
        //noinspection deprecation
        mMessageView.setTextColor(mContext.getResources().getColor(messageColor));
    }
}
