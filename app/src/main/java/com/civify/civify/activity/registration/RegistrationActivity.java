package com.civify.civify.activity.registration;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import com.civify.civify.R;
import com.civify.civify.adapter.UserAdapter;
import com.civify.civify.model.User;
import com.civify.civify.utils.AdapterFactory;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

class RegistrationActivity extends AppCompatActivity {
    private UserAdapter mUserAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserAdapter = AdapterFactory.getInstance().getUserController();
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Nunito-Regular.ttf").setFontAttrId(R.attr.fontPath).build()
        );
        setContentView(R.layout.registration_layout);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        // Set an Adapter on the ViewPager
        mViewPager.setAdapter(new RegistrationPagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(mViewPager.getAdapter().getCount() - 1);

        Toolbar toolbar = ((Toolbar) findViewById(R.id.registration_toolbar));
        Log.e("toolbar", String.valueOf(findViewById(R.id.registration_toolbar) == null));
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            if (mViewPager.getCurrentItem() > 0) {
                previousPage();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void nextPage() {
        if (mViewPager.getCurrentItem() < mViewPager.getAdapter().getCount() - 1) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        }
    }

    private void previousPage() {
        if (mViewPager.getCurrentItem() > 0) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0)
            super.onBackPressed();
        else
            previousPage();
    }

    public void register() {
        String username = ((EditText) findViewById(R.id.username_input)).getText().toString();
        String name = ((EditText) findViewById(R.id.name_input)).getText().toString();
        String surname = ((EditText) findViewById(R.id.surname_input)).getText().toString();
        String email = ((EditText) findViewById(R.id.email_input)).getText().toString();
        String password = ((EditText) findViewById(R.id.password_input)).getText().toString();
        try {
            mUserAdapter.registerUser(
                new User(username, name, surname, email, password)
            );
        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.registration_layout), e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }
}