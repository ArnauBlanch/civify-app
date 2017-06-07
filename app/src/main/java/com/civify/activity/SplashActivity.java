package com.civify.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;

import com.civify.adapter.LoginAdapter;
import com.civify.adapter.LoginError;
import com.civify.adapter.LoginFinishedCallback;
import com.civify.model.User;
import com.civify.utils.AdapterFactory;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends BaseActivity {

    private static final long SPLASH_SCREEN_DELAY = 1000;
    private static final String SHAREDPREFSLOCALE = "LocalePrefs";
    private static final String LANGUAGE = "lang";
    private static final String CA = "ca";
    private static final String ES = "es";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setupLang();
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

    private void setupLang() {
        SharedPreferences sprefs = getSharedPreferences(SHAREDPREFSLOCALE, Context.MODE_PRIVATE);
        Configuration config;
        config = new Configuration(getResources().getConfiguration());
        if (sprefs.contains(LANGUAGE)) {
            config.locale = new Locale(sprefs.getString(LANGUAGE, "en"));
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        } else {
            String devLoc = Locale.getDefault().getLanguage();
            switch (devLoc) {
                case ES:
                    config.locale = new Locale(ES);
                    getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                    break;
                case CA:
                    config.locale = new Locale(CA);
                    getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                    break;
                default:
                    break;
            }
        }
    }
}
