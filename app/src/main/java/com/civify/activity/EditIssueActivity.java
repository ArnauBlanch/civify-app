package com.civify.activity;

import android.os.Bundle;
import android.util.Log;

import com.civify.R;
import com.civify.model.issue.Issue;

public class EditIssueActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_issue);
        Log.d("Ricard", "Final onCreate");
    }

}
