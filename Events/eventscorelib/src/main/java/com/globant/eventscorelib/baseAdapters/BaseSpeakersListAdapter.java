package com.globant.eventscorelib.baseAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.utils.CropCircleTransformation;
import com.globant.eventscorelib.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by agustin.gugliotta on 15/04/2015.
 */
public class BaseSpeakersListAdapter extends RecyclerView.Adapter<BaseSpeakersListViewHolder>{
    private List<Speaker> mSpeakers;
    private CropCircleTransformation transformation;
    private final Context mContext;


    public BaseSpeakersListAdapter(Context context, List<Speaker> speakers) {
        mSpeakers = speakers;
        mContext = context;
        transformation = new CropCircleTransformation(context);
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
        Uri speaker_picture = getImageUri(position, speaker);
        Picasso.with(mContext).load(speaker_picture).transform(transformation).into(holder.getImageView());
    }

    public Uri getImageUri(int position, Speaker speaker) {
        Bitmap speaker_picture;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        speaker_picture = BitmapFactory.decodeByteArray(speaker.getPicture(), 0, speaker.getPicture().length, options);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        speaker_picture.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), speaker_picture, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public int getItemCount() {
        return mSpeakers.size();
    }

    public void addSpeaker(Speaker speaker){
        mSpeakers.add(speaker);


    }


}


