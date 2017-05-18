package com.civify.activity.fragments.reward;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.civify.R;

public class ShowQrFragment extends Fragment {

    private View mView;

    private TextView mTitle;
    private TextView mBusiness;
    private TextView mDate;
    private ImageView mQrcode;
    private TextView mTextQrcode;
    private TextView mState;

    public ShowQrFragment() {
        // Required empty public constructor
    }

    public static ShowQrFragment newInstance() {
        ShowQrFragment fragment = new ShowQrFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        mTitle = (TextView) mView.findViewById(R.id.qr_title);
        mBusiness = (TextView) mView.findViewById(R.id.qr_business);
        mDate = (TextView) mView.findViewById(R.id.qr_date);
        mQrcode = (ImageView) mView.findViewById(R.id.qr_qrcode);
        mTextQrcode = (TextView) mView.findViewById(R.id.qr_textQrcode);
        mState = (TextView) mView.findViewById(R.id.qr_state);
    }
}
