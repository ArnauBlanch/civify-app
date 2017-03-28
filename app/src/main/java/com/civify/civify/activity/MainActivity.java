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
    private static final String MSG_NOT_IMPLEMENTED = "Feature not implemented yet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonRegister = (Button) findViewById(R.id.registerButton);
        buttonRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });
        Button buttonLogin = (Button) findViewById(R.id.signInButton);
        buttonLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        MSG_NOT_IMPLEMENTED, Toast.LENGTH_LONG).show();
            }
        });
        Button buttonGoogle = (Button) findViewById(R.id.buttonGoogle);
        buttonGoogle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        MSG_NOT_IMPLEMENTED, Toast.LENGTH_LONG).show();
            }
        });
        Button buttonTwitter = (Button) findViewById(R.id.buttonTwitter);
        buttonTwitter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        MSG_NOT_IMPLEMENTED, Toast.LENGTH_LONG).show();
            }
        });
    }
}
