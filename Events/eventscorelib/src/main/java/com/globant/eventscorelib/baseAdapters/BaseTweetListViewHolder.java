package com.globant.eventscorelib.baseAdapters;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.globant.eventscorelib.R;

/**
* Created by juan.soler on 24/04/2015.
*/
public class BaseTweetListViewHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView mTweetText;
    private AppCompatTextView mUserName;
    private ImageView mUserPicture;

    public BaseTweetListViewHolder(View v) {
        super(v);
        mTweetText = (AppCompatTextView) v.findViewById(R.id.textView_tweet);
        mUserName =  (AppCompatTextView) v.findViewById(R.id.textView_user_name);
        mUserPicture = (ImageView) v.findViewById(R.id.imageView_user_picture);
    }

    public AppCompatTextView getTweetText() {
        return mTweetText;
    }

    public AppCompatTextView getUserName() {
        return mUserName;
    }

    public ImageView getUserPicture() {
        return mUserPicture;
    }
}
