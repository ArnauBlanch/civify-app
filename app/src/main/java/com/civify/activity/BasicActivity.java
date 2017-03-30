package com.civify.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import com.civify.R;
import com.civify.model.CivifyMap;

public class BasicActivity extends BaseActivity {

    private static BasicActivity sInstance;
    private CivifyMap mCivifyMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        sInstance = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setMap();
    }

    private void setMap() {
        mCivifyMap = new CivifyMap(this);
        Fragment mapFragment = mCivifyMap.getMapFragment();
        mCivifyMap.enableLocationUpdates();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_placeholder, mapFragment)
                .commit();
    }

    @Override
    // Called after onCreate
    protected void onResume() {
        super.onResume();
        mCivifyMap.enableLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCivifyMap.disableLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        mCivifyMap.onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCivifyMap.onMapSettingsResults(requestCode, resultCode);
    }

    @NonNull
    public static BasicActivity getInstance() {
        return sInstance;
    }

}
