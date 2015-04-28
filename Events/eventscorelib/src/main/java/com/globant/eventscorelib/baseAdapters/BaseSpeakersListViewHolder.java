package com.globant.eventscorelib.baseAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.globant.eventscorelib.R;

/**
* Created by micaela.cavallo on 27/04/2015.
*/
public class BaseSpeakersListViewHolder extends RecyclerView.ViewHolder{
    private final TextView textViewName;
    private final TextView textViewDescription;
    private final ImageView imageView;


    public BaseSpeakersListViewHolder(View v) {
        super(v);
        textViewName = (TextView) v.findViewById(R.id.text_view_speaker_item_name);
        textViewDescription = (TextView) v.findViewById(R.id.text_view_speaker_item_description);
        imageView = (ImageView) v.findViewById(R.id.image_view_profile_speaker);
    }

    public TextView getTextViewName() {
        return textViewName;
    }
    public TextView getTextViewDescription() {
        return textViewDescription;
    }
    public ImageView getImageView() {
        return imageView;
    }
}
