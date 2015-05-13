package com.globant.eventscorelib.baseAdapters;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.globant.eventscorelib.R;

/**
* Created by micaela.cavallo on 27/04/2015.
*/
public class BaseSpeakersListViewHolder extends RecyclerView.ViewHolder{
    private final AppCompatTextView textViewName;
    private final AppCompatTextView textViewDescription;
    private final ImageView imageView;


    public BaseSpeakersListViewHolder(View v) {
        super(v);
        textViewName = (AppCompatTextView) v.findViewById(R.id.text_view_speaker_item_name);
        textViewDescription = (AppCompatTextView) v.findViewById(R.id.text_view_speaker_item_description);
        imageView = (ImageView) v.findViewById(R.id.image_view_profile_speaker);
    }

    public AppCompatTextView getTextViewName() {
        return textViewName;
    }
    public AppCompatTextView getTextViewDescription() {
        return textViewDescription;
    }
    public ImageView getImageView() {
        return imageView;
    }
}
