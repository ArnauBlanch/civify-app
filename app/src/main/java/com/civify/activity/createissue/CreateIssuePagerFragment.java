package com.civify.activity.createissue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.civify.R;
import com.civify.activity.SplashActivity;
import com.civify.model.map.CivifyMap;
import com.civify.model.map.MapNotLoadedException;

public class CreateIssuePagerFragment extends Fragment {

    private static final int ISSUE_TITLE = 0;
    private static final int ISSUE_CATEGORY = 1;
    private static final int ISSUE_PHOTO = 2;
    private static final int ISSUE_RISK = 3;

    private View mView;
    private int mPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        checkLocationAvailable();

        mPage = getArguments().getInt("page");
        mView = getActivity().getLayoutInflater()
                .inflate(getLayoutResource(mPage), container, false);

        mView.setTag(mPage);

        if (mPage == ISSUE_CATEGORY) {
            Spinner categorySpinner = (Spinner) mView.findViewById(R.id.category_spinner);
            categorySpinner.setAdapter(new CategorySpinnerAdapter(getActivity(),
                    R.layout.text_and_icon_spinner_tem));
            categorySpinner.setSelection(categorySpinner.getCount());
            categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position,
                        long id) {
                    mView.findViewById(R.id.category_validation).setVisibility(View
                            .INVISIBLE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        return mView;
    }

    private static int getLayoutResource(int pageId) {
        switch (pageId) {
            case ISSUE_TITLE:
                return R.layout.create_issue_title;
            case ISSUE_CATEGORY:
                return R.layout.create_issue_category;
            case ISSUE_PHOTO:
                return R.layout.create_issue_photo;
            case ISSUE_RISK:
                return R.layout.create_issue_risk;
            default:
                return R.layout.create_issue_description;
        }
    }

    private void checkLocationAvailable() {
        try {
            CivifyMap.getInstance();
        } catch (MapNotLoadedException ignore) {
            Log.d(CreateIssuePagerFragment.class.getSimpleName(), "Starting Splash");
            startActivity(new Intent(getActivity(), SplashActivity.class));
            getActivity().finish();
        }
    }
}
