package com.globant.eventmanager.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.nfc.Tag;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventmanager.R;
import com.globant.eventmanager.fragments.EventParticipantsManagerFragment;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.domainObjects.Subscriber;
import com.globant.eventscorelib.utils.CropCircleTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by gonzalo.lodi on 4/16/2015.
 */
public class EventParticipantsListAdapterManager extends RecyclerView.Adapter<ParticipantsListViewHolderManager> implements  ParticipantsListViewHolderManager.TouchListenerItem {

    private List<Subscriber> mSubscribers;
    private final Context mContext;
    private EventParticipantsManagerFragment mFragment;
    public Boolean mBooleanIsPressed;
    public ParticipantsListViewHolderManager mCurrentParticipant;
    private CropCircleTransformation mTransformation;

    public ParticipantsListViewHolderManager getCurrentParticipant() {
        return mCurrentParticipant;
    }

    public void setSubscribers(List<Subscriber> mSubscribers) {
        this.mSubscribers = mSubscribers;
    }

    @Override
    public void onTouchListenerItem(ParticipantsListViewHolderManager participantsListViewHolderManager) {
        mCurrentParticipant = participantsListViewHolderManager;
    }

    public EventParticipantsListAdapterManager(Context context, List<Subscriber> subscribers, EventParticipantsManagerFragment fragment) {
        mContext = context;
        mFragment = fragment;
        mSubscribers = subscribers;
        mTransformation = new CropCircleTransformation(mContext);
    }

    @Override
    public ParticipantsListViewHolderManager onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(com.globant.eventmanager.R.layout.participant_row_item, parent, false);

        return new ParticipantsListViewHolderManager(view, mFragment, this);
    }

    @Override
    public void onBindViewHolder(ParticipantsListViewHolderManager holder, int position) {
        Subscriber subscriber = mSubscribers.get(position);
        holder.getTextViewPosition().setText(String.valueOf(position));
        String name = subscriber.getName()+" "+subscriber.getLastName();
        holder.getTextViewName().setText(name);
        holder.getTextViewNameLeft().setText(name);
        String occupation = subscriber.getOccupation();
        holder.getTextViewOccupation().setText(occupation);
        holder.getTextViewOccupationLeft().setText(occupation);
        Boolean isGlober = subscriber.isGlober();
        if (isGlober) {
            holder.getTextViewGlober().setText("Glober");
            holder.getTextViewGloberLeft().setText("Glober");
        }else {
            holder.getTextViewGlober().setText("");
            holder.getTextViewGloberLeft().setText("");
        }
        String location = subscriber.getCity() + ", " + subscriber.getCountry();
        holder.getTextViewLocation().setText(location);
        holder.getTextViewLocationLeft().setText(location);
        setViewHolderImage(holder, subscriber);
        if (subscriber.isAccepted()) {
            setAcceptedVisibility(holder);
        }else {
            setNotAcceptedVisibility(holder);
        }
        mBooleanIsPressed = holder.getBooleanIsPressed();

    }

    public void setNotAcceptedVisibility(ParticipantsListViewHolderManager holder) {
        holder.getLinearLayoutMiddle().setVisibility(View.VISIBLE);
        holder.getLinearLayoutMiddleLeft().setVisibility(View.INVISIBLE);
        holder.getFrameLayoutLeft().setVisibility(View.VISIBLE);
        holder.getFrameLayoutRight().setVisibility(View.INVISIBLE);
        holder.getFrameLayoutHolder().setBackgroundColor(Color.WHITE);
    }

    public void setAcceptedVisibility(ParticipantsListViewHolderManager holder) {
        holder.getLinearLayoutMiddle().setVisibility(View.INVISIBLE);
        holder.getLinearLayoutMiddleLeft().setVisibility(View.VISIBLE);
        holder.getFrameLayoutLeft().setVisibility(View.INVISIBLE);
        holder.getFrameLayoutRight().setVisibility(View.VISIBLE);
        holder.getFrameLayoutHolder().setBackgroundColor(mFragment.getActivity().getResources().getColor(R.color.globant_green_light));
    }

    private void setViewHolderImage(ParticipantsListViewHolderManager holder, Subscriber subscriber) {
        Bitmap subscriberPicture = getSubscriberImage(subscriber);
        Bitmap circularImage = mTransformation.transform(subscriberPicture);
        holder.getImageViewParticipantLeft().setImageBitmap(circularImage);
        holder.getImageViewParticipantRight().setImageBitmap(circularImage);
    }

    public Bitmap getSubscriberImage(Subscriber subscriber) {
        Bitmap subscriberPicture;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        subscriberPicture = BitmapFactory.decodeByteArray(subscriber.getPicture(), 0, subscriber.getPicture().length, options);
        return subscriberPicture;
    }

    @Override
    public int getItemCount() {
        return mSubscribers.size();
    }
}
