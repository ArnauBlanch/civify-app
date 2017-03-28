package com.civify.civify.activity.registration;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.civify.civify.R;
import com.civify.civify.activity.BaseActivity;
import com.civify.civify.adapter.UserAdapter;
import com.civify.civify.model.User;
import com.civify.civify.utils.AdapterFactory;

@SuppressWarnings({ "CyclicClassDependency", "LawOfDemeter" })
public class RegistrationActivity
        extends BaseActivity {

    private UserAdapter mUserAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserAdapter = AdapterFactory.getInstance().getUserAdapter();
        setContentView(R.layout.registration_layout);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        // Set an Adapter on the ViewPager
        mViewPager.setAdapter(new RegistrationPagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(mViewPager.getAdapter().getCount() - 1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.registration_toolbar);
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
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void nextPage() {
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
        if (mViewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            previousPage();
        }
    }

    private void register() {
        String username = ((EditText) findViewById(R.id.username_input)).getText().toString();
        String name = ((EditText) findViewById(R.id.name_input)).getText().toString();
        String surname = ((EditText) findViewById(R.id.surname_input)).getText().toString();
        String email = ((EditText) findViewById(R.id.email_input)).getText().toString();
        String password = ((EditText) findViewById(R.id.password_input)).getText().toString();
        mUserAdapter.registerUser(new User(username, name, surname, email, password));
    }

    @SuppressWarnings("UnusedParameters")
    public void nameAndSurnameButtonListener(View v) {
        boolean valid = true;
        if (((EditText) findViewById(R.id.name_input)).getText().length() == 0) {
            valid = false;
            findViewById(R.id.name_validation).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.name_validation).setVisibility(View.INVISIBLE);
        }

        if (((EditText) findViewById(R.id.surname_input)).getText().length() == 0) {
            valid = false;
            findViewById(R.id.surname_validation).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.surname_validation).setVisibility(View.INVISIBLE);
        }

        if (valid) {
            nextPage();
        }
    }

    @SuppressWarnings("UnusedParameters")
    public void usernameButtonListener(View v) {
        if (((EditText) findViewById(R.id.username_input)).getText().length() == 0) {
            ((EditText) findViewById(R.id.username_input)).setText("");
        }
        if (mUserAdapter.checkValidUnusedUsername(
                ((EditText) findViewById(R.id.username_input)).getText().toString())
                == UserAdapter.VALID_UNUSED) {
            nextPage();
        }
    }

    @SuppressWarnings("UnusedParameters")
    public void emailButtonListener(View v) {
        if (((EditText) findViewById(R.id.email_input)).getText().length() == 0) {
            ((EditText) findViewById(R.id.email_input)).setText("");
        }
        if (mUserAdapter.checkValidUnusedEmail(
                ((EditText) findViewById(R.id.email_input)).getText().toString())
                == UserAdapter.VALID_UNUSED) {
            nextPage();
        }
    }

    @SuppressWarnings("UnusedParameters")
    public void passwordButtonListener(View v) {
        if (((EditText) findViewById(R.id.password_input)).getText().length() == 0) {
            ((EditText) findViewById(R.id.password_input)).setText("");
        }
        if (((EditText) findViewById(R.id.password2_input)).getText().length() == 0) {
            ((EditText) findViewById(R.id.password2_input)).setText("");
        }
        EditText password = (EditText) findViewById(R.id.password_input);
        EditText matchingPassword = (EditText) findViewById(R.id.password2_input);
        if (mUserAdapter.checkValidPassword(password.getText().toString())
                && password.getText().toString().equals(matchingPassword.getText().toString())) {
            register();
        }
    }
}
