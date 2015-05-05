package com.globant.events.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
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
    protected EventsListViewHolderClient getViewHolder(View view) {
        return new EventsListViewHolderClient(view, mContext, mFragment);
    }
}
