package com.civify.activity.fragments.event;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.civify.R;
import com.civify.activity.DrawerActivity;
import com.civify.activity.fragments.BasicFragment;
import com.civify.adapter.event.EventViewAdapter;
import com.civify.model.event.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventViewFragment extends BasicFragment {

    private List<Event> mEventList;
    private EventViewAdapter mEventViewAdapter;
    private EventsFragment mEventsFragment;

    public EventViewFragment() {
        mEventList = new ArrayList<>();
    }

    public static EventViewFragment newInstance() {
        EventViewFragment fragment = new EventViewFragment();
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

        mEventViewAdapter = new EventViewAdapter(mEventList, mEventsFragment);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_view, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.event_recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mEventViewAdapter);

        return view;
    }

    public void setEventsList(List<Event> items) {
        mEventList.clear();
        ArrayList<Event> intermediateList = new ArrayList<>(items);
        Collections.reverse(intermediateList);
        mEventList.addAll(intermediateList);

        mEventViewAdapter.notifyDataSetChanged();
    }

    public void setEventsFragment(EventsFragment eventsFragment) {
        mEventsFragment = eventsFragment;
    }
}
