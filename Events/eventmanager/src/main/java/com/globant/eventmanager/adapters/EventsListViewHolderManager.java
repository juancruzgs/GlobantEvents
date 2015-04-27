package com.globant.eventmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.globant.eventmanager.activities.EventDetailManagerActivity;
import com.globant.eventscorelib.baseAdapters.BaseEventsListViewHolder;

/**
 * Created by paula.baudo on 4/17/2015.
 */
public class EventsListViewHolderManager extends BaseEventsListViewHolder {

    public EventsListViewHolderManager(View itemView, final Context context) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventDetailManagerActivity.class);
                context.startActivity(intent);
            }
        });
    }
}
