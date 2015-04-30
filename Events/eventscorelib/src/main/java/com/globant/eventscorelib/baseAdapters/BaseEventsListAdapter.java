package com.globant.eventscorelib.baseAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.ConvertImage;
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.CustomDateFormat;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseEventsListAdapter extends RecyclerView.Adapter<BaseEventsListViewHolder> {

    private Context mContext;
    private List<Event> mEventList;
    private List<Bitmap> mBitmapList;

    public BaseEventsListAdapter(List<Event> eventList, Context context) {
        mContext = context;
        mBitmapList = new ArrayList<>();
        for (int n = 0 ; n < eventList.size() ; n++){
            mBitmapList.add(ConvertImage.convertByteToBitmap(eventList.get(n).getEventLogo()));
        }
        eventList.add(new Event(CoreConstants.KEY_LAYOUT_PLACEHOLDER));
        mEventList = eventList;
    }

    protected abstract BaseEventsListViewHolder getViewHolder(View view);

    @Override
    public BaseEventsListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.event_card_row_item, viewGroup, false);
        return getViewHolder(view);
    }


    @Override
    public void onBindViewHolder(BaseEventsListViewHolder holder, int position) {
        holder.itemView.setTag(position);
        byte[] eventLogo = mEventList.get(position).getEventLogo();
           if (mEventList.get(position).getTitle().equals(CoreConstants.KEY_LAYOUT_PLACEHOLDER)){
            holder.getEventTitle().setText(mEventList.get(position).getTitle());
            holder.getViewGroup().setVisibility(View.INVISIBLE);
        }
        else {
            if (holder.getViewGroup().getVisibility() == View.INVISIBLE){
                holder.getViewGroup().setVisibility(View.VISIBLE);
            }
            if (eventLogo == null) {
                holder.getImageEvent().setImageResource(R.mipmap.placeholder);

            } else {
                holder.getImageEvent().setImageBitmap(mBitmapList.get(position));
            }
            holder.getEventTitle().setText(mEventList.get(position).getTitle());
            holder.getEventDate().setText(CustomDateFormat.getDate(mEventList.get(position).getStartDate(), mContext));
            holder.getLocationEvent().setText(mEventList.get(position).getCity() + ", " + mEventList.get(position).getCountry());
            holder.getShortDescriptionEvent().setText(mEventList.get(position).getShortDescription());
        }
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

}
