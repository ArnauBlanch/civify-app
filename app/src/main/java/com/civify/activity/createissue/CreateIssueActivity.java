package com.civify.activity.createissue;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.civify.R;
import com.civify.activity.CustomViewPager;
import com.civify.adapter.UserAdapter;
import com.civify.adapter.issue.IssueAdapter;
import com.civify.model.User;
import com.civify.model.issue.Category;
import com.civify.model.issue.Issue;
import com.civify.service.issue.IssueSimpleCallback;
import com.civify.utils.AdapterFactory;

public class CreateIssueActivity extends CameraGalleryLocationActivity {

    public static final int ISSUE_CREATION = 10110;
    public static final int ISSUE_CREATED = 3343;
    private IssueAdapter mIssueAdapter;
    private ViewPager mViewPager;

    private String mTitle;
    private Category mCategory;
    private Bitmap mImageBitmap;
    private boolean mRisk;
    private String mDescription;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences userPreferences =
                getSharedPreferences("USERPREFS", Context.MODE_PRIVATE);
        mIssueAdapter = AdapterFactory.getInstance().getIssueAdapter(userPreferences);
        setContentView(R.layout.create_issue_layout);

        mViewPager = (CustomViewPager) findViewById(R.id.viewpager);

        // Set an Adapter on the ViewPager
        mViewPager.setAdapter(new CreateIssuePagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(mViewPager.getAdapter().getCount() - 1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_issue_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.report_new_issue));

        setupCloseKeyboard(findViewById(android.R.id.content));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupCloseKeyboard(final View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard();
                    view.requestFocus();
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupCloseKeyboard(innerView);
            }
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager
                .hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(), 0);

    }

    private void nextPage() {
        hideSoftKeyboard();
        findViewById(android.R.id.content).requestFocus();
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

    public void titleButtonListener(View v) {
        EditText title = (EditText) findViewById(R.id.title_input);
        TextInputLayout titleLayout = (TextInputLayout) findViewById(R.id.title_input_layout);
        if (title.getText().length() == 0) {
            titleLayout.setError(getString(R.string.must_insert_issue_title));
        } else {
            title.setError(null);
            mTitle = title.getText().toString();
            nextPage();
        }
    }

    public void categoryButtonListener(View v) {
        Spinner categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        TextView categoryValidation = (TextView) findViewById(R.id.category_validation);
        if (categorySpinner.getSelectedItemPosition() == categorySpinner.getCount()) {
            categoryValidation.setVisibility(View.VISIBLE);
        } else {
            categoryValidation.setVisibility(View.INVISIBLE);
            int categoryId =
                    ((Spinner) findViewById(R.id.category_spinner)).getSelectedItemPosition();
            mCategory = Category.values()[categoryId];
            nextPage();
        }
    }

    public void photoButtonListener(View v) {
        ImageView imageView = (ImageView) findViewById(R.id.photo_view);
        TextView message = (TextView) findViewById(R.id.photo_validation);
        if (imageView.getDrawable() != null) {
            nextPage();
        } else {
            message.setVisibility(View.VISIBLE);
        }
    }

    public void riskButtonListener(View v) {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        RadioButton radioButton = (RadioButton) findViewById(R.id.radio_yes);
        TextView message = (TextView) findViewById(R.id.risk_validation);
        if (radioGroup.getCheckedRadioButtonId() != -1) {
            message.setVisibility(View.INVISIBLE);
            mRisk = radioGroup.getCheckedRadioButtonId()
                    == radioButton.getId();
            nextPage();
        } else {

            message.setVisibility(View.VISIBLE);
        }
    }

    public void descriptionListener(View v) {
        hideSoftKeyboard();
        findViewById(android.R.id.content).requestFocus();
        TextInputLayout descLayout = (TextInputLayout) findViewById(R.id.description_layout);
        mDescription = ((TextView) findViewById(R.id.description_input)).getText().toString();


        if (mDescription.isEmpty()) {
            descLayout.setError(getString(R.string.must_insert_description));
        } else {
            descLayout.setError(null);
            processIssue();
        }
    }

    private void processIssue() {
        User currentUser = UserAdapter.getCurrentUser();
        Location currentLocation = getCurrentLocation();

        // Initialize the location fields
        if (currentLocation != null) {
            float longitude = (float) currentLocation.getLongitude();
            float latitude = (float) currentLocation.getLatitude();

            showProgressDialog();
            Issue newIssue =
                    new Issue(mTitle, mDescription, mCategory, mRisk, longitude, latitude,
                            mImageBitmap, currentUser.getUserAuthToken());
            mIssueAdapter.createIssue(newIssue, new IssueSimpleCallback() {
                @Override
                public void onSuccess(Issue issue) {
                    mProgressDialog.dismiss();
                    setResult(ISSUE_CREATED);
                    finish();
                }

                @Override
                public void onFailure() {
                    mProgressDialog.dismiss();
                    Snackbar.make(findViewById(R.id.create_issue_linearlayout),
                            getString(R.string.couldnt_create_issue), Snackbar.LENGTH_LONG)
                            .show();
                }
            });
        } else {
            showError(R.string.couldnt_get_location);
        }
    }

    @Override
    protected void handlePhotoResult(Bitmap imageBitmap) {
        ImageView imageView = (ImageView) findViewById(R.id.photo_view);
        imageView.setImageBitmap(imageBitmap);
        TextView message = (TextView) findViewById(R.id.photo_validation);
        message.setVisibility(View.GONE);
        mImageBitmap = imageBitmap;
    }

    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.creating_new_issue));
        mProgressDialog.show();
    }
}
