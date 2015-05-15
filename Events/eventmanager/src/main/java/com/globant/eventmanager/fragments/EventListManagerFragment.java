package com.globant.eventmanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.globant.eventmanager.R;
import com.globant.eventmanager.activities.EventHistoryManagerActivity;
import com.globant.eventmanager.activities.EventsManagerPagerActivity;
import com.globant.eventmanager.adapters.EventListAdapterManager;
import com.globant.eventscorelib.baseActivities.BaseEventListActivity;
import com.globant.eventscorelib.baseAdapters.BaseEventsListAdapter;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseFragments.BaseEventListFragment;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.CoreConstants;
import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.List;


public class EventListManagerFragment extends BaseEventListFragment {

    private ActionButton mActionButton;

//    class EventList implements Parcelable

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_manager_event_list;
    }

    @Override
    protected boolean getIsGlober() {
        return true;
    }

    @Override
    protected BaseEventsListAdapter getAdapter() {
        return new EventListAdapterManager(getEventList(), getActivity(), this);
    }

    public EventListManagerFragment() {
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return this;
    }

    @Override
    public String getBindingKey() {
        return EventListManagerFragment.class.getSimpleName();
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        super.onFinishAction(theAction, result);
        if (mRecyclerView.getAdapter().getItemCount() > 0) {
            mRecyclerView.scrollToPosition(1);
            ScrollUtils.addOnGlobalLayoutListener(mRecyclerView, new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.smoothScrollToPosition(0);
                }
            });
        }
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateEventView(inflater, container, savedInstanceState);
        prepareRecyclerView();
        wireUpFAB(rootView);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_manager_event_list, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean handled = false;

        if (id == R.id.action_history) {
            ArrayList<Event> events = (ArrayList<Event>) ((BaseEventListActivity)getActivity()).getEventList();
            Intent intentHistory = new Intent(getActivity(), EventHistoryManagerActivity.class);
            intentHistory.putParcelableArrayListExtra(CoreConstants.FIELD_EVENTS, events);

            startActivity(intentHistory);
            handled = true;
        }
        if (!handled) {
            handled = super.onOptionsItemSelected(item);
        }
        return handled;
    }

    private void prepareRecyclerView() {
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

    private void wireUpFAB(View rootView) {
        mActionButton = (ActionButton) rootView.findViewById(R.id.action_button);
        mActionButton.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        mActionButton.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventsManagerPagerActivity.class);
                EventsFragment.mEventAction = EventsFragment.ActionType.CREATE_EVENT;
                startActivity(intent);
            }
        });
    }
}