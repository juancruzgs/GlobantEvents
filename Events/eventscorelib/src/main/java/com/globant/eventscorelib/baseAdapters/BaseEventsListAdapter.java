package com.globant.eventscorelib.baseAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.utils.ConvertImage;
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.CustomDateFormat;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseEventsListAdapter extends RecyclerView.Adapter<BaseEventsListViewHolder> {

    private Context mContext;
    private List<Event> mEventList;
    private List<Bitmap> mBitmapList;
    private Drawable mDrawableToApply;

    public BaseEventsListAdapter(List<Event> eventList, Context context) {
        mContext = context;
        mBitmapList = new ArrayList<>();
        for (int n = 0; n < eventList.size(); n++) {
            if (eventList.get(n).getEventLogo() != null) {
                mBitmapList.add(ConvertImage.convertByteToBitmap(eventList.get(n).getEventLogo()));
            } else {
                mBitmapList.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.placeholder));
            }
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
        if (mEventList.get(position).getTitle().equals(CoreConstants.KEY_LAYOUT_PLACEHOLDER)) {
            holder.getEventTitle().setText(mEventList.get(position).getTitle());
            holder.getViewGroup().setVisibility(View.INVISIBLE);
        } else {

            if (holder.getViewGroup().getVisibility() == View.INVISIBLE) {
                holder.getViewGroup().setVisibility(View.VISIBLE);
            }

            if (eventLogo == null) {
                holder.getImageEvent().setImageResource(R.mipmap.placeholder);

            } else {
                holder.getImageEvent().setImageBitmap(mBitmapList.get(position));
            }
            if (mEventList.get(position).getCategory() != null) {
                switch (mEventList.get(position).getCategory().toLowerCase()) {
                    case "social":
                        holder.getCategoryLogo().setImageResource(R.mipmap.ic_social);
                        mDrawableToApply = mContext.getResources().getDrawable(R.mipmap.ic_social);
                        mDrawableToApply = DrawableCompat.wrap(mDrawableToApply);
                        DrawableCompat.setTint(mDrawableToApply, mContext.getResources().getColor(R.color.pink_material));
                        break;
                    case "technical":
                        holder.getCategoryLogo().setImageResource(R.mipmap.ic_technical);
                        mDrawableToApply = mContext.getResources().getDrawable(R.mipmap.ic_technical);
                        mDrawableToApply = DrawableCompat.wrap(mDrawableToApply);
                        DrawableCompat.setTint(mDrawableToApply, mContext.getResources().getColor(R.color.yellow_material));
                        break;
                    case "informative":
                        holder.getCategoryLogo().setImageResource(R.mipmap.ic_informative);
                        mDrawableToApply = mContext.getResources().getDrawable(R.mipmap.ic_informative);
                        mDrawableToApply = DrawableCompat.wrap(mDrawableToApply);
                        DrawableCompat.setTint(mDrawableToApply, mContext.getResources().getColor(R.color.blue_light_material));
                        break;
                    default:
                        holder.getCategoryLogo().setImageResource(R.mipmap.ic_launcher);
                        mDrawableToApply = mContext.getResources().getDrawable(R.mipmap.ic_launcher);
                        mDrawableToApply = DrawableCompat.wrap(mDrawableToApply);
                        DrawableCompat.setTint(mDrawableToApply, mContext.getResources().getColor(R.color.globant_green_dark));
                        break;
                }
            } else {
                holder.getCategoryLogo().setImageResource(R.mipmap.ic_launcher);
                mDrawableToApply = mContext.getResources().getDrawable(R.mipmap.ic_launcher);
                mDrawableToApply = DrawableCompat.wrap(mDrawableToApply);
                DrawableCompat.setTint(mDrawableToApply, mContext.getResources().getColor(R.color.globant_green_dark));

            }
            if (mEventList.get(position).getSpeakers().size() == 0){
                holder.hideSpeakersLayout();
            }
            else {
                holder.showSpeakersLayout();
                holder.getEventSpeakers().setText(speakerToString(mEventList.get(position).getSpeakers()));
            }
            mDrawableToApply = DrawableCompat.unwrap(mDrawableToApply);
            holder.getCategoryLogo().setImageDrawable(mDrawableToApply);
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

    private String speakerToString(List<Speaker> speakers){
        String result = "";
        for (Speaker speaker : speakers){
            result += speaker.getName() + " " + speaker.getLastName() + ", ";
        }
        result = result.substring(0,result.length()-2) + ".";
        return result;
    }

}
