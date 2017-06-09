package com.civify.activity.fragments.event;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;
import com.civify.model.event.Event;
import com.civify.service.event.ListEventsSimpleCallback;
import com.civify.utils.AdapterFactory;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends BasicFragment {

    private static final String TAG_TEST_EVENT = "tag_test_event";

    private ProgressBar mProgressBar;
    private EventViewFragment mEventViewFragment;

    public EventsFragment() {
    }

    public static EventsFragment newInstance() {
        return new EventsFragment();
    }

    @Override
    public int getFragmentId() {
        return DrawerActivity.EVENTS_ID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.loading_events);
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        mEventViewFragment = new EventViewFragment();
        mEventViewFragment.setEventsFragment(this);
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.events_container, mEventViewFragment)
                .commit();
        AdapterFactory.getInstance().getEventAdapter(getContext())
                .getEvents(new ListEventsSimpleCallback() {
                    @Override
                    public void onSuccess(List<Event> events) {
                        mProgressBar.setVisibility(View.GONE);
                        mEventViewFragment.setEventsList(validEvents(events));
                    }

                    @Override
                    public void onFailure() {
                        Snackbar.make(view, "Couldn't retrieve events.",
                                Snackbar.LENGTH_SHORT);
                    }
                });
    }

    public void updateList() {
        AdapterFactory.getInstance().getEventAdapter(getContext())
                .getEvents(new ListEventsSimpleCallback() {
                    @Override
                    public void onSuccess(List<Event> events) {
                        mEventViewFragment.setEventsList(validEvents(events));
                    }

                    @Override
                    public void onFailure() {

                    }
                });
    }

    private static List<Event> validEvents(List<Event> oldEvents) {
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < oldEvents.size(); ++i) {
            Event event = oldEvents.get(i);
            if (event.isEnabled() && isShowableEvent(event)) {
                events.add(event);
            }
        }
        return events;
    }

    private static boolean isShowableEvent(Event event) {
        return !event.isPast() || isPastCompletedEvent(event);
    }

    private static boolean isPastCompletedEvent(Event event) {
        return event.isPast() && event.isCompleted() && !event.isClaimed();
    }
}
