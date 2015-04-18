package com.globant.eventscorelib.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globant.eventscorelib.R;

/**
 * Created by agustin.gugliotta on 15/04/2015.
 */
public class SpeakersListAdapter extends RecyclerView.Adapter<SpeakersListAdapter.ViewHolder>{
    private String[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.text_view_speaker_item);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public SpeakersListAdapter(String[] dataset) {
        mDataset = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.speaker_list_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getTextView().setText(mDataset[position]);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
