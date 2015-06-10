package com.globant.eventscorelib.baseAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.domainObjects.Subscriber;
import com.globant.eventscorelib.utils.CropCircleTransformation;

import java.util.List;

/**
 * Created by gonzalo.lodi on 5/14/2015.
 */
public abstract class BaseParticipantsListAdapter extends RecyclerView.Adapter<BaseParticipantListViewHolder>{

    private List<Subscriber> mSubscribers;
    private CropCircleTransformation mTransformation;


    public BaseParticipantsListAdapter(Context context, List<Subscriber> subscribers) {
        mSubscribers = subscribers;
        mTransformation = new CropCircleTransformation(context);
    }

    protected abstract BaseParticipantListViewHolder getViewHolder(View view);

    @Override
    public BaseParticipantListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.participant_row_item, parent, false);

        return getViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseParticipantListViewHolder holder, int position) {
        Subscriber subscriber = mSubscribers.get(position);
        String name = subscriber.getName() + " " + subscriber.getLastName();
        holder.getTextViewName().setText(name);
        String occupation = subscriber.getOccupation();
        holder.getTextViewOccupation().setText(occupation);
        Boolean isGlober = subscriber.isGlober();
        if (isGlober) {
            holder.getTextViewGlober().setText("Glober");
        } else {
            holder.getTextViewGlober().setText("");
        }
        String location = subscriber.getCity() + ", " + subscriber.getCountry();
        holder.getTextViewLocation().setText(location);
        Bitmap subscriberPicture = subscriber.getPicture();
        Bitmap circularImage = mTransformation.transform(subscriberPicture);
        holder.getImageViewParticipantLeft().setImageBitmap(circularImage);
    }

    @Override
    public int getItemCount() {
        return mSubscribers.size();
    }
}
