package com.globant.eventscorelib.baseAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.utils.CoreConstants;

public class SocialNetworksAdapter extends BaseAdapter {
    private final Context mContext;
    private final CharSequence[] mItems;

    public SocialNetworksAdapter(Context context, CharSequence[] items) {
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public Object getItem(int position) {
        return mItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(mContext, R.layout.dialog_share_list_item, null);
        ImageView icon = (ImageView) convertView.findViewById(R.id.image_view_icon);
        AppCompatTextView socialNetwork = (AppCompatTextView) convertView.findViewById(R.id.text_view_social_network);
        socialNetwork.setText(mItems[position]);
        switch (socialNetwork.getText().toString()) {
            case CoreConstants.FACEBOOK:
                icon.setImageResource(R.mipmap.ic_facebook);
                break;
            case CoreConstants.TWITTER:
                icon.setImageResource(R.mipmap.ic_twitter);
                break;
            case CoreConstants.GMAIL:
                icon.setImageResource(R.mipmap.ic_gmail);
                break;
            case CoreConstants.WHATSAPP:
                icon.setImageResource(R.mipmap.ic_whatsapp);
                break;
        }
        return convertView;
    }
}
