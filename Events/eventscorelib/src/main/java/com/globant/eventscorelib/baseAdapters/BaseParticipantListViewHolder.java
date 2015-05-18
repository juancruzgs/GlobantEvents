package com.globant.eventscorelib.baseAdapters;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.globant.eventscorelib.R;

/**
 * Created by gonzalo.lodi on 5/14/2015.
 */
public abstract class BaseParticipantListViewHolder extends RecyclerView.ViewHolder{

    private AppCompatTextView mTextViewName;
    private AppCompatTextView mTextViewGlober;
    private AppCompatTextView mTextViewOccupation;
    private ImageView mImageViewParticipantLeft;
    private AppCompatTextView mTextViewLocation;

    public AppCompatTextView getTextViewName() {
        return mTextViewName;
    }

    public AppCompatTextView getTextViewGlober() {
        return mTextViewGlober;
    }

    public AppCompatTextView getTextViewOccupation() {
        return mTextViewOccupation;
    }

    public ImageView getImageViewParticipantLeft() {
        return mImageViewParticipantLeft;
    }

    public AppCompatTextView getTextViewLocation() {
        return mTextViewLocation;
    }

    public BaseParticipantListViewHolder(View itemView) {
        super(itemView);
        mTextViewName = (AppCompatTextView) itemView.findViewById(R.id.text_view_participant_name);
        mTextViewGlober = (AppCompatTextView) itemView.findViewById(R.id.text_view_glober);
        mImageViewParticipantLeft = (ImageView) itemView.findViewById(R.id.image_view_participant_left);
        mTextViewOccupation = (AppCompatTextView) itemView.findViewById(R.id.text_view_occupation);
        mTextViewLocation = (AppCompatTextView) itemView.findViewById(R.id.text_view_location);

    }

}
