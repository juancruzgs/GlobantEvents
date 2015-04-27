package com.globant.eventscorelib.baseFragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseActivity;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.utils.CoreConstants;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;


public class BaseEventDescriptionFragment extends BaseFragment implements ObservableScrollViewCallbacks, BaseService.ActionListener {

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
//    String mTitle;
    private View mFab;
    private boolean mFabIsShown;
    private int mFlexibleSpaceShowFabOffset;
    private int mFabMargin;
    private boolean mTitleShown = false;

    public BaseEventDescriptionFragment() {
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return this;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_description, container, false);
        hideUtilsAndShowContentOverlay(); // REMOVE AFTER TESTING !!!
        wireUpViews(rootView);
        initializeViewParameters();
        setHasOptionsMenu(true);
        return rootView;
    }

    private void initializeViewParameters() {
        //((ActionBarActivity)getActivity()).setSupportActionBar((Toolbar) rootView.findViewById(R.id.toolbar));
        mActionBarSize = getActionBarSize();
        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
        mToolbarColor = getResources().getColor(R.color.globant_green);
        mStickyToolbar = false;
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mToolbar.setBackgroundColor(Color.TRANSPARENT);
        mScrollView.setScrollViewCallbacks(this);
        mTitleView.setText("La Fiesta del Chori !");

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Refactor with functionality, first subscribe, then check-in
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, new SubscriberFragment())
//                        .addToBackStack(null).commit();
                Intent intentScan = new Intent(CoreConstants.INTENT_SCAN);
                startActivityForResult(intentScan,0);
            }
        });

        mFabMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        ViewHelper.setScaleX(mFab, 0);
        ViewHelper.setScaleY(mFab, 0);

        ScrollUtils.addOnGlobalLayoutListener(mScrollView, new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, 1 );
                mScrollView.scrollTo(0, 0 );
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                showProgressOverlay();
                String eventId = data.getStringExtra(CoreConstants.SCAN_RESULT);
                mService.executeAction(BaseService.ACTIONS.SUBSCRIBER_CHECKIN, eventId);
            }
        }
    }

    private void wireUpViews(View rootView) {
        mToolbar = rootView.findViewById(R.id.toolbar);
        mImageView = rootView.findViewById(R.id.image);
        mOverlayView = rootView.findViewById(R.id.overlay);
        mScrollView = (ObservableScrollView) rootView.findViewById(R.id.scroll);
        mTitleView = (TextView) rootView.findViewById(R.id.title);
        mFab = rootView.findViewById(R.id.fab);
    }

    @Override
    public String getTitle() {
        return "Description";
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

        //PLEASE DON'T EXRACT METHODS YET !, I'LL DO IT WHEN ITS FINISHED. (FP)
        //PLEASE DON'T EXRACT METHODS YET !, I'LL DO IT WHEN ITS FINISHED. (FP)
        //PLEASE DON'T EXRACT METHODS YET !, I'LL DO IT WHEN ITS FINISHED. (FP)
        //PLEASE DON'T EXRACT METHODS YET !, I'LL DO IT WHEN ITS FINISHED. (FP)

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

//        if (i < mFlexibleSpaceImageHeight){
//            mTitle = "";
//            ((BaseActivity)getActivity()).changeFragmentTitle(mTitle);
//        }
//        else {
//            mTitle = "La Fiesta del Chori !";
//            ((BaseActivity)getActivity()).changeFragmentTitle(mTitle);
//        }

        if (i > mFlexibleSpaceImageHeight && !mTitleShown){
            mTitleShown = true;
            ((BaseActivity)getActivity()).changeFragmentTitle("La Fiesta del Chori !");
        }

        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFab.getHeight() / 2;
        float fabTranslationY = ScrollUtils.getFloat( -50
                -i + mFlexibleSpaceImageHeight - mFab.getHeight() / 2,
                mActionBarSize - mFab.getHeight() / 2,
                maxFabTranslationY);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // On pre-honeycomb, ViewHelper.setTranslationX/Y does not set margin,
            // which causes FAB's OnClickListener not working.
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFab.getLayoutParams();
            lp.leftMargin = mOverlayView.getWidth() - mFabMargin - mFab.getWidth();
            lp.topMargin = (int) fabTranslationY;
            mFab.requestLayout();
        } else {
            ViewHelper.setTranslationX(mFab, mOverlayView.getWidth() - mFabMargin - mFab.getWidth());
            ViewHelper.setTranslationY(mFab, fabTranslationY);
        }

        // Show/hide FAB
        if (fabTranslationY < mFlexibleSpaceShowFabOffset-mActionBarSize) {
            hideFab();
        } else {
            showFab();
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_event_description_fragment, menu);
    }

    private void showFab() {
        if (!mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            ViewPropertyAnimator.animate(mFab).cancel();
            ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }


    @Override
    public Activity getBindingActivity() {
        return getActivity();
    }

    @Override
    public Object getBindingKey() {
        return null;
    }

    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {

    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        switch (theAction){
            case SUBSCRIBER_CHECKIN:
                showCheckinOverlay();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        hideUtilsAndShowContentOverlay();
        Toast.makeText(getActivity(), getString(R.string.checkin_error),Toast.LENGTH_SHORT).show();
    }
}
