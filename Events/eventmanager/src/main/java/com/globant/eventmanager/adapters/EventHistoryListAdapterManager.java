package com.globant.eventmanager.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventmanager.R;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.CustomDateFormat;

import java.util.List;

public class EventHistoryListAdapterManager extends RecyclerView.Adapter<EventHistoryListViewHolderManager>{
    private List<Event> mEventList;
    private Context mContext;

    public EventHistoryListAdapterManager(Context context, List<Event> events) {
        mContext = context;
        mEventList = events;
    }

    @Override
    public EventHistoryListViewHolderManager onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_history_row_item, parent, false);
        return new EventHistoryListViewHolderManager(view);
    }

    @Override
    public void onBindViewHolder(EventHistoryListViewHolderManager holder, int position) {
        Event event = mEventList.get(position);
        holder.getEventTitle().setText(event.getTitle());
        holder.getEventDate().setText(CustomDateFormat.getDateWithTimeZone(event.getStartDate(), mContext) + " - " +
                CustomDateFormat.getDate(event.getEndDate(), mContext));
        holder.getEventCountry().setText(event.getCity() + ", " + event.getCountry());
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }
}




