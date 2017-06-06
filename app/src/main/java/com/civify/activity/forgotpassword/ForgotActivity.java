package com.civify.activity.forgotpassword;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.civify.R;
import com.civify.activity.BaseActivity;
import com.civify.adapter.UserAdapter;
import com.civify.adapter.ValidationCallback;
import com.civify.utils.AdapterFactory;

public class ForgotActivity extends BaseActivity {

    private UserAdapter mUserAdapter;
    private EditText mEmailEditText;
    private TextView mMessageView;
    private ImageView mIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotten_password_layout);
        init();
        setTextWatchers();
    }

    private void init() {
        mUserAdapter = AdapterFactory.getInstance().getUserAdapter();
        mEmailEditText = (EditText) findViewById(R.id.email_input);
        mMessageView = (TextView) findViewById(R.id.email_exists_text);
        mIcon = (ImageView) findViewById(R.id.email_exists_icon);
    }

    private void setTextWatchers() {
        mEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mUserAdapter.checkValidUnusedEmail(s.toString(), new ValidationCallback() {
                    @Override
                    public void onValidationResponse(int response) {
                        switch (response) {
                            case UserAdapter.VALID_UNUSED:
                                setIconAndMessage(
                                        R.drawable.ic_cancel,
                                        R.string.email_not_exists,
                                        R.color.red);
                                break;
                            case UserAdapter.INVALID:
                                setIconAndMessage(
                                        R.drawable.ic_cancel,
                                        R.string.invalid_email,
                                        R.color.red);
                                break;
                            default:
                                setIconAndMessage(
                                        R.drawable.ic_checked,
                                        R.string.email_exists_correct,
                                        R.color.green);
                        }
                    }
                });
            }
        });
    }

    private void setIconAndMessage(int iconDrawable, int message, int messageColor) {
        mIcon.setImageResource(iconDrawable);
        mMessageView.setText(getString(message));
        mMessageView.setTextColor(getResources().getColor(messageColor));
    }
}
