package com.globant.events;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.BaseEventListFragment;
import com.globant.eventscorelib.BaseEventsListAdapter;

/**
* Created by paula.baudo on 4/17/2015.
*/
public class EventListClientFragment extends BaseEventListFragment {
    private RecyclerView mRecyclerView;
    private BaseEventsListAdapter mAdapter;
    private String[] mDataset;
    private static final int DATASET_COUNT = 60;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_client_event_list;
    }

    @Override
    protected int getEventListRecyclerView() {
        return R.id.event_list_recycler_view;
    }

    public EventListClientFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateEventView(inflater, container, savedInstanceState);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.event_list_recycler_view);
        mAdapter = new EventsListAdapterClient(mDataset, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    protected void initDataset() {
        mDataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataset[i] = "La Fiesta del Chorizo";
        }
    }
}
