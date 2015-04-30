package com.globant.eventmanager.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.globant.eventmanager.adapters.EventListAdapterManager;
import com.globant.eventmanager.R;
import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseFragments.BaseEventListFragment;
import com.globant.eventscorelib.baseListeners.GetEventInformation;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.CoreConstants;
import com.software.shell.fab.ActionButton;

import java.util.List;

public class EventListManagerFragment extends BaseEventListFragment implements GetEventInformation {

    private ActionButton mActionButton;
    private List<Event> mEventList;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_manager_event_list;
    }

    @Override
    protected int getEventListRecyclerView() {
        return R.id.event_list_recycler_view;
    }

    public EventListManagerFragment() {
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return this;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateEventView(inflater, container, savedInstanceState);
        prepareRecyclerView(rootView);
        prepareSwipeRefreshLayout(rootView);
        wireUpFAB(rootView);
        return rootView;
    }

    private void prepareRecyclerView(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.event_list_recycler_view);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if ((newState == RecyclerView.SCROLL_STATE_DRAGGING) || (newState == RecyclerView.SCROLL_STATE_SETTLING)) {
                    mActionButton.hide();
                } else {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        mActionButton.show();
                    }
                }
            }
        });
    }

    private void prepareSwipeRefreshLayout(View rootView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.events_manager_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mService.executeAction(BaseService.ACTIONS.EVENT_LIST, true, getBindingKey());
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    private void wireUpFAB(View rootView) {
        mActionButton = (ActionButton) rootView.findViewById(R.id.action_button);
        mActionButton.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        mActionButton.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);
    }

    @Override
    public Activity getBindingActivity() {
        return getActivity();
    }

    @Override
    public String getBindingKey() {
        return CoreConstants.BINDING_KEY_FRAGMENT_MANAGER_EVENT_LIST;
    }

    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {
        showProgressOverlay();
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
        ScrollUtils.addOnGlobalLayoutListener(mRecyclerView, new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(1);

            }
        });
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }

    private void setAdapterRecyclerView() {
        EventListAdapterManager adapter = new EventListAdapterManager(mEventList, getActivity(), this);
        mRecyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setService(BaseService service) {
        super.setService(service);
        mService.executeAction(BaseService.ACTIONS.EVENT_LIST, true, getBindingKey());
    }

    @Override
    public void getEvent(int position) {
      Event event = mEventList.get(position);
        BaseApplication.getInstance().setEvent(event);
        showProgressOverlay();
    }
}