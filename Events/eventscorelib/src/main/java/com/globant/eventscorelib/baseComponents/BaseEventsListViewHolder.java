package com.globant.eventscorelib.baseComponents;

import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globant.eventscorelib.R;

/**
* Created by paula.baudo on 4/17/2015.
*/
public class BaseEventsListViewHolder extends RecyclerView.ViewHolder{
    private final TextView mTextView;

    public BaseEventsListViewHolder(final View itemView) {
        super(itemView);
        mTextView = (TextView) itemView.findViewById(R.id.event_title_text_view);
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

    public TextView getTextView() {
        return mTextView;
    }
}
