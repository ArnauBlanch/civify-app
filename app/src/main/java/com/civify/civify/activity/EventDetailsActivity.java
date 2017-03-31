package com.civify.civify.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.civify.civify.R;
import java.util.Date;
import utils.IssueAdapterDummy;

public class EventDetailsActivity extends BaseActivity {

    public static final int MILLIS_TO_DAYS = 1000 * 3600 * 24;
    private IssueAdapterDummy mIssueAdapter;

    private TextView mTextViewTitle;
    private TextView mTextViewCategory;
    private TextView mTextViewRisk;
    private TextView mTextViewDescription;
    private TextView mTextViewStreet;
    private TextView mTextViewDistance;
    private TextView mTextViewDate;

    private String mTitleIssue;
    //TODO enum and select iconº
    private String mCategory;
    private boolean mRisk;
    private String mDescription;
    private String mStreet;
    private Float mDistance;
    private Date mDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        initParameters();
        insertInfo();
    }

    private void insertInfo() {
        String risk;
        risk = mRisk ? getString(R.string.yes) : getString(R.string.no);
        String distance = mDistance.toString();
        int days = (int) (System.currentTimeMillis() - mDate.getTime())/ MILLIS_TO_DAYS;
        String textDate;
        textDate = days > 0 ?  days + getString(R.string.daysAgo) : getString(R.string.today);

        mTextViewTitle.setText(mTitleIssue);
        mTextViewCategory.setText(mCategory);
        mTextViewRisk.setText(risk);
        mTextViewDescription.setText(mDescription);
        mTextViewDistance.setText(distance);
        mTextViewStreet.setText(mStreet);
        mTextViewDate.setText(textDate);
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

        mTextViewTitle = (TextView) findViewById(R.id.nameText);
        mTextViewCategory = (TextView) findViewById(R.id.nameCategoryText);
        mTextViewRisk = (TextView) findViewById(R.id.riskAnswer);
        mTextViewDescription = (TextView) findViewById(R.id.descriptionText);
        mTextViewStreet = (TextView) findViewById(R.id.streetText);
        mTextViewDistance = (TextView) findViewById(R.id.distanceText);
        mTextViewDate = (TextView) findViewById(R.id.sinceText);
    }
}
