package com.civify.adapter.reward;

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
import com.civify.model.reward.RewardStub;

import java.util.List;

public class RewardsViewAdapter extends RecyclerView.Adapter<RewardsViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<RewardStub> mRewardList;

    public RewardsViewAdapter(Context context, List<RewardStub> rewardList) {
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
        final RewardStub reward = mRewardList.get(position);
        holder.getTitle().setText(reward.getTitle());
        holder.getBusiness().setText(reward.getBusiness());
        holder.getCoins().setText(String.valueOf(reward.getCoins()));

        String imageUrl = "http://www.sportlife"
                + ".es/media/cache/original/upload/images/article/10489/article-swolf-eficiencia"
                + "-natacion-55632e1909028.jpg";
        Glide.with(mContext)
                .load(imageUrl)
                .centerCrop()
                .into(holder.getRewardImage());

        OnClickListener showDetails = new OnClickListener() {
            @Override
            public void onClick(View v) {
                reward.showRewardDetails();
            }
        };

        holder.getRewardImage().setOnClickListener(showDetails);
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

        public TextView getBusiness() {
            return mBusiness;
        }

        public TextView getCoins() {
            return mCoins;
        }

        public ImageView getRewardImage() {
            return mRewardImage;
        }

        public View getView() {
            return mView;
        }
    }
}
