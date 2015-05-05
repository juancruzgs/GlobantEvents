package com.globant.eventscorelib.baseAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.utils.CropCircleTransformation;
import com.globant.eventscorelib.R;

import java.util.List;

/**
 * Created by agustin.gugliotta on 15/04/2015.
 */
public class BaseSpeakersListAdapter extends RecyclerView.Adapter<BaseSpeakersListViewHolder>{
    private List<Speaker> mSpeakers;
    private final CropCircleTransformation mTransformation;


    public BaseSpeakersListAdapter(Context context, List<Speaker> speakers) {
        mSpeakers = speakers;
        mTransformation = new CropCircleTransformation(context);
    }

    @Override
    public BaseSpeakersListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.speaker_list_row_item, parent, false);
        return new BaseSpeakersListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseSpeakersListViewHolder holder, int position) {
        Speaker speaker = mSpeakers.get(position);
        holder.getTextViewName().setText(speaker.getName()+" "+speaker.getLastName());
        holder.getTextViewDescription().setText(speaker.getTitle());
        setViewHolderImage(holder, speaker);
    }

    private void setViewHolderImage(BaseSpeakersListViewHolder holder, Speaker speaker) {
        Bitmap speakerPicture = getSpeakerImage(speaker);
        Bitmap circularImage = mTransformation.transform(speakerPicture);
        holder.getImageView().setImageBitmap(circularImage);
    }

    public Bitmap getSpeakerImage(Speaker speaker) {
        Bitmap speakerPicture;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        speakerPicture = BitmapFactory.decodeByteArray(speaker.getPicture(), 0, speaker.getPicture().length, options);
        return speakerPicture;
    }

    @Override
    public int getItemCount() {
        if (mSpeakers != null){
            return mSpeakers.size();
        }
        else {
            return 0;
        }
    }

    public void addSpeaker(Speaker speaker){
        mSpeakers.add(speaker);
    }
}


