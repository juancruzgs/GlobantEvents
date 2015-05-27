package com.globant.eventmanager.adapters;

import android.content.Context;
import android.view.View;

import com.globant.eventscorelib.baseAdapters.BaseParticipantListViewHolder;
import com.globant.eventscorelib.baseAdapters.BaseParticipantsListAdapter;
import com.globant.eventscorelib.domainObjects.Subscriber;

import java.util.List;

public class EventHistoryParticipantsListAdapterManager extends BaseParticipantsListAdapter{

    public EventHistoryParticipantsListAdapterManager(Context context, List<Subscriber> subscribers) {
        super(context, subscribers);
    }

    @Override
    protected BaseParticipantListViewHolder getViewHolder(View view) {
        return new EventHistoryParticipantsViewHolderManager(view);
    }
}
