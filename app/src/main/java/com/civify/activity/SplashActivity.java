package com.civify.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.civify.R;
import com.civify.adapter.LoginAdapter;
import com.civify.adapter.LoginError;
import com.civify.adapter.LoginFinishedCallback;
import com.civify.model.User;
import com.civify.model.map.CivifyMap;
import com.civify.utils.AdapterFactory;
import com.google.android.gms.maps.GoogleMap;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends BaseActivity {

    private static final long SPLASH_SCREEN_DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                LoginAdapter loginAdapter =
                        AdapterFactory.getInstance().getLoginAdapter(getApplicationContext());
                loginAdapter.isLogged(new LoginFinishedCallback() {
                    @Override
                    public void onLoginSucceeded(User u) {
                        Intent intent = new Intent(SplashActivity.this, DrawerActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onLoginFailed(LoginError t) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }
}
