package com.civify.activity;

import android.os.Bundle;

import com.civify.R;
import com.civify.activity.fragments.MapFragment;

public class BasicActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_placeholder, MapFragment.newInstance())
                .commit();
    }
}
