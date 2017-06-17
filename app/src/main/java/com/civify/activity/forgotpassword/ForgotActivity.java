package com.civify.activity.forgotpassword;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.civify.R;
import com.civify.activity.BaseActivity;
import com.civify.adapter.UserAdapter;
import com.civify.adapter.ValidationCallback;
import com.civify.utils.AdapterFactory;
import com.civify.utils.ConfirmDialog;

public class ForgotActivity extends BaseActivity {

    private UserAdapter mUserAdapter;
    private EditText mEmailEditText;
    private TextView mMessageView;
    private ImageView mIcon;
    private Boolean mSubmitClickable;
    private Drawable mOkDrawable;
    private DialogInterface.OnClickListener mDialogOnClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotten_password_layout);
        mSubmitClickable = false;
        mUserAdapter = AdapterFactory.getInstance().getUserAdapter(getApplicationContext());
        mEmailEditText = (EditText) findViewById(R.id.email_input);
        mMessageView = (TextView) findViewById(R.id.email_exists_text);
        mIcon = (ImageView) findViewById(R.id.email_exists_icon);
        mOkDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_checked);
        init();
        setTextWatchers();
    }

    private void initDialogOnClick() {
        mDialogOnClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,
                    int which) {
                dialog.dismiss();
                finish();
            }
        };
    }

    private void init() {
        initDialogOnClick();
        Button submitButton = (Button) findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSubmitClickable) {
                    final ProgressDialog dialog = new ProgressDialog(ForgotActivity.this);
                    dialog.setMessage(getString(R.string.sending_forgot_mail));
                    dialog.show();
                    mUserAdapter.sentResetPasswrdEmail(mEmailEditText.getText().toString(),
                            new ValidationCallback() {
                                @Override
                                public void onValidationResponse(int response) {
                                    switch (response) {
                                        case UserAdapter.EMAIL_SENT_CODE:
                                            dialog.dismiss();
                                            ConfirmDialog.show(ForgotActivity.this,
                                                    null, getString(R.string.email_sent),
                                                    mDialogOnClick, null);
                                            break;
                                        default:
                                            dialog.setMessage(getString(R.string.email_not_sent));
                                    }
                                }
                            });
                } else {
                    setIconAndMessage(R.drawable.ic_cancel, R.string.enter_email, R.color.red);
                }
            }
        });
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
                                mSubmitClickable = false;
                                setIconAndMessage(
                                        R.drawable.ic_cancel, R.string.email_not_exists,
                                        R.color.red);
                                break;
                            case UserAdapter.INVALID:
                                mSubmitClickable = false;
                                setIconAndMessage(
                                        R.drawable.ic_cancel, R.string.invalid_email, R.color.red);
                                break;
                            default:
                                mSubmitClickable = true;
                                setIconAndMessage(
                                        R.drawable.ic_checked, R.string.email_exists_correct,
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
