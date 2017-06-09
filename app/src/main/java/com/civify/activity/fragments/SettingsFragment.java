package com.civify.activity.fragments;

import static com.civify.activity.DrawerActivity.SETTINGS_ID;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends BasicFragment {

    private static final String CA = "ca";
    private static final String ES = "es";
    private static final String EN = "en";
    private static final String SHAREDPREFSLOCALE = "LocalePrefs";
    private static final String LANGUAGE = "lang";
    private static final float DISABLED_ALPHA = 0.15f;
    private static final float ENABLED_ALPHA = 1;
    private AppCompatButton mConfirmButton;
    private View mSettings;
    private Bundle mState;
    private String mCurrentLang;
    private String mNewlang;
    private RadioGroup mRadiolang;

    public SettingsFragment() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NavigateFragment.
     */
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getFragmentId() {
        return SETTINGS_ID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mState = savedInstanceState;
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mSettings = inflater.inflate(R.layout.fragment_settings, container, false);
        mRadiolang = (RadioGroup) mSettings.findViewById(R.id.langradio);
        mConfirmButton = (AppCompatButton) mSettings.findViewById(R.id.confirmButton);
        String currentLocale = Locale.getDefault().getLanguage();
        switch (currentLocale) {
            case CA:
                mRadiolang.check(R.id.radiocat);
                mCurrentLang = CA;
                break;
            case ES:
                mRadiolang.check(R.id.radiocast);
                mCurrentLang = ES;
                break;
            case EN:
                mRadiolang.check(R.id.radioeng);
                mCurrentLang = EN;
                break;
            default:
                break;
        }
        mRadiolang.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                handleRadio(checkedId);
            }
        });
        mConfirmButton.setAlpha(DISABLED_ALPHA);
        mConfirmButton.setEnabled(false);
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage(v);
            }
        });
        return mSettings;
    }

    private void handleRadio(int checkedId) {
        switch (checkedId) {
            case R.id.radiocat:
                if (!mCurrentLang.equals(CA)) {
                    mConfirmButton.setAlpha(ENABLED_ALPHA);
                    mConfirmButton.setEnabled(true);
                    mNewlang = CA;
                } else {
                    mConfirmButton.setAlpha(DISABLED_ALPHA);
                    mConfirmButton.setEnabled(false);
                }
                break;
            case R.id.radiocast:
                if (!mCurrentLang.equals(ES)) {
                    mConfirmButton.setAlpha(ENABLED_ALPHA);
                    mConfirmButton.setEnabled(true);
                    mNewlang = ES;
                } else {
                    mConfirmButton.setAlpha(DISABLED_ALPHA);
                    mConfirmButton.setEnabled(false);
                }
                break;
            case R.id.radioeng:
                if (!mCurrentLang.equals(EN)) {
                    mConfirmButton.setAlpha(ENABLED_ALPHA);
                    mConfirmButton.setEnabled(true);
                    mNewlang = EN;
                } else {
                    mConfirmButton.setAlpha(DISABLED_ALPHA);
                    mConfirmButton.setEnabled(false);
                }
                break;
            default:
                break;
        }
    }

    private void setLanguage(View v) {
        Configuration config;
        config = new Configuration(getResources().getConfiguration());
        SharedPreferences sprefs = getActivity().getApplicationContext()
                .getSharedPreferences(SHAREDPREFSLOCALE, 0);
        SharedPreferences.Editor editor = sprefs.edit();
        switch (v.getId()) {
            case R.id.confirmButton:
                config.locale = new Locale(mNewlang);
                editor.putString(LANGUAGE, mNewlang);
                Locale.setDefault(config.locale);
                mCurrentLang = mNewlang;
                break;
            default:
                break;
        }
        editor.apply();
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        Intent intent = new Intent(getActivity().getApplicationContext(), DrawerActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}

