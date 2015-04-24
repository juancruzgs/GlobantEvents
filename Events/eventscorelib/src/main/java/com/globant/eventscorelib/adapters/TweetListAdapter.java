package com.globant.eventscorelib.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.globant.eventscorelib.utils.CropCircleTransformation;
import com.globant.eventscorelib.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import twitter4j.Status;

public class TweetListAdapter extends RecyclerView.Adapter<TweetListAdapter.TweetListViewHolder> {

    private Context mContext;
    private List<Status> mTweetsList;

    public TweetListAdapter(List<Status> tweetsList, Context context) {
        mTweetsList = tweetsList;
        mContext = context;
    }

    public static class TweetListViewHolder extends RecyclerView.ViewHolder {
        private TextView mTweetText;
        private TextView mUserName;
        private ImageView mUserPicture;

        public TweetListViewHolder(View v) {
            super(v);
            mTweetText = (TextView) v.findViewById(R.id.textView_tweet);
            mUserName =  (TextView) v.findViewById(R.id.textView_user_name);
            mUserPicture = (ImageView) v.findViewById(R.id.imageView_user_picture);
        }

        public TextView getTweetText() {
            return mTweetText;
        }

        public TextView getUserName() {
            return mUserName;
        }

        public ImageView getUserPicture() {
            return mUserPicture;
        }
    }

    @Override
    public TweetListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tweet_card_row_item, viewGroup, false);

        return new TweetListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TweetListViewHolder holder, int position) {
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
