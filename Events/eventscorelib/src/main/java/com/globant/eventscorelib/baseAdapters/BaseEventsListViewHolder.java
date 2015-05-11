package com.globant.eventscorelib.baseAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseEventDetailPagerActivity;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.CoreConstants;

import java.io.Serializable;

public abstract class BaseEventsListViewHolder extends RecyclerView.ViewHolder{
    private final View mViewGroup;
    private final ImageView mImageEvent;
    private final ImageView mCategoryLogo;
    private final TextView mEventTitle;
    private final TextView mEventDate;
    private final TextView mLocationEvent;
    private final TextView mShortDescriptionEvent;
    private final TextView mEventSpeakers;
    private final LinearLayout mLinearLayoutSpeakers;

    public interface GetEventInformation {
        Event getEvent(int position);
    }

    protected abstract Class<? extends BasePagerActivity> getActivityClass();

    public BaseEventsListViewHolder(final View itemView, final Context context, final Fragment fragment) {
        super(itemView);
        mViewGroup = itemView;
        mImageEvent = (ImageView) itemView.findViewById(R.id.event_image_view);
        mCategoryLogo = (ImageView) itemView.findViewById(R.id.imageView_Event_Type_Logo);
        mEventTitle = (TextView) itemView.findViewById(R.id.event_title_text_view);
        mEventDate = (TextView) itemView.findViewById(R.id.event_date_text_view);
        mLocationEvent = (TextView) itemView.findViewById(R.id.event_location_text_view);
        mShortDescriptionEvent = (TextView) itemView.findViewById(R.id.event_short_description_text_view);
        mEventSpeakers = (TextView) itemView.findViewById(R.id.event_speakers_text_view);
        mLinearLayoutSpeakers = (LinearLayout) itemView.findViewById(R.id.speakers_layout);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetEventInformation getEventInformation = (GetEventInformation) fragment;
                Event event = getEventInformation.getEvent((Integer) itemView.getTag());
                Intent intent = new Intent(context, getActivityClass());
                intent.putExtra(CoreConstants.FIELD_EVENTS, event);
                context.startActivity(intent);
            }
        });
    }

    public void hideSpeakersLayout() {
        mLinearLayoutSpeakers.setVisibility(View.GONE);
    }


    public void showSpeakersLayout() {
        mLinearLayoutSpeakers.setVisibility(View.VISIBLE);
    }

    private void getGreenSpeakerIcon(View itemView) {
        Drawable drawableToApply = DrawableCompat.wrap(itemView.getResources().getDrawable(R.mipmap.ic_speaker));
        DrawableCompat.setTint(drawableToApply, itemView.getResources().getColor(R.color.globant_green));
        drawableToApply = DrawableCompat.unwrap(drawableToApply);
        ImageView speakerIcon = (ImageView) itemView.findViewById(R.id.speaker_image_view);
        speakerIcon.setImageDrawable(drawableToApply);
        speakerIcon.setImageDrawable(itemView.getResources().getDrawable(R.mipmap.ic_speaker));
    }
    
    public View getViewGroup() {
        return mViewGroup;
    }

    public TextView getLocationEvent() {
        return mLocationEvent;
    }

    public TextView getEventDate() {
        return mEventDate;
    }

    public TextView getEventSpeakers() {
        return mEventSpeakers;
    }

    public TextView getEventTitle() {
        return mEventTitle;
    }

    public ImageView getImageEvent() {
        return mImageEvent;
    }

    public ImageView getCategoryLogo() {
        return mCategoryLogo;
    }

    public TextView getShortDescriptionEvent() {
        return mShortDescriptionEvent;
    }
}
