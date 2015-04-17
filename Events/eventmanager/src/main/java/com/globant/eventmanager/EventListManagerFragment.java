package com.globant.eventmanager;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.BaseEventListFragment;
import com.software.shell.fab.ActionButton;
import com.software.shell.fab.FloatingActionButton;

public class EventListManagerFragment extends BaseEventListFragment {

    ActionButton mActionButton;
    RecyclerView mRecyclerView;

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
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateEventView(inflater, container, savedInstanceState);
        wireUpFloatingButton(rootView);
        prepareRecyclerViewTouchListener(rootView);
        wireUpFAB(rootView);
        return rootView;
    }

    private void wireUpFloatingButton(View rootView) {
        mActionButton = (ActionButton)rootView.findViewById(R.id.action_button);
    }

    private void prepareRecyclerViewTouchListener(View rootView) {
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.event_list_recycler_view);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if ((newState == RecyclerView.SCROLL_STATE_DRAGGING) || (newState == RecyclerView.SCROLL_STATE_SETTLING)){
                    mActionButton.hide();
                }else{
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        mActionButton.show();
                    }
                }
            }
        });
    }

    private void wireUpFAB(View rootView) {
        mActionButton = (ActionButton) rootView.findViewById(R.id.action_button);
        mActionButton.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        mActionButton.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);
    }

}

