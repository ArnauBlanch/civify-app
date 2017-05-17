package com.civify.activity.fragments.award;

import android.graphics.drawable.Drawable;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.civify.R;
import com.civify.adapter.UserAdapter;
import com.civify.model.award.Award;

public class AwardDetailsFragment extends Fragment {

    private static final String TAG_AWARD = "award";

    private static final int GREEN_BUTTON_WHITE_TEXT = 1;
    private static final int GREEN_BUTTON_RED_TEXT = 2;
    private static final int WHITE_BUTTON = 3;

    private View mViewDetails;
    private Award mAward;

    public AwardDetailsFragment() {
    }

    public static AwardDetailsFragment newInstance(@NonNull Award award) {
        Bundle data = new Bundle();
        data.putSerializable(TAG_AWARD, award);
        AwardDetailsFragment fragment = new AwardDetailsFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mViewDetails = inflater.inflate(R.layout.fragment_award_details, container, false);
        init();
        return mViewDetails;
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    private void init() {
        Bundle bundle = getArguments();
        mAward = (Award) bundle.getSerializable(TAG_AWARD);
        addImage();
        addCoins();
        addTitle();
        addCompany();
        addDescription();
        addButton();
    }

    private void addImage() {
        ImageView image = (ImageView) mViewDetails.findViewById(R.id.reward_details_image);
        Glide.with(this)
                .load(mAward.getPicture().getMedUrl())
                .centerCrop()
                .into(image);
    }

    private void addCoins() {
        TextView coins = (TextView) mViewDetails.findViewById(R.id.reward_details_coins);
        coins.setText(String.valueOf(mAward.getPrice()));
    }

    private void addTitle() {
        TextView title = (TextView) mViewDetails.findViewById(R.id.reward_details_title);
        title.setText(mAward.getTitle());
    }

    private void addCompany() {
        TextView company = (TextView) mViewDetails.findViewById(R.id.reward_details_company);
        company.setText(mAward.getCommerceOffering());
    }

    private void addDescription() {
        TextView description = (TextView) mViewDetails.findViewById(R.id
                .reward_details_description);
        description.setText(mAward.getDescription());
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    private void addButton() {
        final Button button = (Button) mViewDetails.findViewById(R.id.reward_details_buy_button);
        changeTypeButton(button,
                mAward.getPrice() > UserAdapter.getCurrentUser().getCoins() ? GREEN_BUTTON_RED_TEXT
                        : GREEN_BUTTON_WHITE_TEXT);
        button.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                    changeTypeButton(button, WHITE_BUTTON);
            }
        });
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    private void changeTypeButton(Button button, int type) {
        Drawable drawable;
        int color;
        boolean enable;
        switch (type) {
            case GREEN_BUTTON_RED_TEXT:
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.green_bg_button);
                color = ContextCompat.getColor(getActivity(), R.color.red);
                enable = false;
                break;
            case WHITE_BUTTON:
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.green_border_button);
                color = ContextCompat.getColor(getActivity(), R.color.green);
                enable = false;
                break;
            case GREEN_BUTTON_WHITE_TEXT:
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.green_bg_button);
                color = ContextCompat.getColor(getActivity(), R.color.white);
                enable = true;
                break;
            default:
                drawable = null;
                color = 0;
                enable = false;
                break;
        }
        button.setBackground(drawable);
        button.setTextColor(color);
        button.setEnabled(enable);
    }
}
