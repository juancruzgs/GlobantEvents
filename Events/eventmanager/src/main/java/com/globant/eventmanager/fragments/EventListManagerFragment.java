package com.globant.eventmanager.fragments;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.globant.eventmanager.adapters.EventListAdapterManager;
import com.globant.eventmanager.R;
import com.globant.eventscorelib.baseAdapters.BaseEventsListAdapter;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseFragments.BaseEventListFragment;
import com.globant.eventscorelib.utils.CoreConstants;
import com.software.shell.fab.ActionButton;


public class EventListManagerFragment extends BaseEventListFragment {

    private ActionButton mActionButton;

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
        return CoreConstants.BINDING_KEY_FRAGMENT_MANAGER_EVENT_LIST;
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        super.onFinishAction(theAction, result);
        ScrollUtils.addOnGlobalLayoutListener(getRecyclerView(), new Runnable() {
            @Override
            public void run() {
                getRecyclerView().smoothScrollToPosition(1);
            }
        });
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateEventView(inflater, container, savedInstanceState);
        prepareRecyclerView();
        wireUpFAB(rootView);
        return rootView;
    }

    private void prepareRecyclerView() {
        getRecyclerView().setOnScrollListener(new RecyclerView.OnScrollListener() {
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
}