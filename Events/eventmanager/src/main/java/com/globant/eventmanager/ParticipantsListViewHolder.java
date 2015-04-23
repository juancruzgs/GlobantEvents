package com.globant.eventmanager;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
* Created by paula.baudo on 4/17/2015.
*/
public class ParticipantsListViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

    private final TextView mTextViewName;
    private final TextView mTextViewGlober;
    private final ImageView mImageViewParticipantLeft;
    private final ImageView mImageViewParticipantRight;
    private final LinearLayout mParticipantHolderItemLayout;
    private final FrameLayout mFrameLayoutLeft;
    private final FrameLayout mFrameLayoutRight;
    private final LinearLayout mLinearLayoutHolder;
    private final LinearLayout mLinearLayoutMiddle;


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
        mLinearLayoutMiddle = (LinearLayout) itemView.findViewById(R.id.linear_layout_middle);
    }

    public FrameLayout getmFrameLayoutLeft() {
        return mFrameLayoutLeft;
    }

    private void addTranslateAnimation(final FrameLayout frameLayoutFrom, final FrameLayout frameLayoutTo, LinearLayout linearLayoutHolder, boolean leftToRight) {
        TranslateAnimation translateAnimation;
        float position = frameLayoutFrom.getWidth();
        if (leftToRight) {
            translateAnimation = new TranslateAnimation(frameLayoutFrom.getX(),linearLayoutHolder.getRight()-position
                    ,frameLayoutFrom.getY(),frameLayoutFrom.getY());
        }else{
            translateAnimation = new TranslateAnimation(0, -linearLayoutHolder.getWidth()+position
                    ,frameLayoutFrom.getY(),frameLayoutFrom.getY());
        }

        translateAnimation.setDuration(500);
        translateAnimation.initialize(frameLayoutFrom.getWidth(),
                frameLayoutFrom.getHeight(), linearLayoutHolder.getWidth(), linearLayoutHolder.getHeight());
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                frameLayoutFrom.setVisibility(View.GONE);
                frameLayoutTo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        frameLayoutFrom.startAnimation(translateAnimation);
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



    @Override
    public boolean onLongClick(View v) {
        if (v.findViewById(R.id.frame_layout_left_image).getVisibility() == View.VISIBLE) {
            addTranslateAnimation(mFrameLayoutLeft,mFrameLayoutRight,mLinearLayoutHolder,true);
            mParticipantHolderItemLayout.setBackgroundColor(Color.parseColor("#2D27D500"));
        }else{
            addTranslateAnimation(mFrameLayoutRight,mFrameLayoutLeft,mLinearLayoutHolder,false);
            mParticipantHolderItemLayout.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
        return true;
    }
}
