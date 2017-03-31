package com.civify.civify.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.civify.civify.R;
import com.civify.civify.activity.registration.RegistrationActivity;

public class MainActivity extends BaseActivity {
    private static final String MSG_CLOSE = "Close";
    private static final String MSG_ERROR = "Error";
    private static final String MSG_NOT_IMPLEMENTED = "Feature not implemented yet";

    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDialog = createDialog();
        putButtonDialog();

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
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        Button buttonGoogle = (Button) findViewById(R.id.buttonGoogle);
        buttonGoogle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
            }
        });
        Button buttonTwitter = (Button) findViewById(R.id.buttonTwitter);
        buttonTwitter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
            }
        });

    }

    private void putButtonDialog() {
        mDialog.setButton(MSG_CLOSE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(MSG_NOT_IMPLEMENTED)
                .setTitle(MSG_ERROR);

        return builder.create();
    }

}
