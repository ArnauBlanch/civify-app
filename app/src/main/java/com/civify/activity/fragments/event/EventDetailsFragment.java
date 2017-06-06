package com.civify.activity.fragments.event;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;
import com.civify.model.event.Event;

import java.util.Locale;

import org.ocpsoft.prettytime.PrettyTime;

public class EventDetailsFragment extends BasicFragment {

    private static final String DEBUG = "debug-event";

    private static final String TAG_EVENT = "event";
    private static final int PERCENT = 100;

    private Event mEvent;
    private View mView;

    public EventDetailsFragment() {
    }

    public static EventDetailsFragment newInstance(@NonNull Event event) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(TAG_EVENT, event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_event_details, container, false);
        init();
        return mView;
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.DETAILS_EVENTS_ID;
    }

    private void init() {
        Log.d(DEBUG, "init");

        Log.d(DEBUG, "Getting arguments from bundle");
        Bundle bundle = getArguments();
        mEvent = (Event) bundle.getSerializable(TAG_EVENT);

        Log.d(DEBUG, "Setting title toolbar");
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(mEvent.getTitle());

        addEventPicture();
        addEventBadge();
        addEventTitle();
        addEventTime();
        addEventDuration();
        addEventCoins();
        addEventXp();
        addEventDescription();
        addEventProgress();

        Log.d(DEBUG, "init finished");
    }

    private void addEventPicture() {
        Log.d(DEBUG, "Adding picture...");
        ImageView pictureEvent = (ImageView) mView.findViewById(R.id.details_event_picture);
        Glide.with(this)
                .load(mEvent.getPicture().getLargeUrl())
                .centerCrop()
                .into(pictureEvent);
    }

    private void addEventBadge() {
        Log.d(DEBUG, "Adding badge...");
        ImageView badgeEvent = (ImageView) mView.findViewById(R.id.details_event_badge);
        Glide.with(this)
                .load(mEvent.getBadge().getLargeUrl())
                .centerCrop()
                .into(badgeEvent);
    }

    private void addEventTitle() {
        Log.d(DEBUG, "Adding title...");
        TextView title = (TextView) mView.findViewById(R.id.details_event_title);
        title.setText(mEvent.getTitle());
        title.setMovementMethod(new ScrollingMovementMethod());
    }

    private void addEventTime() {
        Log.d(DEBUG, "Adding time...");
        TextView time = (TextView) mView.findViewById(R.id.details_event_time);
        if (mEvent.isHappening()) {
            time.setText(getContext().getString(R.string.its_happening_now));
        } else {
            PrettyTime prettyTime = new PrettyTime(Locale.ENGLISH);
            time.setText(prettyTime.format(mEvent.getStartDate()));
        }
    }

    private void addEventDuration() {
        Log.d(DEBUG, "Adding duration...");
        TextView duration = (TextView) mView.findViewById(R.id.details_event_duration);
        String text = mEvent.getDuration(getContext());
        duration.setText(text);
    }

    private void addEventCoins() {
        Log.d(DEBUG, "Adding coins...");
        TextView coins = (TextView) mView.findViewById(R.id.details_event_coins);
        coins.setText(String.valueOf(mEvent.getCoins()));
    }

    private void addEventXp() {
        Log.d(DEBUG, "Adding xp...");
        TextView xp = (TextView) mView.findViewById(R.id.details_event_xp);
        xp.setText(mEvent.getXp() + " xp");
    }

    private void addEventDescription() {
        Log.d(DEBUG, "Adding description...");
        TextView description = (TextView) mView.findViewById(R.id.details_event_description);
        description.setText(mEvent.getDescription());
        description.setMovementMethod(new ScrollingMovementMethod());
    }

    private void addEventProgress() {
        Log.d(DEBUG, "Adding progress...");
        TextView progressTotal = (TextView) mView.findViewById(R.id
                .details_event_progressTotal);
        progressTotal.setText(mEvent.getProgress() + "/" + mEvent.getNumber());
        int progress = (mEvent.getProgress() * PERCENT) / mEvent.getNumber();
        ProgressBar progressBar = (ProgressBar) mView.findViewById(R.id
                .details_event_progressBar);
        progressBar.setProgress(progress);
        TextView progressText =
                (TextView) mView.findViewById(R.id.details_event_progressText);
        progressText.setText(progress + "%");
    }
}
