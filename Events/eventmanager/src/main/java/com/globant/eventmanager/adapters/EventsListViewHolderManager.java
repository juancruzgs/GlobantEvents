package com.globant.eventmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.globant.eventmanager.activities.EventDetailManagerActivity;
import com.globant.eventscorelib.baseAdapters.BaseEventsListViewHolder;
import com.globant.eventscorelib.baseListeners.GetEventInformation;

public class EventsListViewHolderManager extends BaseEventsListViewHolder {

    public EventsListViewHolderManager(final View itemView, final Context context, final Fragment fragment) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetEventInformation getEventInformation = (GetEventInformation) fragment;
                getEventInformation.getEvent((Integer) itemView.getTag());
                Intent intent = new Intent(context, EventDetailManagerActivity.class);
                context.startActivity(intent);
            }
        });
    }
}
