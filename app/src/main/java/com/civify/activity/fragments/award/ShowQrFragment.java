package com.civify.activity.fragments.award;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;
import com.civify.model.award.ExchangedAward;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class ShowQrFragment extends BasicFragment {

    private static final String TAG_EXCHANGE_AWARD = "exchange_award";
    private static final int WIDTH = 1000;

    private View mView;
    private ExchangedAward mExchangedAward;

    private TextView mTitle;
    private TextView mBusiness;
    private TextView mDate;
    private ImageView mQrcode;
    private TextView mTextQrcode;
    private TextView mStateText;
    private LinearLayout mStateBackground;

    public ShowQrFragment() {
        // Required empty public constructor
    }

    public static ShowQrFragment newInstance(@NonNull ExchangedAward exchangedAward) {
        ShowQrFragment fragment = new ShowQrFragment();
        Bundle data = new Bundle();
        Log.v("DEBUG1", "" + exchangedAward.getCode());
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
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(mExchangedAward.getTitle());
        Log.v("DEBUG2", "" + mExchangedAward.getCode());
        mTitle = (TextView) mView.findViewById(R.id.qr_title);
        mTitle.setText(mExchangedAward.getTitle());
        mBusiness = (TextView) mView.findViewById(R.id.qr_business);
        mBusiness.setText(mExchangedAward.getCommerceOffering());
        mDate = (TextView) mView.findViewById(R.id.qr_date);
        mDate.setText(mExchangedAward.getCreatedAt().toString());
        mQrcode = (ImageView) mView.findViewById(R.id.qr_qrcode);
        try {
            if (mExchangedAward.getCode() != null) {
                Bitmap bitmap = encodeAsBitmap(mExchangedAward.getCode());
                mQrcode.setImageBitmap(bitmap);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        mTextQrcode = (TextView) mView.findViewById(R.id.qr_textQrcode);
        mTextQrcode.setText(mExchangedAward.getCode());
        mStateText = (TextView) mView.findViewById(R.id.qr_state_text);
        mStateBackground = (LinearLayout) mView.findViewById(R.id.qr_state_background);
        if (mExchangedAward.isUsed()) {
            noEsValida();
        } else {
            esValida();
        }
    }

    private void esValida() {
        mStateText.setText(getString(R.string.exchanged_award_valid));
        mStateBackground.setBackgroundColor(getResources().getColor(R.color.green));
    }

    private void noEsValida() {
        mStateText.setText(getString(R.string.exchanged_award_used));
        mStateBackground.setBackgroundColor(getResources().getColor(R.color.red));
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }

        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.DETAILS_QR_ID;
    }
}
