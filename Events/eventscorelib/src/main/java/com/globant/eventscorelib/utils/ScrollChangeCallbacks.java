package com.globant.eventscorelib.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.globant.eventscorelib.baseActivities.BaseActivity;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.software.shell.fab.ActionButton;

/**
 * Created by juan.soler on 5/20/2015.
 */
public class ScrollChangeCallbacks implements ObservableScrollViewCallbacks {

    private int mActionBarSize;
    private int mFlexibleSpaceImageHeight;
    private int mToolbarColor;
    private boolean mFabIsShown = false;
    private int mFlexibleSpaceShowFabOffset;
    private int mFabMargin;
    private boolean mStickyToolbar;
    private View mToolbar;
    private View mOverlayView;
    private AppCompatTextView mTitle;
    private ImageView mPhoto;
    private ActionButton mFloatingActionButton;
    private Context mContext;

    public ScrollChangeCallbacks(int actionBarSize, int flexibleSpaceImageHeight, int toolbarColor, int flexibleSpaceShowFabOffset, int fabMargin, View toolBar,
                                 View overlayView, AppCompatTextView title, ImageView photo, ActionButton floatingActionButton, boolean stickyToolbar, Context context) {
        mActionBarSize = actionBarSize;
        mFlexibleSpaceImageHeight = flexibleSpaceImageHeight;
        mToolbarColor = toolbarColor;
        mFlexibleSpaceShowFabOffset = flexibleSpaceShowFabOffset;
        mFabMargin = fabMargin;
        mToolbar = toolBar;
        mToolbar.setBackgroundColor(Color.TRANSPARENT);
        mOverlayView = overlayView;
        mTitle = title;
        mPhoto = photo;
        mStickyToolbar = stickyToolbar;
        mFloatingActionButton = floatingActionButton;
        mContext = context;
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-i, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mPhoto, ScrollUtils.getFloat(-i / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) i / flexibleRange, 0, 1));
        mTitle.getBackground().setAlpha(Math.round(255 * (1 - ScrollUtils.getFloat((float) i / flexibleRange, 0, 1))));

        // Scale title text
        float titleHeight = mTitle.getHeight();
        float scaleY = (titleHeight - i + flexibleRange) / titleHeight;
        float scale = ScrollUtils.getFloat(scaleY, 0, 1);
        ViewHelper.setPivotX(mTitle, 0);
        ViewHelper.setPivotY(mTitle, 0);
        ViewHelper.setScaleY(mTitle, scale);

        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitle.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - i;

        ViewHelper.setTranslationY(mTitle, titleTranslationY);

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

        if (i > mFlexibleSpaceImageHeight){
            ((BaseActivity) mContext).changeFragmentTitle(mTitle.getText().toString());
        }

        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFloatingActionButton.getHeight() / 2;
        float fabTranslationY = ScrollUtils.getFloat( -i + mFlexibleSpaceImageHeight - mFloatingActionButton.getHeight() / 2,
                mActionBarSize - mFloatingActionButton.getHeight() / 2,
                maxFabTranslationY);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // On pre-honeycomb, ViewHelper.setTranslationX/Y does not set margin,
            // which causes FAB's OnClickListener not working.
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFloatingActionButton.getLayoutParams();
            lp.leftMargin = mOverlayView.getWidth() - mFabMargin - mFloatingActionButton.getWidth();
            lp.topMargin = (int) fabTranslationY;
            mFloatingActionButton.requestLayout();
        } else {
            ViewHelper.setTranslationX(mFloatingActionButton, mOverlayView.getWidth() - mFabMargin - mFloatingActionButton.getWidth());
            ViewHelper.setTranslationY(mFloatingActionButton, fabTranslationY);
        }

        // Show-hide FAB
        if (fabTranslationY < mFlexibleSpaceShowFabOffset-mActionBarSize) {
            hideFab();
        } else {
            showFab();
        }
    }

    private void showFab() {
        if (!mFabIsShown) {
            mFloatingActionButton.setVisibility(View.VISIBLE);
            ViewPropertyAnimator.animate(mFloatingActionButton).cancel();
            ViewPropertyAnimator.animate(mFloatingActionButton).scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            mFloatingActionButton.setVisibility(View.GONE);
            ViewPropertyAnimator.animate(mFloatingActionButton).cancel();
            ViewPropertyAnimator.animate(mFloatingActionButton).scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
