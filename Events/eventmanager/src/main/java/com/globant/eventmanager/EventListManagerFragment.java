package com.globant.eventmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseComponents.BaseEventListFragment;
import com.globant.eventscorelib.baseComponents.BaseEventsListAdapter;
import com.software.shell.fab.ActionButton;

public class EventListManagerFragment extends BaseEventListFragment {

    private ActionButton mActionButton;
    private String[] mDataset;
    ObservableRecyclerView mRecyclerView;
    private static final int DATASET_COUNT = 10;

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
        wireUpFloatingButton(rootView);
        prepareRecyclerViewTouchListener(rootView);
        wireUpFAB(rootView);
        return rootView;
    }

    private void wireUpFloatingButton(View rootView) {
        mActionButton = (ActionButton)rootView.findViewById(R.id.action_button);
    }

    private void prepareRecyclerViewTouchListener(View rootView) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.event_list_recycler_view);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
        BaseEventsListAdapter adapter = new EventListAdapterManager(mDataset, getActivity());
        recyclerView.setAdapter(adapter);
    }

    protected void initDataset() {
        mDataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataset[i] = "Curso de Java";
        }
    }

    private void wireUpFAB(View rootView) {
        mActionButton = (ActionButton) rootView.findViewById(R.id.action_button);
        mActionButton.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        mActionButton.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);
    }

}

