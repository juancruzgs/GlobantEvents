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
import com.globant.eventscorelib.baseFragments.BaseEventListFragment;
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

    @Override
    public BaseService.ActionListener getActionListener() {
        return this;
    }

    public EventListClientFragment() {
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateEventView(inflater, container, savedInstanceState);
        prepareRecyclerView(rootView);
        prepareSwipeRefreshLayout(rootView);
        return rootView;
    }

    private void prepareRecyclerView(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.event_list_recycler_view);
    }

    private void prepareSwipeRefreshLayout(View rootView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.events_client_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                boolean isGlober = SharedPreferencesController.isGlober(getActivity());
                mService.executeAction(BaseService.ACTIONS.EVENT_LIST, isGlober, getBindingKey());
                mSwipeRefreshLayout.setRefreshing(true);
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

    private void setAdapterRecyclerView() {
        EventsListAdapterClient adapter = new EventsListAdapterClient(mEventList, getActivity());
        mRecyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }

    @Override
    public void setService(BaseService service) {
        super.setService(service);
        boolean isGlober = SharedPreferencesController.isGlober(getActivity());
        mService.executeAction(BaseService.ACTIONS.EVENT_LIST, isGlober, getBindingKey());
        showProgressOverlay();
    }
}