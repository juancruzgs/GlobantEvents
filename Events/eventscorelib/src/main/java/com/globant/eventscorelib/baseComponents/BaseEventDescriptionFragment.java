package com.globant.eventscorelib.baseComponents;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.globant.eventscorelib.R;
import com.nineoldandroids.view.ViewHelper;


public class BaseEventDescriptionFragment extends BaseFragment implements ObservableScrollViewCallbacks {

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    boolean mStickyToolbar;
    private View mToolbar;
    private View mImageView;
    private View mOverlayView;
    private ObservableScrollView mScrollView;
    private TextView mTitleView;
    private int mActionBarSize;
    private int mFlexibleSpaceImageHeight;
    private int mToolbarColor;

    public BaseEventDescriptionFragment() {
    }


    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_description, container, false);

        hideUtilsAndShowContentOverlay();

        //((ActionBarActivity)getActivity()).setSupportActionBar((Toolbar) rootView.findViewById(R.id.toolbar));

        mStickyToolbar = false;

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mActionBarSize = getActionBarSize();
        mToolbarColor = getResources().getColor(R.color.globant_green);

        mToolbar = rootView.findViewById(R.id.toolbar);
        mToolbar.setBackgroundColor(Color.TRANSPARENT);

        mImageView = rootView.findViewById(R.id.image);

        mOverlayView = rootView.findViewById(R.id.overlay);

        mScrollView = (ObservableScrollView) rootView.findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);

        mTitleView = (TextView) rootView.findViewById(R.id.title);
        mTitleView.setText(/*getTitle()*/"La Fiesta del Chori !");

        ScrollUtils.addOnGlobalLayoutListener(mScrollView, new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, 1 );
                mScrollView.scrollTo(0, 0 );
            }
        });

        return rootView;
    }

    @Override
    public String getTitle() {
        return "BaseEventDescriptionFragment";
    }


    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-i, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-i / 2 , minOverlayTransitionY, 0));

        // Change alpha of overlay // getFloat(float value, float minValue, float maxValue)
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) i / flexibleRange, 0, 1));
        mTitleView.getBackground().setAlpha(Math.round( 255 * (1 - ScrollUtils.getFloat((float) i / flexibleRange, 0, 1))));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - i) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        ViewHelper.setPivotX(mTitleView, 0);
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - i;
        //titleTranslationY = Math.max(0, titleTranslationY);
        ViewHelper.setTranslationY(mTitleView, titleTranslationY);

        if (mStickyToolbar) {
            // Change alpha of toolbar background
            if (-i + mFlexibleSpaceImageHeight <= mActionBarSize) {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(1, mToolbarColor));
            } else {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, mToolbarColor));
            }
        } else {
            // Translate Toolbar
            if (i < mFlexibleSpaceImageHeight) {
                ViewHelper.setTranslationY(mToolbar, 0);
            } else {
                ViewHelper.setTranslationY(mToolbar, -i);
            }
        }

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
