package com.civify.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;

import com.civify.activity.introduction.IntroductionActivity;
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
    private static final String EN = "en";

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
                        Intent intent = new Intent(SplashActivity.this, IntroductionActivity.class);
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

        if (sprefs.contains(LANGUAGE)) {
            setLang(new Locale(sprefs.getString(LANGUAGE, EN)));
        } else {
            String devLoc = Locale.getDefault().getLanguage();
            setLang(new Locale(devLoc.equals(ES) || devLoc.equals(CA) ? devLoc : EN));
        }
    }

    private void setLang(Locale locale) {
        Locale.setDefault(locale);
        Configuration config = new Configuration(getResources().getConfiguration());
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
