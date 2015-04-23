package com.globant.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.baseComponents.BaseEventsListAdapter;

/**
 * Created by paula.baudo on 4/17/2015.
 */
public class EventsListAdapterClient extends BaseEventsListAdapter {
    Context mContext;

    public EventsListAdapterClient(String[] dataSet, Context context) {
        super(dataSet);
        mContext = context;
    }

    @Override
    public EventsListViewHolderClient onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card_row_item_technical, parent, false);
        return new EventsListViewHolderClient(view, mContext);
    }
}
