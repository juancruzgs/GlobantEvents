package com.globant.eventmanager.fragments;

import com.globant.eventmanager.R;
import com.globant.eventmanager.adapters.EventHistoryParticipantsListAdapterManager;
import com.globant.eventscorelib.baseAdapters.BaseParticipantsListAdapter;
import com.globant.eventscorelib.baseFragments.BaseParticipantsFragment;
import com.globant.eventscorelib.domainObjects.Subscriber;

import java.util.List;

public class EventHistoryParticipantsManagerFragment extends BaseParticipantsFragment {
    @Override
    protected void setViewButtonsAddDeclineAllVisibility(List<Subscriber> subscribers) {

    }

    @Override
    protected void initializeAcceptedSubscribers() {

    }

    @Override
    protected BaseParticipantsListAdapter getAdapter() {
        List<Subscriber> subscribers = getSubscribers();
        mAdapter = new EventHistoryParticipantsListAdapterManager(getActivity(), subscribers);
        return mAdapter;
    }

    @Override
    protected void showHint() {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_base_event_participants;
    }

    @Override
    protected void cancelAnimationOnRefresh() {

    }
}
