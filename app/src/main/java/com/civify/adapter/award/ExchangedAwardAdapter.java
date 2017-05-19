package com.civify.adapter.award;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.civify.R;
import com.civify.model.award.ExchangedAward;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ExchangedAwardAdapter extends RecyclerView.Adapter<ExchangedAwardAdapter
        .MyViewHolder> {

    private static final int AWARD_USED = R.string.exchanged_award_used;
    private static final int AWARD_VALID = R.string.exchanged_award_valid;
    private static final int COLOR_VALID = R.drawable.green_bg_button;
    private static final int COLOR_USED = R.drawable.red_bg_button;
    private static final int EXCHANGED_ON = R.string.exchanged_on;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private List<ExchangedAward> mExchangedAwards;
    private Context mContext;
    private SimpleDateFormat mSimpleDateFormat;

    public ExchangedAwardAdapter(List<ExchangedAward> awards, Context context) {
        mExchangedAwards = awards;
        mContext = context;
        mSimpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rewards_exchanged_view_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setExchangedAward(mExchangedAwards.get(position));

        ExchangedAward exchangedAward = mExchangedAwards.get(position);
        // Title
        holder.getTitle().setText(exchangedAward.getTitle());
        // Commerce
        holder.getCommerce().setText(exchangedAward.getCommerceOffering());
        // Date
        String exchangedDate = mContext.getString(EXCHANGED_ON) + ' ' + mSimpleDateFormat.format(
                exchangedAward.getCreatedAt());
        holder.getExchangedDate().setText(exchangedDate);
        // USED/VALID indicator
        holder.getExchanged().setText(mContext.getString(exchangedAward.isUsed()
                ? AWARD_VALID : AWARD_USED));
        // Indicator background
        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
            holder.getExchanged()
                    .setBackground(ContextCompat.getDrawable(mContext,
                            exchangedAward.isUsed() ? COLOR_VALID : COLOR_USED));
        }
    }

    @Override
    public int getItemCount() {
        return mExchangedAwards.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private TextView mTitle, mCommerce, mExchangedDate, mExchanged;
        private ExchangedAward mExchangedAward;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.reward_exchanged_title);
            mCommerce = (TextView) itemView.findViewById(R.id.reward_exchanged_commerce_name);
            mExchangedDate = (TextView) itemView.findViewById(R.id.reward_exchanged_date);
            mExchanged = (TextView) itemView.findViewById(R.id.reward_exchanged_indicator);
            // On click listener
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        public TextView getTitle() {
            return mTitle;
        }

        public TextView getCommerce() {
            return mCommerce;
        }

        public TextView getExchangedDate() {
            return mExchangedDate;
        }

        public TextView getExchanged() {
            return mExchanged;
        }

        public void setExchangedAward(ExchangedAward exchangedAward) {
            mExchangedAward = exchangedAward;
        }

        @Override
        public void onClick(View v) {
            // TODO: show exchanged award details (code)
            //mExchangedAward.showDetails();
        }
    }
}
