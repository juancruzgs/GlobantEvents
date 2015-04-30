package com.globant.events.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.events.adapters.EventsListAdapterClient;
import com.globant.events.R;
import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseFragments.BaseEventListFragment;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseListeners.GetEventInformation;
import com.globant.eventscorelib.controllers.SharedPreferencesController;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.CoreConstants;

import java.util.List;

public class EventListClientFragment extends BaseEventListFragment implements GetEventInformation {
    private List<Event> mEventList;
    private RecyclerView mRecyclerView;

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
        return rootView;
    }

    @Override
    public Activity getBindingActivity() {
        return getActivity();
    }

    @Override
    public String getBindingKey() {
        return CoreConstants.BINDING_KEY_FRAGMENT_CLIENT_EVENT_LIST;
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
        EventsListAdapterClient adapter = new EventsListAdapterClient(mEventList, getActivity(), this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setService(BaseService service) {
        super.setService(service);
        boolean isGlober = SharedPreferencesController.isGlober(getActivity());
        mService.executeAction(BaseService.ACTIONS.EVENT_LIST, isGlober, getBindingKey());
    }

    @Override
    public void getEvent(int position) {
        Event event = mEventList.get(position);
        BaseApplication.getInstance().setEvent(event);
    }
}
