package com.civify.civify.activity.issue;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.civify.civify.R;
import com.civify.civify.adapter.IssueAdapter;
import com.civify.civify.utils.AdapterFactory;

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
        if (title.getText().length() == 0) {
            title.setError(getString(R.string.must_insert_issue_title));
        } else {
            title.setError(null);
            nextPage();
        }
    }

    @Override
    protected void handlePhotoResult(Bitmap imageBitmap) {
        ImageView imageView = (ImageView) findViewById(R.id.photo_view);
        imageView.setImageBitmap(imageBitmap);
    }
}
