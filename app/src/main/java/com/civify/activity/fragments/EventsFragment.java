package com.civify.activity.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.adapter.event.EventAdapter;
import com.civify.model.event.Event;
import com.civify.service.event.ListEventsSimpleCallback;
import com.civify.utils.AdapterFactory;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends BasicFragment {

    private static final String TAG_TEST_EVENT = "tag_test_event";

    public EventsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NavigateFragment.
     */
    public static EventsFragment newInstance() {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        //test();
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    private void test() {
        AdapterFactory adapterFactory = AdapterFactory.getInstance();
        EventAdapter eventAdapter = adapterFactory.getEventAdapter(getContext());
        eventAdapter.getEvents(new ListEventsSimpleCallback() {
            @Override
            public void onSuccess(List<Event> events) {
                printEvents(events);
            }

            @Override
            public void onFailure() {
                Log.d(TAG_TEST_EVENT, "FAIL");
            }
        });
    }

    private void printEvents(List<Event> events) {
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            Log.d(TAG_TEST_EVENT, "Event number " + (i + 1));
            Log.d(TAG_TEST_EVENT, "Title: " + event.getTitle());
            Log.d(TAG_TEST_EVENT, "Description: " + event.getDescription());
            Log.d(TAG_TEST_EVENT, "Number: " + event.getNumber());
            Log.d(TAG_TEST_EVENT, "Coins: " + event.getCoins());
            Log.d(TAG_TEST_EVENT, "XP: " + event.getXp());
            Log.d(TAG_TEST_EVENT, "Created at: " + event.getCreatedAt());
            Log.d(TAG_TEST_EVENT, "Updated at: " + event.getUpdatedAt());
            Log.d(TAG_TEST_EVENT, "Kind: " + event.getKind());
            Log.d(TAG_TEST_EVENT, "Event_token: " + event.getToken());
            Log.d(TAG_TEST_EVENT, "Start date: " + event.getStartDate());
            Log.d(TAG_TEST_EVENT, "End date: " + event.getEndDate());
            Log.d(TAG_TEST_EVENT, "Enabled: " + event.isEnabled());
            Log.d(TAG_TEST_EVENT, "Picture: " + event.getPicture());
            Log.d(TAG_TEST_EVENT, "Badge: " + event.getBadge());
        }
    }
}
