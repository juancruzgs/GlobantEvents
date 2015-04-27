package com.globant.eventscorelib.baseAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.utils.CropCircleTransformation;
import com.globant.eventscorelib.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import twitter4j.Status;

public class BaseTweetListAdapter extends RecyclerView.Adapter<BaseTweetListViewHolder> {

    private Context mContext;
    private List<Status> mTweetsList;

    public BaseTweetListAdapter(List<Status> tweetsList, Context context) {
        mTweetsList = tweetsList;
        mContext = context;
    }

    @Override
    public BaseTweetListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tweet_card_row_item, viewGroup, false);

        return new BaseTweetListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BaseTweetListViewHolder holder, int position) {
        CropCircleTransformation transformation = new CropCircleTransformation(mContext);
        holder.getTweetText().setText(mTweetsList.get(position).getText());
        holder.getUserName().setText(mTweetsList.get(position).getUser().getName());
        Picasso.with(mContext).load(mTweetsList.get(position).getUser()
                .getOriginalProfileImageURL()).transform(transformation).into(holder.getUserPicture());
        Linkify.addLinks(holder.getTweetText(), Linkify.ALL);
    }

    @Override
    public int getItemCount() {
        return mTweetsList.size();
    }
}
