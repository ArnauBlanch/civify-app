package com.civify.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.civify.R;
import com.civify.activity.forgotpassword.ForgotActivity;
import com.civify.adapter.LoginAdapter;
import com.civify.adapter.LoginError;
import com.civify.adapter.LoginFinishedCallback;
import com.civify.adapter.UserAdapter;
import com.civify.model.User;
import com.civify.utils.AdapterFactory;

public class LoginActivity extends BaseActivity {
    private static final int FIRST = 0;
    private static final int SECOND = 1;
    private AppCompatButton mBlogin;
    private EditText mUser;
    private EditText mPassw;
    private LoginAdapter mLoginAdapter;
    private TextView mPassforgot;
    private ImageView mIconFirstCredential;
    private TextView mMessageFirstCredential;
    private ImageView mIconPassword;
    private TextView mMessagePassword;
    private AlertDialog mLoginAlertDialog;
    private View.OnClickListener mListen = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.bsignin:
                    String password = mPassw.getText().toString();
                    String user = mUser.getText().toString();
                    callLogin(user, password);
                    break;
                case R.id.login_forgot:
                    Intent intent = new Intent(getApplicationContext(), ForgotActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

    private void callLogin(String user, String password) {
        if (validCredentials(user, password)) {
            mLoginAdapter.login(user, password, new LoginFinishedCallback() {
                @Override
                public void onLoginSucceeded(User u) {
                    Intent intent = new Intent(getApplicationContext(), DrawerActivity.class);
                    int flags = Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK;
                    intent.setFlags(flags);
                    startActivity(intent);
                }

                @Override
                public void onLoginFailed(LoginError e) {
                    mLoginAlertDialog.setMessage(loginErrorMessage(e));
                    mLoginAlertDialog.show();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initDialogs();
        mBlogin = (AppCompatButton) findViewById(R.id.bsignin);
        mBlogin.setOnClickListener(mListen);
        mUser = (EditText) findViewById(R.id.login_email_input);
        mPassw = (EditText) findViewById(R.id.login_password_input);
        mPassforgot = (TextView) findViewById(R.id.login_forgot);
        mIconFirstCredential = (ImageView) findViewById(R.id.email_errors_icon);
        mMessageFirstCredential = (TextView) findViewById(R.id.email_errors_text);
        mIconPassword = (ImageView) findViewById(R.id.password_errors_icon);
        mMessagePassword = (TextView) findViewById(R.id.password_errors_text);
        mPassforgot.setOnClickListener(mListen);
        mLoginAdapter = AdapterFactory.getInstance().getLoginAdapter(this);

    }

    private void initDialogs() {
        mLoginAlertDialog = new AlertDialog.Builder(this).create();
        mLoginAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok),
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mLoginAlertDialog.dismiss();
                    }
                });
    }

    private String loginErrorMessage(LoginError e) {
        switch (e.getType()) {
            case USER_NOT_EXISTS:
                return getString(R.string.user_not_exists);
            case INVALID_CREDENTIALS:
                return getString(R.string.incorrect_credentials);
            default:
                return getString(R.string.general_error);

        }
    }

    private boolean validCredentials(String user, String password) {
        clearErrors();
        if (user.isEmpty() || (user.contains("@") && !UserAdapter.VALID_EMAIL.matcher(user)
                .matches())) {
            setIconAndMessage(FIRST, R.drawable.ic_cancel, R.string.introduce_valid_ue, R
                    .color.red);
            return false;
        }
        if (password.isEmpty()) {
            setIconAndMessage(SECOND, R.drawable.ic_cancel, R.string.introduce_valid_password, R
                    .color.red);
            return false;
        }
        return true;
    }

    private void clearErrors() {
        mIconFirstCredential.setImageResource(android.R.color.transparent);
        mMessageFirstCredential.setText("");
        mIconPassword.setImageResource(android.R.color.transparent);
        mMessagePassword.setText("");
    }

    private void setIconAndMessage(int credential, int iconDrawable, int message, int
            messageColor) {
        switch (credential) {
            case FIRST:
                mIconFirstCredential.setImageResource(iconDrawable);
                mMessageFirstCredential.setText(getString(message));
                mMessageFirstCredential.setTextColor(getResources().getColor(messageColor));
                break;
            default:
                mIconPassword.setImageResource(iconDrawable);
                mMessagePassword.setText(getString(message));
                mMessagePassword.setTextColor(getResources().getColor(messageColor));
                break;
        }

    }
}
