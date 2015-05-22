package com.globant.eventmanager.adapters;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.globant.eventmanager.R;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.CustomDateFormat;

import java.util.ArrayList;
import java.util.List;

public class EventHistoryListAdapterManager extends RecyclerView.Adapter<EventHistoryListViewHolderManager> implements Filterable {
    private List<Event> mEventList;
    private Context mContext;
    private List<Event> mFilteredEventList;
    private Fragment mFragment;
    private TextView mTextViewNoEvents;
    private RecyclerView mRecyclerView;

    public EventHistoryListAdapterManager(List<Event> events, Context context, Fragment fragment, TextView textViewNoEvents, RecyclerView recyclerView) {
        mContext = context;
        mEventList = events;
        mFragment = fragment;
        mTextViewNoEvents = textViewNoEvents;
        mRecyclerView = recyclerView;
    }

    @Override
    public EventHistoryListViewHolderManager onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_history_row_item, parent, false);
        return new EventHistoryListViewHolderManager(view, mFragment);
    }

    @Override
    public void onBindViewHolder(EventHistoryListViewHolderManager holder, int position) {
        Event event = mEventList.get(position);
        holder.getEventId().setText(event.getObjectID());
        holder.getEventTitle().setText(event.getTitle());
        holder.getEventDate().setText(CustomDateFormat.getCompleteDate(event.getStartDate(), mContext));
        holder.getEventCountry().setText(event.getCity() + ", " + event.getCountry());
    }

    @Override
    public int getItemCount() {
        if (mEventList != null) {
            return mEventList.size();
        } else {
            return CoreConstants.ZERO;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<Event> results = new ArrayList<>();
                if (mFilteredEventList == null)
                    mFilteredEventList = mEventList;
                if (constraint != null) {
                    if (mFilteredEventList != null & mFilteredEventList.size() > 0) {
                        for (final Event event : mFilteredEventList) {
                            if ((event.getTitle().toLowerCase().contains(constraint)) ||
                                    (CustomDateFormat.getCompleteDate(event.getStartDate(), mContext).contains(constraint) ||
                                            (event.getCity().toLowerCase().contains(constraint)) ||
                                            (event.getCountry().toLowerCase().contains(constraint)))) {
                                results.add(event);
                            }
                        }
                        oReturn.values = results;
                    }
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mEventList = (ArrayList<Event>) results.values;
                if (mEventList.size() == 0) {
                    mTextViewNoEvents.setVisibility(View.VISIBLE);
                } else {
                    mTextViewNoEvents.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            }
        };
    }
}




