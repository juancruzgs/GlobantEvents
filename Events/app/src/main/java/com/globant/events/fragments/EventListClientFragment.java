package com.globant.events.fragments;

import com.globant.events.adapters.EventsListAdapterClient;
import com.globant.events.R;
import com.globant.eventscorelib.baseAdapters.BaseEventsListAdapter;
import com.globant.eventscorelib.baseFragments.BaseEventListFragment;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.controllers.SharedPreferencesController;
import com.globant.eventscorelib.utils.CoreConstants;


public class EventListClientFragment extends BaseEventListFragment {
    @Override
    public void getEvent(String eventId) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_client_event_list;
    }

    @Override
    protected boolean getIsGlober() {
        return SharedPreferencesController.isGlober(getActivity());
    }

    @Override
    protected BaseEventsListAdapter getAdapter() {
        return new EventsListAdapterClient(getEventList(), getActivity(), this);
    }

    public EventListClientFragment() {
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return null;
    }

}
