package com.civify.activity.issue;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.civify.R;
import com.civify.adapter.IssueAdapter;
import com.civify.model.Category;
import com.civify.model.Issue;
import com.civify.model.Picture;
import com.civify.service.IssueSimpleCallback;
import com.civify.utils.AdapterFactory;

public class CreateIssueActivity extends CameraGalleryActivity {
    private IssueAdapter mIssueAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences userPreferences = getSharedPreferences("USERPREFS", Context.MODE_PRIVATE);
        mIssueAdapter = AdapterFactory.getInstance().getIssueAdapter(userPreferences);
        setContentView(R.layout.create_issue_layout);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        // Set an Adapter on the ViewPager
        mViewPager.setAdapter(new CreateIssuePagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(mViewPager.getAdapter().getCount() - 1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_issue_toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.report_new_issue));
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

    public void titleButtonListener(View v) {
        EditText title = (EditText) findViewById(R.id.title_input);
        TextInputLayout titleLayout = (TextInputLayout) findViewById(R.id.title_input_layout);
        if (title.getText().length() == 0) {
            titleLayout.setError(getString(R.string.must_insert_issue_title));
        } else {
            title.setError(null);
            nextPage();
        }
    }

    public void categoryButtonListener(View v) {
        nextPage();
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
        TextView message = (TextView) findViewById(R.id.risk_validation);
        if (radioGroup.getCheckedRadioButtonId() != -1) {
            message.setVisibility(View.INVISIBLE);
            nextPage();
        } else {

            message.setVisibility(View.VISIBLE);
        }
    }

    public void descriptionButtonListener(View v) {
        String title = ((TextView) findViewById(R.id.title_input)).getText().toString();
        String description = ((TextView) findViewById(R.id.description_input)).getText().toString();
        Category category = null;
        boolean risk = ((RadioGroup) findViewById(R.id.radio_group)).getCheckedRadioButtonId() == 0;
        float longitude = 0.0f;
        float latitude = 0.0f;
        Picture picture = null;
        String userAuthToken = null;

        Issue newIssue = new Issue(title, description, category, risk,
                longitude, latitude, picture, userAuthToken);
        mIssueAdapter.createIssue(newIssue, new IssueSimpleCallback() {
            @Override
            public void onSuccess(Issue issue) {
                // Issue successfully created
            }

            @Override
            public void onFailure() {
                Snackbar.make(findViewById(R.id.create_issue_linearlayout),
                        getString(R.string.couldnt_create_issue), Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    protected void handlePhotoResult(Bitmap imageBitmap) {
        ImageView imageView = (ImageView) findViewById(R.id.photo_view);
        imageView.setImageBitmap(imageBitmap);
        TextView message = (TextView) findViewById(R.id.photo_validation);
        message.setVisibility(View.GONE);
    }
}
