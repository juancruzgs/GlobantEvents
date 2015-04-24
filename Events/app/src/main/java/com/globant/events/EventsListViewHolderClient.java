package com.globant.events;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.globant.eventscorelib.baseComponents.BaseEventsListViewHolder;

/**
 * Created by paula.baudo on 4/17/2015.
 */
public class EventsListViewHolderClient extends BaseEventsListViewHolder{

        public EventsListViewHolderClient(View itemView, final Context context) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EventDetailClientActivity.class);
                    context.startActivity(intent);
                }
            });
        }
}