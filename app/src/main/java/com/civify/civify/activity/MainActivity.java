package com.civify.civify.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.civify.civify.R;
import com.civify.civify.activity.registration.RegistrationActivity;

public class MainActivity extends BaseActivity {
    private static final String LOGIN = "login";
    private static final String GOOGLE = "google";
    private static final String TWITTER = "twitter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonRegister = (Button) findViewById(R.id.registerButton);
        buttonRegister.setOnClickListener(mListener);
        Button buttonLogin = (Button) findViewById(R.id.signInButton);
        buttonLogin.setOnClickListener(mListener);
        Button buttonGoogle = (Button) findViewById(R.id.buttonGoogle);
        buttonGoogle.setOnClickListener(mListener);
        Button buttonTwitter = (Button) findViewById(R.id.buttonTwitter);
        buttonTwitter.setOnClickListener(mListener);
    }

    private OnClickListener mListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.registerButton:
                    Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                    startActivity(intent);
                    break;
                case R.id.signInButton:
                    Toast.makeText(getApplicationContext(), LOGIN, Toast.LENGTH_LONG).show();
                    break;
                case R.id.buttonGoogle:
                    Toast.makeText(getApplicationContext(), GOOGLE, Toast.LENGTH_LONG).show();
                    break;
                case R.id.buttonTwitter:
                    Toast.makeText(getApplicationContext(), TWITTER, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };


}
