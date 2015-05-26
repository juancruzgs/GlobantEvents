package com.globant.eventmanager.adapters;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.globant.eventmanager.R;
import com.globant.eventmanager.fragments.EventParticipantsManagerFragment;
import com.globant.eventscorelib.baseAdapters.BaseParticipantListViewHolder;

/**
 * Created by paula.baudo on 4/17/2015.
 */
public class ParticipantsListViewHolderManager extends BaseParticipantListViewHolder implements View.OnTouchListener {

    private final AppCompatTextView mTextViewName;
    private AppCompatTextView mTextViewNameLeft;
    private AppCompatTextView mTextViewGlober;
    private AppCompatTextView mTextViewGloberLeft;
    private AppCompatTextView mTextViewOccupation;
    private AppCompatTextView mTextViewOccupationLeft;
    private final ImageView mImageViewParticipantLeft;
    private final ImageView mImageViewParticipantRight;
    private AppCompatTextView mTextViewLocation;
    private AppCompatTextView mTextViewLocationLeft;
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
    private AppCompatTextView mTextViewPosition;
    private ObjectAnimator mColorTransition;
    private ImageView mImageViewAcceptedIcon;

    public ImageView getImageViewAcceptedIcon() {
        return mImageViewAcceptedIcon;
    }

    public AppCompatTextView getTextViewName() {
        return mTextViewName;
    }

    public AppCompatTextView getTextViewNameLeft() {
        return mTextViewNameLeft;
    }

    public AppCompatTextView getTextViewOccupation() {
        return mTextViewOccupation;
    }

    public AppCompatTextView getTextViewOccupationLeft() {
        return mTextViewOccupationLeft;
    }

    public AppCompatTextView getTextViewGlober() {
        return mTextViewGlober;
    }

    public AppCompatTextView getTextViewGloberLeft() {
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

    public AppCompatTextView getTextViewLocation() {
        return mTextViewLocation;
    }

    public AppCompatTextView getTextViewLocationLeft() {
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
        mTextViewNameLeft = (AppCompatTextView) itemView.findViewById(R.id.text_view_participant_name_left);
        mFrameLayoutLeft = (FrameLayout) itemView.findViewById(R.id.frame_layout_left_image);
        mFrameLayoutRight = (FrameLayout) itemView.findViewById(R.id.frame_layout_right_image);
        mTextViewName = (AppCompatTextView) itemView.findViewById(R.id.text_view_participant_name);
        mTextViewGlober = (AppCompatTextView) itemView.findViewById(R.id.text_view_glober);
        mTextViewGloberLeft = (AppCompatTextView) itemView.findViewById(R.id.text_view_glober_left);
        mImageViewParticipantLeft = (ImageView) itemView.findViewById(R.id.image_view_participant_left);
        mImageViewParticipantRight = (ImageView) itemView.findViewById(R.id.image_view_participant_right);
        mTextViewOccupation = (AppCompatTextView) itemView.findViewById(R.id.text_view_occupation);
        mTextViewOccupationLeft = (AppCompatTextView) itemView.findViewById(R.id.text_view_occupation_left);
        mTextViewLocation = (AppCompatTextView) itemView.findViewById(R.id.text_view_location);
        mTextViewLocationLeft = (AppCompatTextView) itemView.findViewById(R.id.text_view_location_left);
        mParticipantHolderItemLayout = (LinearLayout) itemView.findViewById(R.id.participant_item_holder_layout);
        mLinearLayoutMiddle = (LinearLayout) itemView.findViewById(R.id.linear_layout_middle);
        mFragment = fragment;
        mLinearLayoutMiddleLeft = (LinearLayout) itemView.findViewById(R.id.linear_layout_middle_left);
        mAdapter = adapter;
        mTextViewPosition = (AppCompatTextView) itemView.findViewById(R.id.text_view_position);
        mImageViewAcceptedIcon = (ImageView) itemView.findViewById(R.id.image_view_accepted_icon);
        itemView.setOnTouchListener(this);

    }

    private final Runnable mRunnable = new Runnable() {
        public void run() {
            if ((!mScrolling) && (mBooleanIsPressed)){
                if ((mFrameLayoutRight.getVisibility() == View.VISIBLE) &&
                        (mFrameLayoutLeft.getVisibility() == View.VISIBLE)){
                    Toast.makeText(mFragment.getActivity(), "Participant already accepted", Toast.LENGTH_SHORT).show();
                }else {
                    mRunnableIsRunning = true;
                    startAnimations();
                }
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
                        mFrameLayoutLeft.setVisibility(View.INVISIBLE);
                        mFrameLayoutRight.setVisibility(View.VISIBLE);
                        mFragment.acceptSubscriber(mSubPosition);
                    } else{  //decline
                        linearLayoutMiddleLeft.setVisibility(View.INVISIBLE);
                        linearLayoutMiddle.setVisibility(View.VISIBLE);
                        mFrameLayoutLeft.setVisibility(View.VISIBLE);
                        mFrameLayoutRight.setVisibility(View.INVISIBLE);
                        mFragment.declineSubscriber(mSubPosition);
                    }
                    if (mFragment.isLastVisibleItem()){
                        mFragment.notifyAdapter();
                    }
                } else {
                    mColorTransition.reverse();
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
