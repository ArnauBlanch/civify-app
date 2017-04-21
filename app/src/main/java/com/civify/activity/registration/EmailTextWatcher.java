package com.civify.activity.registration;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.civify.R;
import com.civify.adapter.RegisterAdapter;
import com.civify.adapter.ValidationCallback;
import com.civify.utils.AdapterFactory;

class EmailTextWatcher implements TextWatcher {
    private final ImageView mIcon;
    private final TextView mMessageView;
    private final Context mContext;
    private final RegisterAdapter mRegisterAdapter;

    EmailTextWatcher(View view, int iconResource, int messageResource) {
        this(AdapterFactory.getInstance().getRegisterAdapter(),
                view, iconResource, messageResource);
    }

    EmailTextWatcher(RegisterAdapter registerAdapter, View view, int iconResource, int messageResource) {
        this.mRegisterAdapter = registerAdapter;
        this.mContext = view.getContext();
        this.mIcon = (ImageView) view.findViewById(iconResource);
        this.mMessageView = (TextView) view.findViewById(messageResource);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mRegisterAdapter.checkValidUnusedEmail(s.toString(), new ValidationCallback() {
            @Override
            public void onValidationResponse(int response) {
                switch (response) {
                    case RegisterAdapter.VALID_UNUSED:
                        setIconAndMessage(
                                R.drawable.ic_checked, R.string.valid_unused_email, R.color.green);
                        break;
                    case RegisterAdapter.INVALID:
                        setIconAndMessage(
                                R.drawable.ic_cancel, R.string.invalid_email, R.color.red);
                        break;
                    default:
                        setIconAndMessage(
                                R.drawable.ic_cancel, R.string.used_email, R.color.red);
                }
            }
        });
    }

    private void setIconAndMessage(int iconDrawable, int message, int messageColor) {
        mIcon.setImageResource(iconDrawable);
        mMessageView.setText(mContext.getString(message));
        //noinspection deprecation
        mMessageView.setTextColor(mContext.getResources().getColor(messageColor));
    }
}
