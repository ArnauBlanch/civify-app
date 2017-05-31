package com.civify.activity.fragments.issue;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.civify.adapter.SimpleCallback;
import com.civify.adapter.issue.IssueAdapter;
import com.civify.model.issue.Issue;
import com.civify.utils.AdapterFactory;

public class IssueButtonListener implements OnClickListener {

    private final IssueAdapter mIssueAdapter;
    private final Context mContext;
    private final Issue mIssue;
    private AppCompatButton mButton;
    private final IssueButton mDoButton;
    private final IssueButton mUndoButton;
    private final View mParentView;
    private final IssueDetailsFragment mDetailsFragment;

    public IssueButtonListener(IssueDetailsFragment detailsFragment,
            View parentView, Issue issue, IssueButton doButton, IssueButton undoButton) {
        mDetailsFragment = detailsFragment;
        mParentView = parentView;
        mContext = parentView.getContext();
        mIssue = issue;
        mIssueAdapter = AdapterFactory.getInstance().getIssueAdapter(mContext);
        mDoButton = doButton;
        mUndoButton = undoButton;
    }

    @Override
    public void onClick(View v) {
        mButton = (AppCompatButton) v;
        if (((TextView) v).getText().toString()
                .equals(mContext.getString(mDoButton.getText()))) {
            doAction();
        } else {
            undoAction();
        }
    }

    private void doAction() {
        if (mDoButton == IssueButton.CONFIRM) {
            mIssueAdapter
                    .confirmIssue(mIssue.getIssueAuthToken(),
                            new IssueButtonCallback(mUndoButton));
            mIssue.setConfirmVotes(mIssue.getConfirmVotes() + 1);
            mDetailsFragment.addConfirmValue();
        } else if (mDoButton == IssueButton.REPORT) {
            mIssueAdapter
                    .reportIssue(mIssue.getIssueAuthToken(),
                            new IssueButtonCallback(mUndoButton));
            mIssue.setReports(mIssue.getReports() + 1);
        } else {
            mIssueAdapter
                    .resolveIssue(mIssue.getIssueAuthToken(),
                            new IssueButtonCallback(mUndoButton));
            mIssue.setResolvedVotes(mIssue.getResolvedVotes() + 1);
        }
    }

    private void undoAction() {
        if (mDoButton == IssueButton.CONFIRM) {
            mIssueAdapter.unconfirmIssue(mIssue.getIssueAuthToken(),
                    new IssueButtonCallback(mDoButton));
            mIssue.setConfirmVotes(mIssue.getConfirmVotes() - 1);
            mDetailsFragment.addConfirmValue();
        } else if (mDoButton == IssueButton.REPORT) {
            mIssueAdapter.unreportIssue(mIssue.getIssueAuthToken(),
                    new IssueButtonCallback(mDoButton));
        } else {
            mIssueAdapter
                    .unresolveIssue(mIssue.getIssueAuthToken(),
                            new IssueButtonCallback(mDoButton));
        }
    }

    private class IssueButtonCallback implements SimpleCallback {
        private final IssueButton mIssueButton;

        IssueButtonCallback(IssueButton issueButton) {
            mIssueButton = issueButton;
        }

        @Override
        public void onSuccess() {
            changeButtonStyle(mButton, mIssueButton);
            Snackbar.make(mParentView, mIssueButton.getMessage(), Snackbar.LENGTH_SHORT)
                    .show();
        }

        @Override
        public void onFailure() {
            Snackbar.make(mParentView, mIssueButton.getErrorMessage(), Snackbar.LENGTH_SHORT)
                    .show();
        }

        private void changeButtonStyle(AppCompatButton button, IssueButton issueButton) {
            button.setText(issueButton.getText());
            button.setBackgroundResource(issueButton.getDrawable());
            button.setTextColor(mContext.getResources().getColor(issueButton.getTextColor()));
        }
    }
}
