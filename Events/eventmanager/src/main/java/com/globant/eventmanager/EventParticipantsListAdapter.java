package com.globant.eventmanager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.globant.eventscorelib.CropCircleTransformation;
import com.squareup.picasso.Picasso;

/**
 * Created by gonzalo.lodi on 4/16/2015.
 */
public class EventParticipantsListAdapter extends RecyclerView.Adapter<EventParticipantsListAdapter.ViewHolder> {

    private CropCircleTransformation transformation;
    private String[] mDataSet;
    private final Context mContext;




    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

        private final TextView textViewName;
        private final TextView textViewGlober;
        private final ImageView imageViewParticipantLeft;
        private final ImageView imageViewParticipantRight;
        private final LinearLayout participantHolderItemLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            textViewName = (TextView) itemView.findViewById(R.id.text_view_participant_name);
            textViewGlober = (TextView) itemView.findViewById(R.id.text_view_glober);
            imageViewParticipantLeft = (ImageView) itemView.findViewById(R.id.image_view_participant_left);
            imageViewParticipantRight = (ImageView) itemView.findViewById(R.id.image_view_participant_right);
            participantHolderItemLayout = (LinearLayout) itemView.findViewById(R.id.participant_item_holder_layout);
        }

        public TextView getTextViewName() {
            return textViewName;
        }

        public TextView getTextViewGlober() {
            return textViewGlober;
        }

        public ImageView getImageViewParticipantLeft() {
            return imageViewParticipantLeft;
        }

        public ImageView getImageViewParticipantRight() {
            return imageViewParticipantRight;
        }


        private void animateInvisibility(final View myView) {
            int cx = (myView.getLeft() + myView.getRight()) / 2;
            int cy = (myView.getTop() + myView.getBottom()) / 2;

            // get the initial radius for the clipping circle
            int initialRadius = myView.getWidth();

            // create the animation (the final radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.GONE);
                }
            });

            // start the animation
            anim.start();
        }

        private void animateVisibility(View myView) {
            int cx = (myView.getLeft() + myView.getRight()) / 2;
            int cy = (myView.getTop() + myView.getBottom()) / 2;

            // get the final radius for the clipping circle
            int finalRadius = Math.max(myView.getWidth(), myView.getHeight());

            // create the animator for this view (the start radius is zero)
            Animator anim =
            ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

            // make the view visible and start the animation
            myView.setVisibility(View.VISIBLE);
            anim.start();
        }

        @Override
        public boolean onLongClick(View v) {
            int currentApiVersion = Build.VERSION.SDK_INT;
            if (imageViewParticipantLeft.getVisibility() == View.VISIBLE) {
                if (currentApiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                    animateInvisibility(imageViewParticipantLeft);
                    animateVisibility(imageViewParticipantRight);
                } else {
                    imageViewParticipantLeft.setVisibility(View.GONE);
                    imageViewParticipantRight.setVisibility(View.VISIBLE);
                }
                participantHolderItemLayout.setBackgroundColor(Color.parseColor("#2D27D500"));
            }else{
                if (currentApiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                    animateInvisibility(imageViewParticipantRight);
                    animateVisibility(imageViewParticipantLeft);
                } else {
                    imageViewParticipantLeft.setVisibility(View.VISIBLE);
                    imageViewParticipantRight.setVisibility(View.GONE);
                }
                participantHolderItemLayout.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            }
            return true;
        }
    }

    public EventParticipantsListAdapter (Context context, String[] dataSet) {
        mContext = context;
        this.transformation = new CropCircleTransformation(context);
        mDataSet = dataSet;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(com.globant.eventmanager.R.layout.participant_row_item, parent, false);
        return new ViewHolder(v);    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.getTextViewName().setText(mDataSet[position]);
        Picasso.with(mContext).load(R.drawable.profile_pic).transform(transformation).into(holder.getImageViewParticipantLeft());
        Picasso.with(mContext).load(R.drawable.profile_pic).transform(transformation).into(holder.getImageViewParticipantRight());

    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}
