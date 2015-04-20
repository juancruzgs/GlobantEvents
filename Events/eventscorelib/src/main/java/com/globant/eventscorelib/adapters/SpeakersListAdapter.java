package com.globant.eventscorelib.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globant.eventscorelib.utils.CropCircleTransformation;
import com.globant.eventscorelib.R;
import com.squareup.picasso.Picasso;

/**
 * Created by agustin.gugliotta on 15/04/2015.
 */
public class SpeakersListAdapter extends RecyclerView.Adapter<SpeakersListAdapter.ViewHolder>{
    private String[] mDatasetName;
    private String[] mDatasetDescription;
    private CropCircleTransformation transformation;
    private final Context mContext;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final TextView textViewDescription;
        private final ImageView imageView;


        public ViewHolder(View v) {
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

    public SpeakersListAdapter(Context context, String[] datasetName, String[] datasetDescription) {
        mDatasetName = datasetName;
        mDatasetDescription = datasetDescription;
        mContext = context;
        transformation = new CropCircleTransformation(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.speaker_list_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getTextViewName().setText(mDatasetName[position]);
        holder.getTextViewDescription().setText(mDatasetDescription[position]);
        Picasso.with(mContext).load(R.drawable.speaker_image).transform(transformation).into(holder.getImageView());
    }

    @Override
    public int getItemCount() {
        return mDatasetName.length;
    }
}
