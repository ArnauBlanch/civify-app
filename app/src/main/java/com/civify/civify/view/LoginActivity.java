package com.civify.civify.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.civify.civify.R;
import com.civify.civify.controller.LoginAdapter;
import com.civify.civify.controller.LoginAdapterImpl;
import com.civify.civify.controller.LoginError;
import com.civify.civify.controller.LoginFinishedCallback;
import com.civify.civify.model.User;


public class LoginActivity extends AppCompatActivity {
    Button blogin;
    EditText usr;
    EditText pasw;
    LoginAdapterImpl loginadapterimpl;
    TextView passforgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        blogin.findViewById(R.id.bsignin);
        blogin.setOnClickListener(lis);
        usr.findViewById(R.id.login_email_input);
        pasw.findViewById(R.id.login_password_input);
        passforgot.findViewById(R.id.login_forgot);
        passforgot.setOnClickListener(lis);
        SharedPreferences userpreferences = getSharedPreferences("USERPREFS", Context.MODE_PRIVATE);
        loginadapterimpl = new LoginAdapterImpl(userpreferences);
    }
    View.OnClickListener lis = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bsignin:
                String user = usr.getText().toString();
                String password = pasw.getText().toString();
                loginadapterimpl.login(user, password, new LoginFinishedCallback() {
                    @Override
                    public void onLoginSucceeded(User u) {
                        Log.d("login", "login succeed");
                        //anar a la pantalla de mapa
                    }

                    @Override
                    public void onLoginFailed(LoginError e) {
                        Log.d("login", e.getType().toString());
                        Toast toast = Toast.makeText(getApplicationContext(), e.getType().toString(), Toast.LENGTH_SHORT);
                        toast.show();
                        //Mostrar l'error per pantalla corresponent
                    }
                });
                    break;
                case R.id.login_forgot:
                    Log.d("login", "change pass");
                    break;
            }
        }
    };
}