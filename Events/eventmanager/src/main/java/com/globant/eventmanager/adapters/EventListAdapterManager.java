package com.globant.eventmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventmanager.R;
import com.globant.eventscorelib.baseAdapters.BaseEventsListAdapter;
import com.globant.eventscorelib.domainObjects.Event;

import java.util.List;

public class EventListAdapterManager extends BaseEventsListAdapter {

    Context mContext;

    public EventListAdapterManager(List<Event> eventList, Context context) {
        super(eventList, context);
        mContext = context;
    }

    @Override
    public EventsListViewHolderManager onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card_row_item, parent, false);
        return new EventsListViewHolderManager(view, mContext);
    }
}
