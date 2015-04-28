package com.globant.eventmanager.adapters;

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

import com.globant.eventmanager.R;
import com.globant.eventmanager.fragments.EventParticipantsManagerFragment;
import com.globant.eventscorelib.utils.Logger;

/**
 * Created by paula.baudo on 4/17/2015.
 */
public class ParticipantsListViewHolderManager extends RecyclerView.ViewHolder implements View.OnTouchListener {

    private final TextView mTextViewName;
    private final TextView mTextViewGlober;
    private final ImageView mImageViewParticipantLeft;
    private final ImageView mImageViewParticipantRight;
    private final LinearLayout mParticipantHolderItemLayout;
    private final FrameLayout mFrameLayoutLeft;
    private final FrameLayout mFrameLayoutRight;
    private final FrameLayout mFrameLayoutHolder;
    private final LinearLayout mLinearLayoutMiddle;
    private Boolean mScrolling;
    private EventParticipantsManagerFragment mFragment;
    private Boolean mBooleanIsPressed;
    private TranslateAnimation mTranslateAnimationPhoto;
    private TranslateAnimation mTranslateAnimationText;
    private Boolean mAnimationCancelled;
    private View mView;
    private Boolean mRunnableIsRunning;
    private LinearLayout mLinearLayoutMiddleLeft;
    private TextView mTextViewNameLeft;

    private final Handler mHandler = new Handler();
    private final Runnable mRunnable = new Runnable() {
        public void run() {
            mRunnableIsRunning = true;
            Logger.d("true");
            if ((!mScrolling) && (mBooleanIsPressed)){
                if (mView.findViewById(R.id.frame_layout_left_image).getVisibility() == View.VISIBLE) {
                    addTranslateAnimationPhoto(mFrameLayoutLeft, mFrameLayoutRight, mFrameLayoutHolder, true);
                    addTranslateAnimationText(mLinearLayoutMiddle, mLinearLayoutMiddleLeft, mFrameLayoutHolder,true);
                }else{
                    addTranslateAnimationPhoto(mFrameLayoutRight, mFrameLayoutLeft, mFrameLayoutHolder, false);
                    addTranslateAnimationText(mLinearLayoutMiddle, mLinearLayoutMiddleLeft, mFrameLayoutHolder,false);
                }
            }
        }
    };

    public Boolean getmBooleanIsPressed() {
        return mBooleanIsPressed;
    }

    public TextView getmTextViewNameLeft() {
        return mTextViewNameLeft;
    }

    public ParticipantsListViewHolderManager(View itemView, EventParticipantsManagerFragment fragment) {
        super(itemView);
        mFrameLayoutHolder = (FrameLayout) itemView.findViewById(R.id.relative_layout_holder);
        mTextViewNameLeft = (TextView) itemView.findViewById(R.id.text_view_participant_name_left);
        mFrameLayoutLeft = (FrameLayout) itemView.findViewById(R.id.frame_layout_left_image);
        mFrameLayoutRight = (FrameLayout) itemView.findViewById(R.id.frame_layout_right_image);
        mTextViewName = (TextView) itemView.findViewById(R.id.text_view_participant_name);
        mTextViewGlober = (TextView) itemView.findViewById(R.id.text_view_glober);
        mImageViewParticipantLeft = (ImageView) itemView.findViewById(R.id.image_view_participant_left);
        mImageViewParticipantRight = (ImageView) itemView.findViewById(R.id.image_view_participant_right);
        mParticipantHolderItemLayout = (LinearLayout) itemView.findViewById(R.id.participant_item_holder_layout);
        mLinearLayoutMiddle = (LinearLayout) itemView.findViewById(R.id.linear_layout_middle);
        mFragment = fragment;
        mLinearLayoutMiddleLeft = (LinearLayout) itemView.findViewById(R.id.linear_layout_middle_left);
        itemView.setOnTouchListener(this);

    }

    public float getFrameLayoutWidth(){
        return mFrameLayoutLeft.getWidth();
    }

    public LinearLayout getmLinearLayoutMiddle() {
        return mLinearLayoutMiddle;
    }

