package com.globant.eventscorelib.baseComponents;

import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private void hideSpeakersLayout(View itemView) {
        LinearLayout linearLayoutSpeakers = (LinearLayout) itemView.findViewById(R.id.speakers_layout);
        linearLayoutSpeakers.setVisibility(View.GONE);
    }

    private void showSpeakersLayout(View itemView) {
        LinearLayout linearLayoutSpeakers = (LinearLayout) itemView.findViewById(R.id.speakers_layout);
        linearLayoutSpeakers.setVisibility(View.VISIBLE);
    }

    private void getGreenSpeakerIcon(View itemView) {
        Drawable drawableToApply = DrawableCompat.wrap(itemView.getResources().getDrawable(R.mipmap.ic_speaker));
        DrawableCompat.setTint(drawableToApply, itemView.getResources().getColor(R.color.globant_green));
        drawableToApply = DrawableCompat.unwrap(drawableToApply);
        ImageView speakerIcon = (ImageView) itemView.findViewById(R.id.speaker_image_view);
        speakerIcon.setImageDrawable(drawableToApply);
        speakerIcon.setImageDrawable(itemView.getResources().getDrawable(R.mipmap.ic_speaker));
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
