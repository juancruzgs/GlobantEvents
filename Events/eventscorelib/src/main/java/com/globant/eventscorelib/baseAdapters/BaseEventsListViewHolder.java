package com.globant.eventscorelib.baseAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.SharingIntent;

public abstract class BaseEventsListViewHolder extends RecyclerView.ViewHolder{
    private final View mViewGroup;
    private final View mSeparator;
    private final ImageView mImageEvent;
    private final ImageView mCategoryLogo;
    private final AppCompatTextView mEventTitle;
    private final AppCompatTextView mEventDate;
    private final AppCompatTextView mLocationEvent;
    private final AppCompatTextView mShortDescriptionEvent;
    private final AppCompatTextView mEventSpeakers;
    private final LinearLayout mLinearLayoutSpeakers;
    private final ImageView mEventShare;

    protected abstract Class<? extends BasePagerActivity> getActivityClass();

    public BaseEventsListViewHolder(final View itemView, final Context context, final Fragment fragment) {
        super(itemView);
        mViewGroup = itemView;
        mSeparator = itemView.findViewById(R.id.separator_view);
        mImageEvent = (ImageView) itemView.findViewById(R.id.event_image_view);
        mCategoryLogo = (ImageView) itemView.findViewById(R.id.imageView_Event_Type_Logo);
        mEventTitle = (AppCompatTextView) itemView.findViewById(R.id.event_title_text_view);
        mEventDate = (AppCompatTextView) itemView.findViewById(R.id.event_date_text_view);
        mLocationEvent = (AppCompatTextView) itemView.findViewById(R.id.event_location_text_view);
        mShortDescriptionEvent = (AppCompatTextView) itemView.findViewById(R.id.event_short_description_text_view);
        mEventSpeakers = (AppCompatTextView) itemView.findViewById(R.id.event_speakers_text_view);
        mLinearLayoutSpeakers = (LinearLayout) itemView.findViewById(R.id.speakers_layout);
        mEventShare = (ImageView) itemView.findViewById(R.id.image_view_icon);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetEventInformation getEventInformation = (GetEventInformation) fragment;
                Event event = getEventInformation.getEvent((Integer) itemView.getTag());
                Intent intent = new Intent(context, getActivityClass());
                intent.putExtra(CoreConstants.FIELD_EVENTS, event);
                context.startActivity(intent);
                fragment.getActivity().overridePendingTransition(R.anim.bottom_in, R.anim.top_out);
            }
        });

        mEventShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shortDescription = mShortDescriptionEvent.getText().toString() +
                        " - " + mEventDate.getText().toString() + " - " + mLocationEvent.getText().toString();
                Bitmap photo = ((BitmapDrawable)mImageEvent.getDrawable()).getBitmap();
                SharingIntent.showList(context);
              //  Uri imageUri = ConvertImage.getImageUri(context, photo);
              //  SharingIntent.shareViaFacebook(fragment, mEventTitle.getText().toString(), shortDescription, imageUri);
            }
       });
    }

    public void hideSpeakersLayout() {
        mLinearLayoutSpeakers.setVisibility(View.GONE);
        mSeparator.setVisibility(View.GONE);
    }


    public void showSpeakersLayout() {
        mLinearLayoutSpeakers.setVisibility(View.VISIBLE);
        mSeparator.setVisibility(View.VISIBLE);
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

    public AppCompatTextView getLocationEvent() {
        return mLocationEvent;
    }

    public AppCompatTextView getEventDate() {
        return mEventDate;
    }

    public AppCompatTextView getEventSpeakers() {
        return mEventSpeakers;
    }

    public AppCompatTextView getEventTitle() {
        return mEventTitle;
    }

    public ImageView getImageEvent() {
        return mImageEvent;
    }

    public ImageView getCategoryLogo() {
        return mCategoryLogo;
    }

    public AppCompatTextView getShortDescriptionEvent() {
        return mShortDescriptionEvent;
    }
}
