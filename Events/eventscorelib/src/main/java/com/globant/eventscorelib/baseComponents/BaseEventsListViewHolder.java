package com.globant.eventscorelib.baseComponents;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.globant.eventscorelib.R;

public class BaseEventsListViewHolder extends RecyclerView.ViewHolder{
    private final ImageView mImageEvent;
    private final TextView mEventTitle;
    private final TextView mEventDate;
    private final TextView mLocationEvent;

    public BaseEventsListViewHolder(final View itemView) {
        super(itemView);
        mImageEvent = (ImageView) itemView.findViewById(R.id.event_image_view);
        mEventTitle = (TextView) itemView.findViewById(R.id.event_title_text_view);
        mEventDate = (TextView) itemView.findViewById(R.id.event_date_text_view);
        mLocationEvent = (TextView) itemView.findViewById(R.id.event_location_text_view);
    }

    public TextView getLocationEvent() {
        return mLocationEvent;
    }

    public TextView getEventDate() {
        return mEventDate;
    }

    public TextView getEventTitle() {
        return mEventTitle;
    }

    public ImageView getImageEvent() {
        return mImageEvent;
    }
}
