package com.globant.eventscorelib;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import twitter4j.Status;


public class TweetAdapter extends ArrayAdapter<Status> {

    private Context mContext;
    private LayoutInflater mInflater;

    public TweetAdapter(Context context, List<Status> list) {
        super(context, R.layout.adapter_tweet_item, list);
        mContext = context;
        this.addAll(list);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

   private class ViewHolder {
        TextView mTweetText;
        TextView mUserName;
        ImageView mUserPicture;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_tweet_item, null);
            holder = new ViewHolder();
            holder.mTweetText = (TextView) convertView.findViewById(R.id.name);
            holder.mUserName = (TextView) convertView.findViewById(R.id.position);
            holder.mUserPicture = (ImageView) convertView.findViewById(R.id.photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Status tweetStatus = getItem(position);
        holder.mTweetText.setText(tweetStatus.getText());

        Linkify.addLinks(holder.mTweetText, Linkify.ALL);

        holder.mUserName.setText(tweetStatus.getUser().getName());

        if (tweetStatus.getUser().getBiggerProfileImageURL() != null) {
            Picasso.with(mContext).load(tweetStatus.getUser().getBiggerProfileImageURL()).
                    into(holder.mUserPicture);
        }

        return convertView;
    }
}
