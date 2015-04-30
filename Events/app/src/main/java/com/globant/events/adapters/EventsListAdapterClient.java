package com.globant.events.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.events.R;
import com.globant.eventscorelib.baseAdapters.BaseEventsListAdapter;
import com.globant.eventscorelib.domainObjects.Event;

import java.util.List;


public class EventsListAdapterClient extends BaseEventsListAdapter {

    Context mContext;
    Fragment mFragment;

    public EventsListAdapterClient(List<Event> eventList, Context context, Fragment fragment) {
        super(eventList, context);
        mContext = context;
        mFragment = fragment;
    }

    @Override
    public EventsListViewHolderClient onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card_row_item, parent, false);
        return new EventsListViewHolderClient(view, mContext, mFragment);
    }
}
