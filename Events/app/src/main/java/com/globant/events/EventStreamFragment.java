package com.globant.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.BaseEventListFragment;

/**
* Created by paula.baudo on 4/17/2015.
*/
public class EventStreamFragment extends BaseEventListFragment {
    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_client_event_list;
    }

    @Override
    protected int getEventListRecyclerView() {
        return R.id.event_list_recycler_view;
    }

    public EventStreamFragment() {
    }
//
//    @Override
//    public View onCreateEventView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_client_event_list, container, false);
//        hideUtilsAndShowContentOverlay();
//        return rootView;
//    }

}
