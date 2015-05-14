package com.globant.eventmanager.adapters;


import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.globant.eventmanager.R;

public class EventHistoryListViewHolderManager extends RecyclerView.ViewHolder{
    private AppCompatTextView mEventTitle;
    private AppCompatTextView mEventDate;
    private AppCompatTextView mEventCountry;

    public EventHistoryListViewHolderManager (View v) {
        super (v);
        mEventTitle = (AppCompatTextView) v.findViewById(R.id.text_view_event_title);
        mEventDate = (AppCompatTextView) v.findViewById(R.id.text_view_event_date);
        mEventCountry = (AppCompatTextView) v.findViewById(R.id.text_view_event_country);
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
}