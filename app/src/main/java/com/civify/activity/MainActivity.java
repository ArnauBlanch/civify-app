package com.civify.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.civify.R;
import com.civify.activity.registration.RegistrationActivity;
import com.civify.adapter.LoginAdapter;
import com.civify.adapter.LoginError;
import com.civify.adapter.LoginFinishedCallback;
import com.civify.model.User;
import com.civify.utils.AdapterFactory;

public class MainActivity extends BaseActivity {

    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDialog = createDialog();
        putButtonDialog();

        initButtons();
    }

    private void initButtons() {
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
                loginListener();
            }
        });
    }

    private void loginListener() {
        LoginAdapter loginAdapter = AdapterFactory.getInstance().getLoginAdapter(this);
        loginAdapter.isLogged(new LoginFinishedCallback() {
            @Override
            public void onLoginSucceeded(User u) {
                Intent intent = new Intent(getApplicationContext(), DrawerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onLoginFailed(LoginError t) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void putButtonDialog() {
        mDialog.setButton(getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.not_implemented_yet))
                .setTitle(getString(R.string.error));

        return builder.create();
    }

}
