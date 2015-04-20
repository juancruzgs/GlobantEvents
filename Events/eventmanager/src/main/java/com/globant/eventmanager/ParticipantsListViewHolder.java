package com.globant.eventmanager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

/**
* Created by paula.baudo on 4/17/2015.
*/
public class ParticipantsListViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

    private final TextView mTextViewName;
    private final TextView mTextViewGlober;
    private final ImageView mImageViewParticipantLeft;
    private final ImageView mImageViewParticipantRight;
    private final LinearLayout mParticipantHolderItemLayout;
    private final TranslateAnimation mTranslateAnimation;
    private final FrameLayout mFrameLayoutLeft;
    private final FrameLayout mFrameLayoutRight;
    private final LinearLayout mLinearLayoutHolder;

    public ParticipantsListViewHolder(View itemView) {
        super(itemView);
        itemView.setOnLongClickListener(this);
        mLinearLayoutHolder = (LinearLayout) itemView.findViewById(R.id.linear_layout_holder);
        mFrameLayoutLeft = (FrameLayout) itemView.findViewById(R.id.frame_layout_left_image);
        mFrameLayoutRight = (FrameLayout) itemView.findViewById(R.id.frame_layout_right_image);
        mTextViewName = (TextView) itemView.findViewById(R.id.text_view_participant_name);
        mTextViewGlober = (TextView) itemView.findViewById(R.id.text_view_glober);
        mImageViewParticipantLeft = (ImageView) itemView.findViewById(R.id.image_view_participant_left);
        mImageViewParticipantRight = (ImageView) itemView.findViewById(R.id.image_view_participant_right);
        mParticipantHolderItemLayout = (LinearLayout) itemView.findViewById(R.id.participant_item_holder_layout);
        mTranslateAnimation = new TranslateAnimation(mFrameLayoutLeft.getX(),900f
                ,mFrameLayoutLeft.getY(),mFrameLayoutLeft.getY());
        mTranslateAnimation.setDuration(1000);
        mTranslateAnimation.initialize(mFrameLayoutLeft.getWidth(),
                mFrameLayoutLeft.getHeight(), mLinearLayoutHolder.getWidth(), mLinearLayoutHolder.getHeight());
        mTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mFrameLayoutLeft.setVisibility(View.INVISIBLE);
                mImageViewParticipantRight.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public TextView getTextViewName() {
        return mTextViewName;
    }

    public TextView getTextViewGlober() {
        return mTextViewGlober;
    }

    public ImageView getImageViewParticipantLeft() {
        return mImageViewParticipantLeft;
    }

    public ImageView getImageViewParticipantRight() {
        return mImageViewParticipantRight;
    }

    private void animateInvisibility(final View myView) {
        int cx = (myView.getLeft() + myView.getRight()) / 2;
        int cy = (myView.getTop() + myView.getBottom()) / 2;

        int initialRadius = myView.getWidth();
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                myView.setVisibility(View.GONE);
            }
        });

        anim.start();
    }

    private void animateVisibility(View myView) {
        int cx = (myView.getLeft() + myView.getRight()) / 2;
        int cy = (myView.getTop() + myView.getBottom()) / 2;
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
        myView.setVisibility(View.VISIBLE);
        anim.start();
    }

    @Override
    public boolean onLongClick(View v) {
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (mImageViewParticipantLeft.getVisibility() == View.VISIBLE) {
            /*if (currentApiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                animateInvisibility(mImageViewParticipantLeft);
                animateVisibility(mImageViewParticipantRight);
            } else {
                mImageViewParticipantLeft.setVisibility(View.GONE);
                mImageViewParticipantRight.setVisibility(View.VISIBLE);
            }*/
            mFrameLayoutLeft.startAnimation(mTranslateAnimation);
            mParticipantHolderItemLayout.setBackgroundColor(Color.parseColor("#2D27D500"));
        }else{
            if (currentApiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                animateInvisibility(mImageViewParticipantRight);
                animateVisibility(mImageViewParticipantLeft);
            } else {
                mImageViewParticipantLeft.setVisibility(View.VISIBLE);
                mImageViewParticipantRight.setVisibility(View.GONE);
            }
            mParticipantHolderItemLayout.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
        return true;
    }
}
