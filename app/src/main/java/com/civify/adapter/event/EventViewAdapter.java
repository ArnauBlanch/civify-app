package com.civify.adapter.event;

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
import com.civify.model.event.Event;

import java.util.List;
import java.util.Locale;

import org.ocpsoft.prettytime.PrettyTime;

public class EventViewAdapter extends RecyclerView.Adapter<EventViewAdapter
        .EventViewHolder> {

    private List<Event> mItems;
    private Context mContext;

    public EventViewAdapter(List<Event> items, Context context) {
        mItems = items;
        mContext = context;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event,
                parent, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = mItems.get(position);
        holder.setEvent(event);
        Glide.with(mContext)
                .load(event.getBadge().getLargeUrl())
                .centerCrop()
                .into(holder.getBadge());
        Glide.with(mContext)
                .load(event.getPicture().getLargeUrl())
                .centerCrop()
                .into(holder.getPicture());
        holder.getTitle().setText(event.getTitle());
        holder.getDescription().setText(event.getDescription());
        if (event.isHappening()) {
            holder.getTime().setText(mContext.getString(R.string.its_happening_now));
        } else {
            PrettyTime prettyTime = new PrettyTime(Locale.ENGLISH);
            holder.getTime().setText(prettyTime.format(event.getStartDate()));
        }
        holder.getDuration().setText(event.getDuration(mContext));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder
            implements OnClickListener {

        private final ImageView mPicture;
        private final ImageView mBadge;
        private final TextView mTitle;
        private final TextView mDescription;
        private final TextView mTime;
        private final TextView mDuration;

        private Event mEvent;

        public EventViewHolder(View itemView) {
            super(itemView);
            mPicture = (ImageView) itemView.findViewById(R.id.item_event_picture);
            mBadge = (ImageView) itemView.findViewById(R.id.item_event_badge);
            mTitle = (TextView) itemView.findViewById(R.id.item_event_title);
            mDescription = (TextView) itemView.findViewById(R.id.item_event_description);
            mTime = (TextView) itemView.findViewById(R.id.item_event_time);
            mDuration = (TextView) itemView.findViewById(R.id.item_event_duration);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        public ImageView getPicture() {
            return mPicture;
        }

        public ImageView getBadge() {
            return mBadge;
        }

        public TextView getTitle() {
            return mTitle;
        }

        public TextView getDescription() {
            return mDescription;
        }

        public TextView getTime() {
            return mTime;
        }

        public TextView getDuration() {
            return mDuration;
        }

        public Event getEvent() {
            return mEvent;
        }

        public void setEvent(Event event) {
            mEvent = event;
        }

        @Override
        public void onClick(View v) {
            mEvent.showEventDetails();
        }
    }
}
