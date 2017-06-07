package com.civify.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.civify.R;
import com.civify.activity.forgotpassword.ForgotActivity;
import com.civify.adapter.LoginAdapter;
import com.civify.adapter.LoginError;
import com.civify.adapter.LoginFinishedCallback;
import com.civify.model.User;
import com.civify.utils.AdapterFactory;

public class LoginActivity extends BaseActivity {
    private AppCompatButton mBlogin;
    private EditText mUser;
    private EditText mPassw;
    private LoginAdapter mLoginAdapter;
    private TextView mPassforgot;
    private View.OnClickListener mListen = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final String tag = "loginLog";
            switch (v.getId()) {
                case R.id.bsignin:
                    String password = mPassw.getText().toString();
                    String user = mUser.getText().toString();
                    mLoginAdapter.login(user, password, new LoginFinishedCallback() {
                        @Override
                        public void onLoginSucceeded(User u) {
                            Log.d(tag, "login succeed");
                            Intent intent = new Intent(getApplicationContext(),
                                    DrawerActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                        @Override
                        public void onLoginFailed(LoginError e) {
                            Snackbar.make(v, e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    });
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mBlogin = (AppCompatButton) findViewById(R.id.bsignin);
        mBlogin.setOnClickListener(mListen);
        mUser = (EditText) findViewById(R.id.login_email_input);
        mPassw = (EditText) findViewById(R.id.login_password_input);
        mPassforgot = (TextView) findViewById(R.id.login_forgot);
        mPassforgot.setOnClickListener(mListen);
        mLoginAdapter = AdapterFactory.getInstance().getLoginAdapter(this);
    }

}
