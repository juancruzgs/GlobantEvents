package com.globant.eventscorelib;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
* Created by paula.baudo on 4/17/2015.
*/
public class BaseEventsListViewHolder extends RecyclerView.ViewHolder{
    private final TextView mTextView;

    public BaseEventsListViewHolder(final View itemView) {
        super(itemView);
        mTextView = (TextView) itemView.findViewById(R.id.event_title_text_view);
    }

    public TextView getTextView() {
        return mTextView;
    }
}
