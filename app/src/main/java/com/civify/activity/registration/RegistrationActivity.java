package com.civify.activity.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.civify.R;
import com.civify.activity.BaseActivity;
import com.civify.activity.DrawerActivity;
import com.civify.adapter.LoginAdapter;
import com.civify.adapter.LoginError;
import com.civify.adapter.LoginFinishedCallback;
import com.civify.adapter.SimpleCallback;
import com.civify.adapter.UserAdapter;
import com.civify.adapter.ValidationCallback;
import com.civify.model.User;
import com.civify.utils.AdapterFactory;

public class RegistrationActivity
        extends BaseActivity {

    private UserAdapter mUserAdapter;
    private ViewPager mViewPager;
    private LoginAdapter mLoginAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserAdapter = AdapterFactory.getInstance().getUserAdapter();
        mLoginAdapter = AdapterFactory.getInstance().getLoginAdapter(this);
        setContentView(R.layout.registration_layout);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        // Set an Adapter on the ViewPager
        mViewPager.setAdapter(new RegistrationPagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(mViewPager.getAdapter().getCount() - 1);

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
        final String username = ((EditText) findViewById(R.id.username_input)).getText().toString();
        String name = ((EditText) findViewById(R.id.name_input)).getText().toString();
        String surname = ((EditText) findViewById(R.id.surname_input)).getText().toString();
        String email = ((EditText) findViewById(R.id.email_input)).getText().toString();
        final String password = ((EditText) findViewById(R.id.password_input)).getText().toString();
        String password2 = ((EditText) findViewById(R.id.password2_input)).getText().toString();

        User newUser = new User(username, name, surname, email, password, password2);
        mUserAdapter.registerUser(newUser, new SimpleCallback() {
            @Override
            public void onSuccess() {
                mLoginAdapter.login(username, password, new LoginFinishedCallback() {
                    @Override
                    public void onLoginSucceeded(User u) {
                        finish();
                        startActivity(new Intent(getApplicationContext(),
                                DrawerActivity.class));
                    }

                    @Override
                    public void onLoginFailed(LoginError e) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                e.getType().toString(), Toast.LENGTH_SHORT);
                        toast.show();
                        //Mostrar l'error per pantalla corresponent
                    }
                });
            }

            @Override
            public void onFailure() {
                Snackbar.make(findViewById(R.id.registration_linearlayout),
                        "There was an error when registering", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

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

    public void usernameButtonListener(View v) {
        if (((EditText) findViewById(R.id.username_input)).getText().length() == 0) {
            ((EditText) findViewById(R.id.username_input)).setText("");
        }
        mUserAdapter.checkValidUnusedUsername(
                ((EditText) findViewById(R.id.username_input)).getText().toString(),
                new ValidationCallback() {
                    @Override
                    public void onValidationResponse(int response) {
                        if (response == UserAdapter.VALID_UNUSED) {
                            nextPage();
                        }
                    }
                });
    }

    public void emailButtonListener(View v) {
        if (((EditText) findViewById(R.id.email_input)).getText().length() == 0) {
            ((EditText) findViewById(R.id.email_input)).setText("");
        }
        mUserAdapter.checkValidUnusedEmail(
                ((EditText) findViewById(R.id.email_input)).getText().toString(),
                new ValidationCallback() {
                    @Override
                    public void onValidationResponse(int response) {
                        if (response == UserAdapter.VALID_UNUSED) {
                            nextPage();
                        }
                    }
                });
    }

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
