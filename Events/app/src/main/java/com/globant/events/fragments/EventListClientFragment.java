package com.globant.events.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.events.adapters.EventsListAdapterClient;
import com.globant.events.R;
import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseFragments.BaseEventListFragment;
import com.globant.eventscorelib.baseAdapters.BaseEventsListAdapter;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.controllers.SharedPreferencesController;
import com.globant.eventscorelib.domainObjects.Event;

import java.util.List;

public class EventListClientFragment extends BaseEventListFragment {

    private List<Event> mEventList;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

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
    public BaseService.ActionListener getActionListener() {
        return this;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateEventView(inflater, container, savedInstanceState);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.event_list_recycler_view);
        prepareSwipeRefreshLayout(rootView);
        return rootView;
    }

    private void prepareSwipeRefreshLayout(View rootView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.events_client_swipe);
        mSwipeRefreshLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.executeAction(BaseService.ACTIONS.EVENT_LIST, true, getBindingKey());
            }
        });
    }

    @Override
    public Activity getBindingActivity() {
        return getActivity();
    }

    @Override
    public String getBindingKey() {
        return "EventListClientFragment";
    }

    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        switch (theAction) {
            case EVENT_LIST:
                mEventList = (List<Event>) result;
                if (mEventList != null) {
                    setAdapterRecyclerView();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    showErrorOverlay();
                }
                break;
        }
        hideUtilsAndShowContentOverlay();
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }


    private void setAdapterRecyclerView() {
        EventsListAdapterClient adapter = new EventsListAdapterClient(mEventList, getActivity());
        mRecyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setService(BaseService service) {
        super.setService(service);
        boolean isGlober = SharedPreferencesController.isGlober(getActivity());
        mService.executeAction(BaseService.ACTIONS.EVENT_LIST, isGlober, getBindingKey());
    }
}
