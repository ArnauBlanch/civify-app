package com.civify.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.civify.R;
import com.civify.activity.createissue.CameraGalleryActivity;
import com.civify.activity.createissue.CameraGalleryLocationActivity;
import com.civify.activity.createissue.CategorySpinnerAdapter;
import com.civify.adapter.issue.IssueAdapter;
import com.civify.model.issue.Category;
import com.civify.utils.AdapterFactory;

public class EditIssueActivity extends CameraGalleryActivity {
    EditText mIssuename;
    RadioGroup mPosesrisk;
    private Category mCategory;
    AppCompatButton save;
    private Bitmap mImageBitmap;
    private IssueAdapter mIssueAdapter;
    private View.OnClickListener lis = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

           // mIssueAdapter.editIssue();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_issue);
        mIssuename = (EditText) findViewById(R.id.IssueName);
        mPosesrisk = (RadioGroup) findViewById(R.id.risk);
        save = (AppCompatButton) findViewById(R.id.savechangeButton);
        save.setOnClickListener(lis);
        mIssueAdapter = AdapterFactory.getInstance().getIssueAdapter(this);

        Spinner categorySpinner = (Spinner) findViewById(R.id.nameCategorySpinner);
        categorySpinner.setAdapter(new CategorySpinnerAdapter(getApplicationContext(),
                R.layout.text_and_icon_spinner_tem));
        categorySpinner.setSelection(categorySpinner.getCount());
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
