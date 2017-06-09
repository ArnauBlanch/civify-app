package com.civify.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;

import com.civify.R;
import com.civify.activity.introduction.IntroductionActivity;
import com.civify.adapter.LoginAdapter;
import com.civify.adapter.LoginError;
import com.civify.adapter.LoginError.ErrorType;
import com.civify.adapter.LoginFinishedCallback;
import com.civify.model.User;
import com.civify.utils.AdapterFactory;
import com.civify.utils.ConfirmDialog;
import com.civify.utils.NetworkController;
import com.civify.utils.Timeout;

import java.util.Locale;

public class SplashActivity extends BaseActivity {

    private static final long SPLASH_SCREEN_DELAY = 1000;
    private static final String SHAREDPREFSLOCALE = "LocalePrefs";
    private static final String LANGUAGE = "lang";
    private static final String CA = "ca";
    private static final String ES = "es";
    private static final String EN = "en";
    private static final String INTRO = "intro";
    private static long sStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sStart = System.currentTimeMillis();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setupLang();

        checkNetwork();
    }

    private void checkNetwork() {
        if (checkedNetwork()) checkLogin();
    }

    private void checkLogin() {
        LoginAdapter loginAdapter =
                AdapterFactory.getInstance().getLoginAdapter(getApplicationContext());
        loginAdapter.isLogged(new LoginFinishedCallback() {
            @Override
            public void onLoginSucceeded(User u) {
                Intent intent = new Intent(SplashActivity.this, DrawerActivity.class);
                startCheckingDelay(intent);
            }

            @Override
            public void onLoginFailed(LoginError t) {
                if (t.getType() == ErrorType.HTTP_ERROR) {
                    if (checkedNetwork()) {
                        ConfirmDialog.show(SplashActivity.this, getString(R.string.error),
                                getString(R.string.network_present_but_error),
                                new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        checkNetwork();
                                    }
                                }, null);
                    }
                } else {
                    Intent intent = new Intent(SplashActivity.this, intro()
                            ? MainActivity.class : IntroductionActivity.class);
                    startCheckingDelay(intent);
                }
            }
        });
    }

    private void startCheckingDelay(final Intent intent) {
        long delayConsumed = System.currentTimeMillis() - sStart;
        long delayRemaining = SPLASH_SCREEN_DELAY - delayConsumed;
        Timeout.schedule(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, delayRemaining);
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

    private boolean intro() {
        boolean introDone = false;
        SharedPreferences sharedPreferences = AdapterFactory.getInstance().getSharedPreferences(
                getApplicationContext());
        if (sharedPreferences.contains(INTRO)) {
            introDone = true;
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(INTRO, true);
            editor.apply();
        }
        return introDone;
    }

    private boolean checkedNetwork() {
        return NetworkController.checkNetwork(this, null, new Runnable() {
            @Override
            public void run() {
                checkNetwork();
            }
        });
    }
}
