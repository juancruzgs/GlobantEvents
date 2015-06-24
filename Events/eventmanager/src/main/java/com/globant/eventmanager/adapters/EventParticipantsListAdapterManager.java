package com.globant.eventmanager.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;

import com.globant.eventmanager.R;
import com.globant.eventmanager.fragments.EventParticipantsManagerFragment;
import com.globant.eventscorelib.baseAdapters.BaseParticipantListViewHolder;
import com.globant.eventscorelib.baseAdapters.BaseParticipantsListAdapter;
import com.globant.eventscorelib.domainObjects.Subscriber;
import com.globant.eventscorelib.utils.CropCircleTransformation;

import java.util.List;

/**
 * Created by gonzalo.lodi on 4/16/2015.
 */
public class EventParticipantsListAdapterManager extends BaseParticipantsListAdapter implements  ParticipantsListViewHolderManager.TouchListenerItem {

    private List<Subscriber> mSubscribers;
    private EventParticipantsManagerFragment mFragment;
    public Boolean mBooleanIsPressed;
    public ParticipantsListViewHolderManager mCurrentParticipant;
    private CropCircleTransformation mTransformation;
    private Bitmap rectangularBitmap;

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
        super(context,subscribers);
        mTransformation = new CropCircleTransformation(context);
        mFragment = fragment;
        mSubscribers = subscribers;
    }

    @Override
    protected BaseParticipantListViewHolder getViewHolder(View view) {
        return new ParticipantsListViewHolderManager(view, mFragment, this);
    }

    @Override
    public void onBindViewHolder(BaseParticipantListViewHolder holder, int position) {
        Subscriber subscriber = mSubscribers.get(position);
        ParticipantsListViewHolderManager participantListViewHolderManager = (ParticipantsListViewHolderManager) holder;
        participantListViewHolderManager.getTextViewPosition().setText(String.valueOf(position));
        String name = subscriber.getName()+" "+subscriber.getLastName();
        participantListViewHolderManager.getTextViewName().setText(name);
        participantListViewHolderManager.getTextViewNameLeft().setText(name);
        String occupation = subscriber.getOccupation();
        participantListViewHolderManager.getTextViewOccupation().setText(occupation);
        participantListViewHolderManager.getTextViewOccupationLeft().setText(occupation);
        Boolean isGlober = subscriber.isGlober();
        if (isGlober) {
            participantListViewHolderManager.getTextViewGlober().setText("Glober");
            participantListViewHolderManager.getTextViewGloberLeft().setText("Glober");
        }else {
            participantListViewHolderManager.getTextViewGlober().setText("");
            participantListViewHolderManager.getTextViewGloberLeft().setText("");
        }
        String location = subscriber.getCity() + ", " + subscriber.getCountry();
        participantListViewHolderManager.getTextViewLocation().setText(location);
        participantListViewHolderManager.getTextViewLocationLeft().setText(location);
        setViewHolderImage(participantListViewHolderManager, subscriber);
        if (subscriber.isAccepted()) {
            setConfirmedAcceptedVisibility(participantListViewHolderManager);
        }else {
            if (subscriber.isChecked()){
                setAcceptedVisibility(participantListViewHolderManager);
            }else {
                setNotAcceptedVisibility(participantListViewHolderManager);
            }
        }
        mBooleanIsPressed = participantListViewHolderManager.getBooleanIsPressed();
    }

    public void setNotAcceptedVisibility(ParticipantsListViewHolderManager holder) {
        holder.getLinearLayoutMiddle().setVisibility(View.VISIBLE);
        holder.getLinearLayoutMiddleLeft().setVisibility(View.INVISIBLE);
        holder.getFrameLayoutLeft().setVisibility(View.VISIBLE);
        holder.getFrameLayoutRight().setVisibility(View.INVISIBLE);
        holder.getImageViewParticipantRight().setVisibility(View.VISIBLE);
        holder.getImageViewAcceptedIcon().setVisibility(View.INVISIBLE);
        holder.getFrameLayoutHolder().setBackgroundColor(Color.WHITE);
    }

    private void setAcceptedVisibility(ParticipantsListViewHolderManager holder) {
        holder.getLinearLayoutMiddle().setVisibility(View.INVISIBLE);
        holder.getLinearLayoutMiddleLeft().setVisibility(View.VISIBLE);
        holder.getFrameLayoutLeft().setVisibility(View.INVISIBLE);
        holder.getFrameLayoutRight().setVisibility(View.VISIBLE);
        holder.getImageViewParticipantRight().setVisibility(View.VISIBLE);
        holder.getImageViewAcceptedIcon().setVisibility(View.INVISIBLE);
        holder.getFrameLayoutHolder().setBackgroundColor(mFragment.getActivity().getResources().getColor(R.color.globant_green_light));
    }

    public void setConfirmedAcceptedVisibility(ParticipantsListViewHolderManager holder) {
        holder.getLinearLayoutMiddle().setVisibility(View.VISIBLE);
        holder.getLinearLayoutMiddleLeft().setVisibility(View.INVISIBLE);
        holder.getFrameLayoutLeft().setVisibility(View.VISIBLE);
        holder.getFrameLayoutRight().setVisibility(View.VISIBLE);
        holder.getImageViewParticipantRight().setVisibility(View.INVISIBLE);
        holder.getImageViewAcceptedIcon().setVisibility(View.VISIBLE);
        holder.getFrameLayoutHolder().setBackgroundColor(Color.WHITE);
    }

    private void setViewHolderImage(ParticipantsListViewHolderManager holder, Subscriber subscriber) {
        Bitmap subscriberPicture = subscriber.getPicture();
//        cropRectangularImage(subscriberPicture);
        Bitmap circularImage = mTransformation.transform(subscriberPicture);
        holder.getImageViewParticipantLeft().setImageBitmap(circularImage);
        if (!subscriber.isAccepted()){
            holder.getImageViewParticipantRight().setImageBitmap(circularImage);
        }
    }

}
