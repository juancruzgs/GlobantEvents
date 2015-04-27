package com.globant.events.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.events.R;
import com.globant.eventscorelib.baseAdapters.BaseEventsListAdapter;

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
                .inflate(R.layout.event_card_row_item, parent, false);
        return new EventsListViewHolderClient(view, mContext);
    }
}
