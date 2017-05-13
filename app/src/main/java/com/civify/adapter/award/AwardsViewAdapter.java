package com.civify.adapter.award;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.civify.R;
import com.civify.model.award.Award;

import java.util.List;

public class AwardsViewAdapter extends RecyclerView.Adapter<AwardsViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Award> mRewardList;

    public AwardsViewAdapter(Context context, List<Award> rewardList) {
        this.mContext = context;
        this.mRewardList = rewardList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rewards_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Award award = mRewardList.get(position);
        holder.getTitle().setText(award.getTitle());
        holder.getCommerceOffering().setText(award.getCommerceOffering());
        holder.getPrice().setText(String.valueOf(award.getPrice()));

        String imageUrl = award.getPicture().getSmallUrl();
        Glide.with(mContext)
                .load(imageUrl)
                .centerCrop()
                .into(holder.getAwardImage());

        OnClickListener showDetails = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: show award details
            }
        };

        holder.getAwardImage().setOnClickListener(showDetails);
        holder.getView().setOnClickListener(showDetails);
    }

    @Override
    public int getItemCount() {
        return mRewardList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle, mBusiness, mCoins;
        private ImageView mRewardImage;
        private View mView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTitle = (TextView) itemView.findViewById(R.id.card_view_rewards_title);
            mBusiness = (TextView) itemView.findViewById(R.id.card_view_rewards_subtitle);
            mCoins = (TextView) itemView.findViewById(R.id.card_view_rewards_coins);
            mRewardImage = (ImageView) itemView.findViewById(R.id.card_view_rewards_image);
        }

        public TextView getTitle() {
            return mTitle;
        }

        public TextView getCommerceOffering() {
            return mBusiness;
        }

        public TextView getPrice() {
            return mCoins;
        }

        public ImageView getAwardImage() {
            return mRewardImage;
        }

        public View getView() {
            return mView;
        }
    }
}
