package com.civify.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.civify.R;
import com.civify.activity.createissue.CameraGalleryActivity;
import com.civify.activity.createissue.CategorySpinnerAdapter;
import com.civify.adapter.issue.IssueAdapter;
import com.civify.model.issue.Category;
import com.civify.model.issue.Issue;
import com.civify.utils.AdapterFactory;

public class EditIssueActivity extends CameraGalleryActivity {

    private static final String TAG_ISSUE = "issue";

    private EditText mIssuename;
    private RadioGroup mPosesrisk;
    private Category mCategory;
    private AppCompatButton mSave;
    private Bitmap mImageBitmap;
    private IssueAdapter mIssueAdapter;
    private Issue mIssue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_issue);
        mIssuename = (EditText) findViewById(R.id.IssueName);
        mPosesrisk = (RadioGroup) findViewById(R.id.risk);
        mSave = (AppCompatButton) findViewById(R.id.savechangeButton);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FIXME mIssueAdapter.editIssue();
            }
        });
        mIssueAdapter = AdapterFactory.getInstance().getIssueAdapter(this);

        Spinner categorySpinner = (Spinner) findViewById(R.id.nameCategorySpinner);
        categorySpinner.setAdapter(new CategorySpinnerAdapter(getApplicationContext(),
                R.layout.text_and_icon_spinner_tem));
        categorySpinner.setSelection(categorySpinner.getCount());
        Intent intent = getIntent();
        Bundle data = intent.getBundleExtra(TAG_ISSUE);
        mIssue = (Issue) data.getSerializable(TAG_ISSUE);
    }

    public void categoryButtonListener(View v) {
        Spinner categorySpinner = (Spinner) findViewById(R.id.nameCategorySpinner);
        int categoryId = ((Spinner) findViewById(R.id.category_spinner)).getSelectedItemPosition();
        mCategory = Category.values()[categoryId];
    }

    @Override
    protected void handlePhotoResult(Bitmap imageBitmap) {
        ImageView imageView = (ImageView) findViewById(R.id.eventView);
        imageView.setImageBitmap(imageBitmap);
        TextView message = (TextView) findViewById(R.id.photo_validation);
        message.setVisibility(View.GONE);
        mImageBitmap = imageBitmap;
    }
}
