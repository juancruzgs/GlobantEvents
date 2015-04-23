package com.globant.eventmanager;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by paula.baudo on 4/17/2015.
 */
public class ParticipantsListViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

    private final TextView mTextViewName;
    private final TextView mTextViewGlober;
    private final ImageView mImageViewParticipantLeft;
    private final ImageView mImageViewParticipantRight;
    private final LinearLayout mParticipantHolderItemLayout;
    private final FrameLayout mFrameLayoutLeft;
    private final FrameLayout mFrameLayoutRight;
    private final LinearLayout mLinearLayoutHolder;
    private final LinearLayout mLinearLayoutMiddle;
    private Boolean mScrolling;
    private EventParticipantsFragment mFragment;
    private Boolean mBooleanIsPressed;
    private TranslateAnimation mTranslateAnimationPhoto;
    private TranslateAnimation mTranslateAnimationText;
    private Boolean mAnimationCancelled;
    private View mView;
    private Boolean mRunnableIsRunning;

    private final Handler mHandler = new Handler();
    private final Runnable mRunnable = new Runnable() {
        public void run() {
            mRunnableIsRunning = true;
            if ((!mScrolling) && (mBooleanIsPressed)){
                if (mView.findViewById(R.id.frame_layout_left_image).getVisibility() == View.VISIBLE) {
                    addTranslateAnimationPhoto(mFrameLayoutLeft, mFrameLayoutRight, mLinearLayoutHolder, true);
                    addTranslateAnimationText(mLinearLayoutMiddle,mLinearLayoutHolder,true);
                }else{
                    addTranslateAnimationPhoto(mFrameLayoutRight, mFrameLayoutLeft, mLinearLayoutHolder, false);
                    addTranslateAnimationText(mLinearLayoutMiddle,mLinearLayoutHolder,false);
                }
            }
        }
    };

    public ParticipantsListViewHolder(View itemView, EventParticipantsFragment fragment) {
        super(itemView);
        //itemView.setOnLongClickListener(this);
        mLinearLayoutHolder = (LinearLayout) itemView.findViewById(R.id.linear_layout_holder);
        mFrameLayoutLeft = (FrameLayout) itemView.findViewById(R.id.frame_layout_left_image);
        mFrameLayoutRight = (FrameLayout) itemView.findViewById(R.id.frame_layout_right_image);
        mTextViewName = (TextView) itemView.findViewById(R.id.text_view_participant_name);
        mTextViewGlober = (TextView) itemView.findViewById(R.id.text_view_glober);
        mImageViewParticipantLeft = (ImageView) itemView.findViewById(R.id.image_view_participant_left);
        mImageViewParticipantRight = (ImageView) itemView.findViewById(R.id.image_view_participant_right);
        mParticipantHolderItemLayout = (LinearLayout) itemView.findViewById(R.id.participant_item_holder_layout);
        mLinearLayoutMiddle = (LinearLayout) itemView.findViewById(R.id.linear_layout_middle);
        mFragment = fragment;
        itemView.setOnTouchListener(this);
    }

    public float getFrameLayoutWidth(){
        return mFrameLayoutLeft.getWidth();
    }

    public FrameLayout getmFrameLayoutLeft() {
        return mFrameLayoutLeft;
    }

    public void addTranslateAnimationText(LinearLayout linearLayoutMiddle, LinearLayout linearLayoutHolder, Boolean leftToRight){
        TranslateAnimation translateAnimation;
        if (leftToRight){
            translateAnimation = new TranslateAnimation(0, -getFrameLayoutWidth(), 0, 0);
        }else{
            translateAnimation = new TranslateAnimation(0, getFrameLayoutWidth()+2, 0, 0);
        }
        translateAnimation.setDuration(650);
        translateAnimation.initialize(linearLayoutMiddle.getWidth(), linearLayoutMiddle.getHeight(),
                linearLayoutHolder.getWidth(), linearLayoutHolder.getHeight());
        mTranslateAnimationText = translateAnimation;
        linearLayoutMiddle.startAnimation(translateAnimation);

    }

    public void addTranslateAnimationPhoto(final FrameLayout frameLayoutFrom, final FrameLayout frameLayoutTo, LinearLayout linearLayoutHolder,
                                           final boolean leftToRight) {
        TranslateAnimation translateAnimation;
        if (leftToRight) {
            translateAnimation = new TranslateAnimation(frameLayoutFrom.getX(), linearLayoutHolder.getRight() - getFrameLayoutWidth()
                    , frameLayoutFrom.getY(), frameLayoutFrom.getY());
        } else {
            translateAnimation = new TranslateAnimation(0, -linearLayoutHolder.getWidth() + getFrameLayoutWidth()
                    , frameLayoutFrom.getY(), frameLayoutFrom.getY());
        }

        translateAnimation.setDuration(650);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        translateAnimation.initialize(frameLayoutFrom.getWidth(),
                frameLayoutFrom.getHeight(), linearLayoutHolder.getWidth(), linearLayoutHolder.getHeight());
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mAnimationCancelled){
                    animation.reset();
                    mTranslateAnimationText.cancel();
                    mTranslateAnimationText.reset();
                }else {
                    frameLayoutFrom.setVisibility(View.GONE);
                    frameLayoutTo.setVisibility(View.VISIBLE);
                    if (leftToRight){
                        mParticipantHolderItemLayout.setBackgroundColor(Color.parseColor("#2D27D500"));
                    }else{
                        mParticipantHolderItemLayout.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    }
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mTranslateAnimationPhoto = translateAnimation;
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



    /*@Override
    public boolean onLongClick(View v) {
        if (v.findViewById(R.id.frame_layout_left_image).getVisibility() == View.VISIBLE) {
            addTranslateAnimationPhoto(mFrameLayoutLeft,mFrameLayoutRight,mLinearLayoutHolder,true);
            mParticipantHolderItemLayout.setBackgroundColor(Color.parseColor("#2D27D500"));

        }else{
            addTranslateAnimationPhoto(mFrameLayoutRight,mFrameLayoutLeft,mLinearLayoutHolder,false);
            mParticipantHolderItemLayout.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
        return true;
    }*/

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mScrolling = mFragment.getScrolling();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mBooleanIsPressed) {
                mBooleanIsPressed = false;
                mAnimationCancelled = true;
                if (mRunnableIsRunning) {
                    mTranslateAnimationPhoto.cancel();
                }
                mHandler.removeCallbacks(mRunnable);
                return true;
            }
            return false;
        } else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Execute your Runnable after 5000 milliseconds = 5 seconds.
                mRunnableIsRunning = false;
                mView = v;
                mHandler.postDelayed(mRunnable, 500);
                mAnimationCancelled = false;
                mBooleanIsPressed = true;
                return true;
            }
            return false;
        }
    }
}
