package com.civify.activity.fragments.rewards;

import android.graphics.drawable.Drawable;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
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
import com.civify.model.StubReward;

public class RewardDetailsFragment extends Fragment {

    private static final int GREEN_BUTTON_WHITE_TEXT = 1;
    private static final int GREEN_BUTTON_RED_TEXT = 2;
    private static final int WHITE_BUTTON = 3;

    private View mViewDetails;
    private StubReward mReward;

    public RewardDetailsFragment() {
    }


    public static RewardDetailsFragment newInstance() {
        RewardDetailsFragment fragment = new RewardDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewDetails = inflater.inflate(R.layout.fragment_reward_details, container, false);
        mReward = new StubReward();
        init();
        return mViewDetails;
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    private void init(){
        addImage();
        addCoins();
        addTitle();
        addCompany();
        addDescription();
        addButton();
    }

    private void addImage(){
        ImageView image = (ImageView) mViewDetails.findViewById(R.id.reward_details_image);
        Glide.with(this)
                .load(mReward.getUrlImage())
                .centerCrop()
                .into(image);
    }

    private void addCoins(){
        TextView coins = (TextView)mViewDetails.findViewById(R.id.reward_details_coins);
        coins.setText(String.valueOf(mReward.getCoins()));
    }

    private void addTitle(){
        TextView title = (TextView) mViewDetails.findViewById(R.id.reward_details_title);
        title.setText(mReward.getTitle());
    }

    private void addCompany(){
        TextView company = (TextView) mViewDetails.findViewById(R.id.reward_details_company);
        company.setText(mReward.getCompany());
    }

    private void addDescription(){
        TextView description = (TextView) mViewDetails.findViewById(R.id
                .reward_details_description);
        description.setText(mReward.getDescription());
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    private void addButton() {
        final Button button = (Button)mViewDetails.findViewById(R.id.reward_details_buy_button);
        if(mReward.getCoins() > UserAdapter.getCurrentUser().getCoins()){
            changeTypeButton(button, GREEN_BUTTON_RED_TEXT);
        }
        else {
            changeTypeButton(button, GREEN_BUTTON_WHITE_TEXT);
        }
        button.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
            changeTypeButton(button, WHITE_BUTTON);
            }
        });
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    private void changeTypeButton(Button button, int type){
        Drawable drawable;
        int color;
        boolean enable;
        switch (type){
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
