package com.globant.eventmanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.utils.CropCircleTransformation;
import com.squareup.picasso.Picasso;

/**
 * Created by gonzalo.lodi on 4/16/2015.
 */
public class EventParticipantsListAdapter extends RecyclerView.Adapter<ParticipantsListViewHolder> {

    private CropCircleTransformation transformation;
    private String[] mDataSet;
    private final Context mContext;
    private float mX;
    private EventParticipantsFragment mFragment;

    public EventParticipantsListAdapter (Context context, String[] dataSet, EventParticipantsFragment fragment) {
        mContext = context;
        this.transformation = new CropCircleTransformation(context);
        mFragment = fragment;
        mDataSet = dataSet;
    }

    @Override
    public ParticipantsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(com.globant.eventmanager.R.layout.participant_row_item, parent, false);
        return new ParticipantsListViewHolder(view, mFragment);
    }

    @Override
    public void onBindViewHolder(ParticipantsListViewHolder holder, int position) {
        holder.getTextViewName().setText(mDataSet[position]);
        holder.getmTextViewNameLeft().setText(mDataSet[position]);
        Picasso.with(mContext).load(R.drawable.profile_pic).transform(transformation).into(holder.getImageViewParticipantLeft());
        Picasso.with(mContext).load(R.drawable.profile_pic).transform(transformation).into(holder.getImageViewParticipantRight());
        holder.getmLinearLayoutMiddle().setX(holder.getFrameLayoutWidth());

    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}