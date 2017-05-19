package com.civify.activity.fragments.reward;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.civify.R;
import com.civify.model.award.ExchangedAward;

public class ShowQrFragment extends Fragment {

    private static final String TAG_EXCHANGE_AWARD = "exchange_award";

    private View mView;
    private ExchangedAward mExchangedAward;

    private TextView mTitle;
    private TextView mBusiness;
    private TextView mDate;
    private ImageView mQrcode;
    private TextView mTextQrcode;
    private TextView mState;

    public ShowQrFragment() {
        // Required empty public constructor
    }

    public static ShowQrFragment newInstance(@NonNull ExchangedAward exchangedAward) {
        ShowQrFragment fragment = new ShowQrFragment();
        Bundle data = new Bundle();
        data.putSerializable(TAG_EXCHANGE_AWARD, exchangedAward);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_show_qr, container, false);
        init();
        return mView;
    }

    private void init() {
        Bundle bundle = getArguments();
        mExchangedAward = (ExchangedAward) bundle.getSerializable(TAG_EXCHANGE_AWARD);
        mTitle = (TextView) mView.findViewById(R.id.qr_title);
        mBusiness = (TextView) mView.findViewById(R.id.qr_business);
        mDate = (TextView) mView.findViewById(R.id.qr_date);
        mQrcode = (ImageView) mView.findViewById(R.id.qr_qrcode);
        mTextQrcode = (TextView) mView.findViewById(R.id.qr_textQrcode);
        mState = (TextView) mView.findViewById(R.id.qr_state);
    }
}
