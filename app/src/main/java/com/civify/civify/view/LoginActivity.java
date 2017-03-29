package com.civify.civify.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.civify.civify.R;



public class LoginActivity extends AppCompatActivity {
    Button blogin;
    EditText usr;
    EditText pasw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        blogin.findViewById(R.id.bsignin);
        blogin.setOnClickListener(lis);
        usr.findViewById(R.id.login_email_input);
        pasw.findViewById(R.id.login_password_input);
    }
    View.OnClickListener lis = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Log.e("error", "Login error");
        }
    };
}