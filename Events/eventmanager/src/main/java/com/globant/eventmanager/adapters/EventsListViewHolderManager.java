package com.globant.eventmanager.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.globant.eventmanager.activities.EventDetailManagerActivity;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseAdapters.BaseEventsListViewHolder;

public class EventsListViewHolderManager extends BaseEventsListViewHolder {

    @Override
    protected Class<? extends BasePagerActivity> getActivityClass() {
        return EventDetailManagerActivity.class;
    }

    public EventsListViewHolderManager(View itemView, Context context, Fragment fragment) {
        super(itemView, context, fragment);
    }
}
