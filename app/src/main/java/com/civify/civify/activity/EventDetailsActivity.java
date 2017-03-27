package com.civify.civify.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.civify.civify.R;

import utils.IssueAdapterDummy;

public class EventDetailsActivity extends AppCompatActivity {

    private IssueAdapterDummy mIssueAdapter;

    private TextView mTextViewTitle;
    private TextView mTextViewCategory;
    private TextView mTextViewRisk;
    private TextView mTextViewDescription;
    private TextView mTextViewStreet;
    private TextView mTextViewDistance;
    private TextView mTextViewDate;

    private String mTitleIssue;
    private String mCategory;
    private String mRisk;
    private String mDescription;
    private String mStreet;
    private String mDistance;
    private String mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        initParameters();
        insertInfo();
    }

    private void insertInfo() {
        mTextViewTitle.setText(mTitleIssue);
        mTextViewCategory.setText(mCategory);
        mTextViewRisk.setText(mRisk);
        mTextViewDescription.setText(mDescription);
        mTextViewDistance.setText(mDistance);
        mTextViewStreet.setText(mStreet);
        mTextViewDate.setText(mDate);
    }

    private void initParameters() {
        mIssueAdapter = new IssueAdapterDummy();

        mTitleIssue = mIssueAdapter.getTitle();
        mCategory = mIssueAdapter.getCategory();
        mRisk = mIssueAdapter.getRisk();
        mDescription = mIssueAdapter.getDescription();
        mStreet = mIssueAdapter.getStreet();
        mDistance = mIssueAdapter.getDistance();
        mDate = mIssueAdapter.getDate();

        mTextViewTitle = (TextView)findViewById(R.id.nameText);
        mTextViewCategory = (TextView)findViewById(R.id.nameCategoryText);
        mTextViewRisk = (TextView)findViewById(R.id.riskAnswer);
        mTextViewDescription = (TextView)findViewById(R.id.descriptionText);
        mTextViewStreet = (TextView)findViewById(R.id.streetText);
        mTextViewDistance = (TextView)findViewById(R.id.distanceText);
        mTextViewDate = (TextView)findViewById(R.id.sinceText);
    }
}
