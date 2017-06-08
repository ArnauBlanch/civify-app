package com.civify.activity.introduction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.civify.R;

public class IntroductionFragment extends Fragment {

    private static final String PAGE_NUMBER = "page_number";
    private static final int THREE = 3;

    private int mPageNumber;

    public static final IntroductionFragment newInstance(int page) {
        IntroductionFragment introductionFragment = new IntroductionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE_NUMBER, page);
        introductionFragment.setArguments(bundle);
        return introductionFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mPageNumber = getArguments().getInt(PAGE_NUMBER);

        return mPageNumber == 0
                ? inflater.inflate(R.layout.introduction_first_page, container, false)
                : inflatePage(inflater, container);
    }

    private View inflatePage(LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.introduction_page, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.introduction_image);
        TextView textView = (TextView) view.findViewById(R.id.introduction_text);
        TextView continueTextView = (TextView) view.findViewById(R.id.introduction_continue_button);
        switch (mPageNumber) {
            case 1:
                imageView.setImageResource(R.drawable.logo_planeta);
                textView.setText(getString(R.string.introduction_page_1));
                continueTextView.setVisibility(View.GONE);
                break;
            case 2:
                imageView.setImageResource(R.drawable.logo_planeta);
                textView.setText(getString(R.string.introduction_page_1));
                continueTextView.setVisibility(View.GONE);
                break;
            case THREE:
                imageView.setImageResource(R.drawable.logo_planeta);
                textView.setText(getString(R.string.introduction_page_1));
                continueTextView.setVisibility(View.GONE);
                break;
            case 4:
                imageView.setImageResource(R.drawable.logo_planeta);
                textView.setText(getString(R.string.introduction_page_1));
                break;
            default:
                break;
        }

        return view;
    }
}
