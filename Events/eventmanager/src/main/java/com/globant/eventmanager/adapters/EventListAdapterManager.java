package com.globant.eventmanager.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.globant.eventscorelib.baseAdapters.BaseEventsListAdapter;
import com.globant.eventscorelib.baseAdapters.BaseEventsListViewHolder;
import com.globant.eventscorelib.domainObjects.Event;

import java.util.List;

public class EventListAdapterManager extends BaseEventsListAdapter {

    Context mContext;
    Fragment mFragment;

    public EventListAdapterManager(List<Event> eventList, Context context, Fragment fragment) {
        super(eventList, context);
        mContext = context;
        mFragment = fragment;
    }

    @Override
    protected BaseEventsListViewHolder getViewHolder(View view) {
        return new EventsListViewHolderManager(view, mContext, mFragment);
    }

}
