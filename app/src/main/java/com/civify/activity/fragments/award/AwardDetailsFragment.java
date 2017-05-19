package com.civify.activity.fragments.award;

import android.graphics.Typeface;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;
import com.civify.adapter.UserAdapter;
import com.civify.adapter.UserSimpleCallback;
import com.civify.adapter.award.AwardAdapter;
import com.civify.model.Reward;
import com.civify.model.User;
import com.civify.model.award.Award;
import com.civify.service.award.RewardCallback;
import com.civify.utils.AdapterFactory;

public class AwardDetailsFragment extends BasicFragment {

    public static final float ALPHA = 0.3f;
    private static final String TAG_AWARD = "award";

    private static final int ENABLE_BUTTON = 1;
    private static final int DISABLE_BUTTON = 0;

    private View mViewDetails;
    private Award mAward;
    private AwardAdapter mAwardAdapter;

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
        mAwardAdapter = AdapterFactory.getInstance().getAwardAdapter(getContext());
        mViewDetails = inflater.inflate(R.layout.fragment_award_details, container, false);
        init();
        return mViewDetails;
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
    private void init() {
        Bundle bundle = getArguments();
        mAward = (Award) bundle.getSerializable(TAG_AWARD);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((DrawerActivity) getActivity()).showCoinsOnToolbar();
        toolbar.setTitle(mAward.getTitle());

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
        Button button = (Button) mViewDetails.findViewById(R.id.reward_details_buy_button);
        button.setTypeface(null, Typeface.BOLD);
        changeTypeButton(button,
                mAward.getPrice() > UserAdapter.getCurrentUser().getCoins() ? DISABLE_BUTTON
                        : ENABLE_BUTTON);
        button.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                mAwardAdapter.exchangeAward(mAward.getAwardAuthToken(), new RewardCallback() {
                    @Override
                    public void onSuccess(Reward reward) {
                        showRewardDialog(reward);
                        Snackbar.make(mViewDetails, getString(R.string.dialog_award_bought),
                                Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure() {
                        Snackbar.make(mViewDetails, getString(R.string.problem_award),
                                Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showRewardDialog(Reward reward) {
        final DrawerActivity activity = (DrawerActivity) getActivity();
        AdapterFactory.getInstance().getUserAdapter(getContext())
                .showRewardDialog(activity, reward,
                        new UserSimpleCallback() {
                            @Override
                            public void onSuccess(User user) {
                                activity.setUserHeader();
                                activity.removeCoinsFromToolbar();
                                activity.showCoinsOnToolbar();
                            }

                            @Override
                            public void onFailure() { }
                        });
    }

    private void changeTypeButton(Button button, int type) {
        ImageView coins = (ImageView) mViewDetails.findViewById(R.id
                .reward_coins_icon);
        switch (type) {
            case ENABLE_BUTTON:
                coins.setAlpha(1);
                button.setAlpha(1);
                button.setEnabled(true);
                break;
            case DISABLE_BUTTON:
                coins.setAlpha(ALPHA);
                button.setAlpha(ALPHA);
                button.setEnabled(false);
                break;
            default:
                break;
        }
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.DETAILS_AWARD_ID;
    }
}
