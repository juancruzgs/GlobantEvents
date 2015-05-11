package com.globant.eventmanager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.globant.eventmanager.activities.PushNotificationActivity;
import com.globant.eventmanager.adapters.EventListAdapterManager;
import com.globant.eventmanager.R;
import com.globant.eventscorelib.baseActivities.BaseCreditsActivity;
import com.globant.eventscorelib.baseActivities.BaseSubscriberActivity;
import com.globant.eventscorelib.baseAdapters.BaseEventsListAdapter;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseFragments.BaseEventListFragment;
import com.globant.eventscorelib.utils.CoreConstants;
import com.software.shell.fab.ActionButton;


public class EventListManagerFragment extends BaseEventListFragment {

    private ActionButton mActionButton;
    private RecyclerView mRecyclerView;

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
            ScrollUtils.addOnGlobalLayoutListener(getRecyclerView(), new Runnable() {
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
        mRecyclerView = getRecyclerView();
        prepareRecyclerView();
        wireUpFAB(rootView);
        return rootView;
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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_event_list_manager, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean handled = false;
        if (id == com.globant.eventscorelib.R.id.action_credits) {
            Intent intentCredits = new Intent(getActivity(), BaseCreditsActivity.class);
            startActivity(intentCredits);
            handled = true;
        } else {
            if (id == com.globant.eventscorelib.R.id.action_profile) {
                Intent intentSubscriber = new Intent(getActivity(), BaseSubscriberActivity.class);
                startActivity(intentSubscriber);
                handled = true;
            } else {
                if (id == com.globant.eventscorelib.R.id.action_checkin) {
                    Intent intentScan = new Intent(CoreConstants.INTENT_SCAN);
                    startActivityForResult(intentScan, 0);
                    handled = true;
                } else {
                    if (id == R.id.action_notifications) {
                        Intent intentNotifications = new Intent(getActivity(), PushNotificationActivity.class);
                        intentNotifications.putExtra(PushNotificationFragment.SOURCE_TAG, this.getClass().getSimpleName());
                        startActivity(intentNotifications);
                        handled = true;
                    }
                }
            }
        }

        if (!handled) {
            handled = super.onOptionsItemSelected(item);
        }
        return handled;
    }
}