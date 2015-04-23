package com.globant.eventmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.baseComponents.BaseEventsListAdapter;

public class EventListAdapterManager extends BaseEventsListAdapter {

    Context mContext;

    public EventListAdapterManager(String[] dataSet, Context context) {
        super(dataSet);
        mContext = context;
    }

    @Override
    public EventsListViewHolderManager onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card_row_item, parent, false);
        return new EventsListViewHolderManager(view, mContext);
    }
}
