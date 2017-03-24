package com.civify.civify.activity.registration;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.civify.civify.R;
import com.civify.civify.adapter.UserAdapter;
import com.civify.civify.utils.AdapterFactory;

@SuppressWarnings("SameParameterValue")
class PasswordTextWatcher implements TextWatcher {
    private final ImageView mIcon;
    private final TextView mMessageView;
    private final Context mContext;
    private final UserAdapter mUserAdapter;

    PasswordTextWatcher(Context context, View view, int iconResource, int messageResource) {
        super();
        this.mContext = context;
        this.mIcon = (ImageView) view.findViewById(iconResource);
        this.mMessageView = (TextView) view.findViewById(messageResource);
        this.mUserAdapter = AdapterFactory.getInstance().getUserController();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mUserAdapter.checkValidPassword(s.toString())) {
            setIconAndMessage(R.drawable.ic_checked, R.string.valid_password, R.color.green);
        } else {
            setIconAndMessage(R.drawable.ic_cancel, R.string.invalid_password, R.color.red);
        }
    }

    private void setIconAndMessage(int iconDrawable, int message, int messageColor) {
        mIcon.setImageResource(iconDrawable);
        mMessageView.setText(mContext.getString(message));
        //noinspection deprecation
        mMessageView.setTextColor(mContext.getResources().getColor(messageColor));
    }
}
