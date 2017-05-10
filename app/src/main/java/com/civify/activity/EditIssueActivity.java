package com.civify.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.civify.R;
import com.civify.activity.createissue.CameraGalleryActivity;
import com.civify.activity.createissue.CategorySpinnerAdapter;
import com.civify.adapter.issue.IssueAdapter;
import com.civify.model.issue.Category;
import com.civify.model.issue.Issue;
import com.civify.service.issue.IssueSimpleCallback;
import com.civify.utils.AdapterFactory;

public class EditIssueActivity extends CameraGalleryActivity {

    private static final String TAG_ISSUE = "issue";

    private EditText mIssuename;
    private EditText mIssueDesc;
    private RadioGroup mPosesrisk;
    private Category mCategory;
    private AppCompatButton mSave;
    private Bitmap mImageBitmap;
    private ImageView mImageView;
    private IssueAdapter mIssueAdapter;
    private Issue mIssue;
    private Boolean mRisk;
    private RadioGroup.OnCheckedChangeListener mRadioListener =
            new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.yes:
                            mRisk = true;
                            break;
                        case R.id.no:
                            mRisk = false;
                            break;
                        default:
                            break;
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_issue);
        mIssuename = (EditText) findViewById(R.id.IssueName);
        mIssueDesc = (EditText) findViewById(R.id.descriptionTex);
        mPosesrisk = (RadioGroup) findViewById(R.id.risk);
        mImageView = (ImageView) findViewById(R.id.eventView);
        mSave = (AppCompatButton) findViewById(R.id.savechangeButton);
        mRisk = false;
        mIssueAdapter = AdapterFactory.getInstance().getIssueAdapter(this);
        Spinner categorySpinner = (Spinner) findViewById(R.id.nameCategorySpinner);
        categorySpinner.setAdapter(new CategorySpinnerAdapter(getApplicationContext(),
                R.layout.text_and_icon_spinner_tem));
        categorySpinner.setSelection(categorySpinner.getCount());
        Intent intent = getIntent();
        Bundle data = intent.getBundleExtra(TAG_ISSUE);
        mIssue = (Issue) data.getSerializable(TAG_ISSUE);
        mIssuename.setText(mIssue.getTitle());
        mIssueDesc.setText(mIssue.getDescription());
        String url = mIssue.getPicture().getMedUrl();
        Glide.with(this).load(url).into(mImageView);
        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; ++i) {
            if (mIssue.getCategory().equals(categories[i])) {
                categorySpinner.setSelection(i);
            }
        }
        if (mIssue.isRisk()) {
            mPosesrisk.check(R.id.yes);
            mRisk = true;
        } else mPosesrisk.check(R.id.no);
        Toolbar toolbar = (Toolbar) findViewById(R.id.create_issue_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.edit_issue));
        mPosesrisk.setOnCheckedChangeListener(mRadioListener);
        setupCloseKeyboard(findViewById(android.R.id.content));
    }

    @Override
    protected void handlePhotoResult(Bitmap imageBitmap) {
        ImageView imageView = (ImageView) findViewById(R.id.eventView);
        imageView.setImageBitmap(imageBitmap);
        mImageBitmap = imageBitmap;
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
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager
                .hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(), 0);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_issue, menu);
        return true;
    }

    public void saveListener(View v) {
        int categoryId = ((Spinner) findViewById(R.id.nameCategorySpinner))
                .getSelectedItemPosition();
        mCategory = Category.values()[categoryId];
        Issue editedIssue = new Issue(mIssuename.getText().toString(), mIssueDesc.getText()
                .toString(), mCategory,
                mRisk, mIssue.getLongitude(), mIssue.getLatitude(), mImageBitmap, mIssue
                .getUserAuthToken());
        mIssueAdapter.editIssue(mIssue.getIssueAuthToken(), editedIssue,
                new IssueSimpleCallback() {
                @Override
                public void onSuccess(Issue issue) {
                    onBackPressed();
                }

                @Override
                public void onFailure() {

                }
            });
    }
}
