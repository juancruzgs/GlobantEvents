package com.globant.eventmanager.adapters;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
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
    private TextView mTextViewNameLeft;
    private TextView mTextViewGlober;
    private TextView mTextViewGloberLeft;
    private TextView mTextViewOccupation;
    private TextView mTextViewOccupationLeft;
    private final ImageView mImageViewParticipantLeft;
    private final ImageView mImageViewParticipantRight;
    private TextView mTextViewLocation;
    private TextView mTextViewLocationLeft;
    private final LinearLayout mParticipantHolderItemLayout;
    private final FrameLayout mFrameLayoutLeft;
    private final FrameLayout mFrameLayoutRight;
    private final FrameLayout mFrameLayoutHolder;
    private final LinearLayout mLinearLayoutMiddle;
    private LinearLayout mLinearLayoutMiddleLeft;
    private Boolean mScrolling;
    private EventParticipantsManagerFragment mFragment;
    private Boolean mBooleanIsPressed;
    private TranslateAnimation mTranslateAnimationPhoto;
    private TranslateAnimation mTranslateAnimationText;
    private Boolean mAnimationCancelled = false;
    private Boolean mRunnableIsRunning;
    private TouchListenerItem mAdapter;
    private final Handler mHandler = new Handler();
    private int mSubPosition;
    private TextView mTextViewPosition;
    private ObjectAnimator mColorTransition;



    public TextView getTextViewName() {
        return mTextViewName;
    }

    public TextView getTextViewNameLeft() {
        return mTextViewNameLeft;
    }

    public TextView getTextViewOccupation() {
        return mTextViewOccupation;
    }

    public TextView getTextViewOccupationLeft() {
        return mTextViewOccupationLeft;
    }

    public TextView getTextViewGlober() {
        return mTextViewGlober;
    }

    public TextView getTextViewGloberLeft() {
        return mTextViewGloberLeft;
    }

    public ImageView getImageViewParticipantLeft() {
        return mImageViewParticipantLeft;
    }

    public ImageView getImageViewParticipantRight() {
        return mImageViewParticipantRight;
    }

    public Boolean getBooleanIsPressed() {
        return mBooleanIsPressed;
    }

    public TextView getTextViewLocation() {
        return mTextViewLocation;
    }

    public TextView getTextViewLocationLeft() {
        return mTextViewLocationLeft;
    }

    public FrameLayout getFrameLayoutLeft() {
        return mFrameLayoutLeft;
    }

    public FrameLayout getFrameLayoutRight() {
        return mFrameLayoutRight;
    }

    public LinearLayout getLinearLayoutMiddleLeft() {
        return mLinearLayoutMiddleLeft;
    }

    public LinearLayout getLinearLayoutMiddle() {
        return mLinearLayoutMiddle;
    }

    public FrameLayout getFrameLayoutHolder() {
        return mFrameLayoutHolder;
    }

    public TextView getTextViewPosition() {
        return mTextViewPosition;
    }

    public ParticipantsListViewHolderManager(View itemView, EventParticipantsManagerFragment fragment, TouchListenerItem adapter) {
        super(itemView);
        mFrameLayoutHolder = (FrameLayout) itemView.findViewById(R.id.frame_layout_holder);
        mTextViewNameLeft = (TextView) itemView.findViewById(R.id.text_view_participant_name_left);
        mFrameLayoutLeft = (FrameLayout) itemView.findViewById(R.id.frame_layout_left_image);
        mFrameLayoutRight = (FrameLayout) itemView.findViewById(R.id.frame_layout_right_image);
        mTextViewName = (TextView) itemView.findViewById(R.id.text_view_participant_name);
        mTextViewGlober = (TextView) itemView.findViewById(R.id.text_view_glober);
        mTextViewGloberLeft = (TextView) itemView.findViewById(R.id.text_view_glober_left);
        mImageViewParticipantLeft = (ImageView) itemView.findViewById(R.id.image_view_participant_left);
        mImageViewParticipantRight = (ImageView) itemView.findViewById(R.id.image_view_participant_right);
        mTextViewOccupation = (TextView) itemView.findViewById(R.id.text_view_occupation);
        mTextViewOccupationLeft = (TextView) itemView.findViewById(R.id.text_view_occupation_left);
        mTextViewLocation = (TextView) itemView.findViewById(R.id.text_view_location);
        mTextViewLocationLeft = (TextView) itemView.findViewById(R.id.text_view_location_left);
        mParticipantHolderItemLayout = (LinearLayout) itemView.findViewById(R.id.participant_item_holder_layout);
        mLinearLayoutMiddle = (LinearLayout) itemView.findViewById(R.id.linear_layout_middle);
        mFragment = fragment;
        mLinearLayoutMiddleLeft = (LinearLayout) itemView.findViewById(R.id.linear_layout_middle_left);
        mAdapter = adapter;
        mTextViewPosition = (TextView) itemView.findViewById(R.id.text_view_position);
        itemView.setOnTouchListener(this);

    }

    private final Runnable mRunnable = new Runnable() {
        public void run() {
            mRunnableIsRunning = true;
            Logger.d("true");
            if ((!mScrolling) && (mBooleanIsPressed)){
                startAnimations();
            }
        }
    };

    public void startAnimations() {
        if (mFrameLayoutLeft.getVisibility() == View.VISIBLE) {
            acceptAnimation();
        }else{
            declineAnimation();
        }
    }

    public void declineAnimation() {
        addTranslateAnimationPhoto(mFrameLayoutRight, mFrameLayoutLeft, mFrameLayoutHolder, false);
        addTranslateAnimationText(mLinearLayoutMiddle, mLinearLayoutMiddleLeft, mFrameLayoutHolder, false);
        addBackgroundColorAnimation(mFrameLayoutHolder, mFragment.getActivity().getResources().getColor(R.color.globant_green_light), Color.WHITE);
    }

    public void acceptAnimation() {
        addTranslateAnimationPhoto(mFrameLayoutLeft, mFrameLayoutRight, mFrameLayoutHolder, true);
        addTranslateAnimationText(mLinearLayoutMiddle, mLinearLayoutMiddleLeft, mFrameLayoutHolder, true);
        addBackgroundColorAnimation(mFrameLayoutHolder, Color.WHITE, mFragment.getActivity().getResources().getColor(R.color.globant_green_light));
    }

    public float getFrameLayoutWidth(){
        return mFrameLayoutLeft.getWidth();
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
        mTranslateAnimationText = translateAnimation;
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!mAnimationCancelled){
                    mSubPosition = Integer.parseInt(mTextViewPosition.getText().toString());
                    if (leftToRight){ //accept
                        linearLayoutMiddleLeft.setVisibility(View.VISIBLE);
                        linearLayoutMiddle.setVisibility(View.INVISIBLE);
                        mFragment.acceptSubscriber(mSubPosition);
                    } else{  //decline
                        linearLayoutMiddleLeft.setVisibility(View.INVISIBLE);
                        linearLayoutMiddle.setVisibility(View.VISIBLE);
                        mFragment.declineSubscriber(mSubPosition);
                    }
                    if ((mFragment.isAddAll()) || (mFragment.isDeclineAll())){
                        mFragment.notifyAdapter();
                    }
                } else {
                    mAnimationCancelled = false;
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
                mRunnableIsRunning = false;
                if (mAnimationCancelled) {
                    animation.reset();
                    mTranslateAnimationText.cancel();
                    mTranslateAnimationText.reset();
                    mColorTransition.reverse();
                } else {
                    frameLayoutFrom.setVisibility(View.INVISIBLE);
                    frameLayoutTo.setVisibility(View.VISIBLE);
                    if (leftToRight) {
                        //mFrameLayoutHolder.setBackgroundColor(Color.parseColor("#2D27D500"));
                    } else {
                        //mFrameLayoutHolder.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
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

    public void addBackgroundColorAnimation(View view, int colorFrom, int colorTo){
        final ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(view,
                "backgroundColor",
                new ArgbEvaluator(),
                colorFrom,
                colorTo);
        backgroundColorAnimator.setDuration(650);
        mColorTransition = backgroundColorAnimator;
        backgroundColorAnimator.start();
    }




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
                mAdapter.onTouchListenerItem(this);
                mRunnableIsRunning = false;
                Logger.d("false");
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
        if (mRunnableIsRunning) {
            mAnimationCancelled = true;
            mTranslateAnimationPhoto.cancel();
        }
        mHandler.removeCallbacks(mRunnable);
    }

    public interface TouchListenerItem {
        public void onTouchListenerItem(ParticipantsListViewHolderManager participantsListViewHolderManager);
    }
}
