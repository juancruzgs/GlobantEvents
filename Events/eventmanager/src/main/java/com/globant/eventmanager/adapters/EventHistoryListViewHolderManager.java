package com.globant.eventmanager.adapters;

import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.globant.eventmanager.R;
import com.globant.eventscorelib.baseAdapters.GetEventInformation;

public class EventHistoryListViewHolderManager extends RecyclerView.ViewHolder{
    private AppCompatTextView mEventTitle;
    private AppCompatTextView mEventDate;
    private AppCompatTextView mEventCountry;
    private AppCompatTextView mEventId;

    public EventHistoryListViewHolderManager (View v, final Fragment fragment) {
        super (v);
        mEventTitle = (AppCompatTextView) v.findViewById(R.id.text_view_event_title);
        mEventDate = (AppCompatTextView) v.findViewById(R.id.text_view_event_date);
        mEventCountry = (AppCompatTextView) v.findViewById(R.id.text_view_event_country);
        mEventId = (AppCompatTextView) v.findViewById(R.id.text_view_event_id);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetEventInformation getEventInformation = (GetEventInformation) fragment;
                getEventInformation.getEvent(String.valueOf(mEventId.getText()));
            }
        });
    }

    public AppCompatTextView getEventTitle() {
        return mEventTitle;
    }

    public AppCompatTextView getEventDate() {
        return mEventDate;
    }

    public AppCompatTextView getEventCountry() {
        return mEventCountry;
    }

    public AppCompatTextView getEventId() {
        return mEventId;
    }
}