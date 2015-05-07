package com.globant.events.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.globant.events.activities.EventDetailClientActivity;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseAdapters.BaseEventsListViewHolder;

public class EventsListViewHolderClient extends BaseEventsListViewHolder{

    @Override
    protected Class<? extends BasePagerActivity> getActivityClass() {
        return EventDetailClientActivity.class;
    }

    public EventsListViewHolderClient(View itemView, Context context, Fragment fragment) {
        super(itemView, context, fragment);
    }
}
