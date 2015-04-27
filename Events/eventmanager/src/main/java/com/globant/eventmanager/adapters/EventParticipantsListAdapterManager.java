package com.globant.eventmanager.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventmanager.R;
import com.globant.eventmanager.fragments.EventParticipantsManagerFragment;
import com.globant.eventscorelib.utils.CropCircleTransformation;
import com.squareup.picasso.Picasso;

/**
 * Created by gonzalo.lodi on 4/16/2015.
 */
public class EventParticipantsListAdapterManager extends RecyclerView.Adapter<ParticipantsListViewHolderManager> {

    private CropCircleTransformation transformation;
    private String[] mDataSet;
    private final Context mContext;
    private float mX;
    private EventParticipantsManagerFragment mFragment;

    public EventParticipantsListAdapterManager(Context context, String[] dataSet, EventParticipantsManagerFragment fragment) {
        mContext = context;
        this.transformation = new CropCircleTransformation(context);
        mFragment = fragment;
        mDataSet = dataSet;
    }

    @Override
    public ParticipantsListViewHolderManager onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(com.globant.eventmanager.R.layout.participant_row_item, parent, false);
        return new ParticipantsListViewHolderManager(view, mFragment);
    }

    @Override
    public void onBindViewHolder(ParticipantsListViewHolderManager holder, int position) {
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
