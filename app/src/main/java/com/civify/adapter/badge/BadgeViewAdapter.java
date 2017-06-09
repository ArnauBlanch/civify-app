package com.civify.adapter.badge;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.civify.R;
import com.civify.model.achievement.Achievement;
import com.civify.model.badge.Badge;
import com.civify.model.event.Event;
import com.civify.service.achievement.AchievementSimpleCallback;
import com.civify.service.event.EventSimpleCallback;
import com.civify.utils.AdapterFactory;

import java.util.Calendar;
import java.util.List;

public class BadgeViewAdapter extends RecyclerView.Adapter<BadgeViewAdapter.BadgeViewHolder> {

    private static final char SLASH = '/';
    private static final char SPACE = ' ';

    private List<Badge> mItems;
    private Context mContext;

    public BadgeViewAdapter(List<Badge> items, Context context) {
        mItems = items;
        mContext = context;
    }

    @Override
    public BadgeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_badge, parent,
                false);
        return new BadgeViewHolder(v, mContext);
    }

    @Override
    public void onBindViewHolder(BadgeViewHolder holder, int position) {
        Badge badge = mItems.get(position);
        holder.setBadge(badge);
        Glide.with(mContext)
                .load(badge.getUrl())
                .centerCrop()
                .into(holder.getImage());
        holder.getTitle().setText(badge.getTitle());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(badge.getObtainedDate());
        holder.getDate().setText(mContext.getResources().getString(R.string.obtained_at) + SPACE
                + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + SLASH
                + String.valueOf(calendar.get(Calendar.MONTH)) + SLASH
                + String.valueOf(calendar.get(Calendar.YEAR)));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class BadgeViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private static final String ACHIEVEMENT = "Achievement";

        private final ImageView mImage;
        private final TextView mTitle;
        private final TextView mDate;

        private Badge mBadge;
        private Context mContext;

        public BadgeViewHolder(View itemView, Context context) {
            super(itemView);
            mContext = context;
            mImage = (ImageView) itemView.findViewById(R.id.badge_item_image) ;
            mTitle = (TextView) itemView.findViewById(R.id.badge_item_title);
            mDate = (TextView) itemView.findViewById(R.id.badge_item_date);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        public ImageView getImage() {
            return mImage;
        }

        public TextView getTitle() {
            return mTitle;
        }

        public TextView getDate() {
            return mDate;
        }

        public Badge getBadge() {
            return mBadge;
        }

        public void setBadge(Badge badge) {
            mBadge = badge;
        }

        @Override
        public void onClick(final View v) {
            if (mBadge.getCorrespondsToType().equals(ACHIEVEMENT)) {
                AdapterFactory.getInstance().getAchievementAdapter(mContext)
                        .getAchievement(mBadge.getCorrespondsToToken(),
                                new AchievementSimpleCallback() {
                                    @Override
                                    public void onSucces(Achievement achievement) {
                                        achievement.showAchievementDetails();
                                    }

                                    @Override
                                    public void onFailure() {
                                        Snackbar.make(v, R.string.couldnt_retrieve_achievement,
                                                Snackbar.LENGTH_SHORT);
                                    }
                                });
            } else {
                AdapterFactory.getInstance().getEventAdapter(mContext).getEvent(mBadge
                        .getCorrespondsToToken(), new EventSimpleCallback() {
                            @Override
                            public void onSucces(Event event) {
                                event.showEventDetails();
                            }

                            @Override
                            public void onFailure() {
                                Snackbar.make(v, R.string.couldnt_retrieve_event,
                                        Snackbar.LENGTH_SHORT);
                            }
                        });
            }
        }
    }
}
