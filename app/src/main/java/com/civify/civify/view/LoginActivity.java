package com.civify.civify.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.civify.civify.R;
import com.civify.civify.activity.DrawerActivity;
import com.civify.civify.controller.LoginAdapterImpl;
import com.civify.civify.controller.LoginError;
import com.civify.civify.controller.LoginFinishedCallback;
import com.civify.civify.model.User;


public class LoginActivity extends AppCompatActivity {
    private AppCompatButton mBlogin;
    private EditText mUser;
    private EditText mPassw;
    private LoginAdapterImpl mLoginadapterimpl;
    private TextView mPassforgot;
    private View.OnClickListener mListen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String tag = "loginLog";
            switch (v.getId()) {
                case R.id.bsignin:
                    String password = mPassw.getText().toString();
                    String user = mUser.getText().toString();
                    mLoginadapterimpl.login(user, password, new LoginFinishedCallback() {
                        @Override
                        public void onLoginSucceeded(User u) {
                            Log.d(tag, "login succeed");
                            startActivity(new Intent(getApplicationContext(),
                                    DrawerActivity.class));
                        }

                        @Override
                        public void onLoginFailed(LoginError e) {
                            Log.d(tag, e.getType().toString());
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    e.getType().toString(), Toast.LENGTH_SHORT);
                            toast.show();
                            //Mostrar l'error per pantalla corresponent
                        }
                    });
                    break;
                case R.id.login_forgot:
                    Log.d(tag, "change pass");
                    Toast toast = Toast.makeText(getApplicationContext(), "portará a la activity",
                            Toast.LENGTH_SHORT);
                    toast.show();
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
        SharedPreferences userpreferences = getSharedPreferences("USERPREFS", Context.MODE_PRIVATE);
        mLoginadapterimpl = new LoginAdapterImpl(userpreferences);
    }

}
