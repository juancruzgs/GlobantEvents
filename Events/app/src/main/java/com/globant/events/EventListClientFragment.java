package com.globant.events;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.baseComponents.BaseEventListFragment;
import com.globant.eventscorelib.baseComponents.BaseEventsListAdapter;
import com.globant.eventscorelib.baseComponents.BaseService;

/**
* Created by paula.baudo on 4/17/2015.
*/
public class EventListClientFragment extends BaseEventListFragment {
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
    public BaseService.ActionListener getActionListener() {
        return null;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateEventView(inflater, container, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.event_list_recycler_view);
        BaseEventsListAdapter adapter = new EventsListAdapterClient(mDataset, getActivity());
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    protected void initDataset() {
        mDataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataset[i] = "Curso de Java";
        }
    }
}
