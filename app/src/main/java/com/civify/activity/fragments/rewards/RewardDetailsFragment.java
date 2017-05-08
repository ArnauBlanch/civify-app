package com.civify.activity.fragments.rewards;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.civify.R;
import com.civify.model.StubReward;

public class RewardDetailsFragment extends Fragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewDetails = inflater.inflate(R.layout.fragment_reward_details, container, false);
        mReward = new StubReward();
        init();
        return mViewDetails;
    }

    private void init(){
        addImage();
        addCoins();
        addTitle();
        addCompany();
        addDescription();
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
        title.setMovementMethod(new ScrollingMovementMethod());
        title.setText(mReward.getTitle());
    }

    private void addCompany(){
        TextView company = (TextView) mViewDetails.findViewById(R.id.reward_details_company);
        company.setText(mReward.getCompany());
    }

    private void addDescription(){
        TextView description = (TextView) mViewDetails.findViewById(R.id
                .reward_details_description);
        description.setMovementMethod(new ScrollingMovementMethod());
        description.setText(mReward.getDescription());
    }

}