    public void addTranslateAnimationText(final LinearLayout linearLayoutMiddle, final LinearLayout linearLayoutMiddleLeft, final FrameLayout frameLayoutHolder, final Boolean leftToRight){
        TranslateAnimation translateAnimation;
        LinearLayout currentAnimated;
        if (leftToRight){
            translateAnimation = new TranslateAnimation(0, -mFrameLayoutLeft.getWidth(), 0, 0);
            currentAnimated = linearLayoutMiddle;
        }else{
            translateAnimation = new TranslateAnimation(0, mFrameLayoutLeft.getWidth(), 0, 0);
            currentAnimated = linearLayoutMiddleLeft;
        }
        translateAnimation.setDuration(650);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        translateAnimation.initialize(linearLayoutMiddle.getWidth(), linearLayoutMiddle.getHeight(),
                frameLayoutHolder.getWidth(), frameLayoutHolder.getHeight());
        mTranslateAnimationText = translateAnimation;
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!mAnimationCancelled){
                    if (leftToRight){
                        linearLayoutMiddle.setVisibility(View.INVISIBLE);
                        linearLayoutMiddleLeft.setVisibility(View.VISIBLE);
                    } else{
                        linearLayoutMiddleLeft.setVisibility(View.INVISIBLE);
                        linearLayoutMiddle.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        currentAnimated.startAnimation(translateAnimation);

    }

    public void addTranslateAnimationPhoto(final FrameLayout frameLayoutFrom, final FrameLayout frameLayoutTo, FrameLayout frameLayoutHolder,
                                           final boolean leftToRight) {
        TranslateAnimation translateAnimation;
        if (leftToRight) {
            translateAnimation = new TranslateAnimation(frameLayoutFrom.getX(), frameLayoutHolder.getRight() - getFrameLayoutWidth()
                    , frameLayoutFrom.getY(), frameLayoutFrom.getY());
        } else {
            translateAnimation = new TranslateAnimation(0, -frameLayoutHolder.getWidth() + getFrameLayoutWidth()
                    , frameLayoutFrom.getY(), frameLayoutFrom.getY());
        }

        translateAnimation.setDuration(650);
        translateAnimation.setInterpolator(new DecelerateInterpolator());
        translateAnimation.initialize(frameLayoutFrom.getWidth(),
                frameLayoutFrom.getHeight(), frameLayoutHolder.getWidth(), frameLayoutHolder.getHeight());
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mAnimationCancelled) {
                    animation.reset();
                    mTranslateAnimationText.cancel();
                    mTranslateAnimationText.reset();
                } else {
                    frameLayoutFrom.setVisibility(View.INVISIBLE);
                    frameLayoutTo.setVisibility(View.VISIBLE);
                    if (leftToRight) {
                        mLinearLayoutMiddle.setX(-getFrameLayoutWidth());
                        mParticipantHolderItemLayout.setBackgroundColor(Color.parseColor("#2D27D500"));
                    } else {
                        mLinearLayoutMiddle.setX(getFrameLayoutWidth());
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
            addTranslateAnimationPhoto(mFrameLayoutLeft,mFrameLayoutRight,mFrameLayoutHolder,true);
            mParticipantHolderItemLayout.setBackgroundColor(Color.parseColor("#2D27D500"));

        }else{
            addTranslateAnimationPhoto(mFrameLayoutRight,mFrameLayoutLeft,mFrameLayoutHolder,false);
            mParticipantHolderItemLayout.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
        return true;
    }*/

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mScrolling = mFragment.getScrolling();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mBooleanIsPressed) {
                cancelAnimations();
                return true;
            }
            return false;
        } else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Execute your Runnable after 5000 milliseconds = 5 seconds.
                mRunnableIsRunning = false;
                Logger.d("false");
                mView = v;
                mHandler.postDelayed(mRunnable, 500);
                mAnimationCancelled = false;
                mBooleanIsPressed = true;
                return true;
            }
            return false;
        }
    }

    public void cancelAnimations() {
        mBooleanIsPressed = false;
        mAnimationCancelled = true;
        if (mRunnableIsRunning) {
            mTranslateAnimationPhoto.cancel();
        }
        mHandler.removeCallbacks(mRunnable);
    }
}
