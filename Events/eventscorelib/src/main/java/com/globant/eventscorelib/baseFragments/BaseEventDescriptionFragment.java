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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseActivity;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.ConvertImage;
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.CustomDateFormat;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;


public class BaseEventDescriptionFragment extends BaseFragment implements ObservableScrollViewCallbacks, BaseService.ActionListener, BasePagerActivity.FragmentLifecycle{

    boolean mStickyToolbar;
    private View mToolbar;
    private ImageView mEventImage;
    private TextView mEventTitle;
    private TextView mEventStartDate;
    private TextView mEventEndDate;
    private TextView mEventAddress;
    private TextView mEventCity;
    private TextView mEventCountry;
    private TextView mEventLanguage;
    private TextView mEventAdditionalInfo;
    private TextView mEventFullDescription;
    private View mOverlayView;
    private ObservableScrollView mScrollView;
    private int mActionBarSize;
    private int mFlexibleSpaceImageHeight;
    private int mToolbarColor;
    private View mFab;
    private boolean mFabIsShown;
    private int mFlexibleSpaceShowFabOffset;
    private int mFabMargin;
    private boolean mTitleShown = false;

    private Event mEvent;

    public BaseEventDescriptionFragment() {
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return this;
    }

    @Override
    public Activity getBindingActivity() {
        return getActivity();
    }

    @Override
    public String getBindingKey() {
        return "BaseEventDescriptionFragment";
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_description, container, false);
        hideUtilsAndShowContentOverlay(); // REMOVE AFTER TESTING !!!
        wireUpViews(rootView);
        mEvent = BaseApplication.getInstance().getEvent();
        if (mEvent != null) {
            loadEventDescription();
        }
        initializeViewParameters();
        setHasOptionsMenu(true);
        setRetainInstance(true);
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
                mScrollView.scrollTo(0, 1);
                mScrollView.scrollTo(0, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                showProgressOverlay();
                String eventId = data.getStringExtra(CoreConstants.SCAN_RESULT);
                mService.executeAction(BaseService.ACTIONS.SUBSCRIBER_CHECKIN, eventId, getBindingKey());
            }
        }
    }

    private void wireUpViews(View rootView) {
        mToolbar = rootView.findViewById(R.id.toolbar);
        mEventImage = (ImageView) rootView.findViewById(R.id.image);
        mEventTitle = (TextView) rootView.findViewById(R.id.title);
        mEventStartDate = (TextView) rootView.findViewById(R.id.textView_Event_Start_Date);
        mEventEndDate = (TextView) rootView.findViewById(R.id.textView_Event_End_Date);
        mEventAddress = (TextView) rootView.findViewById(R.id.textView_Event_Address);
        mEventCity = (TextView) rootView.findViewById(R.id.textView_Event_City);
        mEventCountry = (TextView) rootView.findViewById(R.id.textView_Event_Country);
        mEventLanguage = (TextView) rootView.findViewById(R.id.textView_Event_Language);
        mEventAdditionalInfo = (TextView) rootView.findViewById(R.id.textView_Event_Additional_Info);
        mEventFullDescription = (TextView) rootView.findViewById(R.id.textView_Event_Full_Description);
        mOverlayView = rootView.findViewById(R.id.overlay);
        mScrollView = (ObservableScrollView) rootView.findViewById(R.id.scroll);
        mFab = rootView.findViewById(R.id.fab);
    }

    @Override
    public String getTitle() {
        return "Description";
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-i, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mEventImage, ScrollUtils.getFloat(-i / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) i / flexibleRange, 0, 1));
        mEventTitle.getBackground().setAlpha(Math.round(255 * (1 - ScrollUtils.getFloat((float) i / flexibleRange, 0, 1))));

        // Scale title text
        float titleHeight = mEventTitle.getHeight();
        float scaleY = (titleHeight - i + flexibleRange) / titleHeight;
        float scale = ScrollUtils.getFloat(scaleY, 0, 1);
        ViewHelper.setPivotX(mEventTitle, 0);
        ViewHelper.setPivotY(mEventTitle, 0);
        //ViewHelper.setScaleX(mEventTitle, scale);
        ViewHelper.setScaleY(mEventTitle, scale);
//        mEventStartDate.setText(String.format("%.02f", scale) + " | " + i +  " | " + titleHeight +  " | " + String.format("%.02f", scaleY));

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mEventTitle.getHeight() * scale);
        int titleTranslationY = maxTitleTranslationY - i;

        //titleTranslationY = Math.max(0, titleTranslationY);
        ViewHelper.setTranslationY(mEventTitle, titleTranslationY);

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

        if (i > mFlexibleSpaceImageHeight && !mTitleShown){
            mTitleShown = true;
            ((BaseActivity) getActivity()).changeFragmentTitle((String) mEventTitle.getText());
        }

        // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFab.getHeight() / 2;
        float fabTranslationY = ScrollUtils.getFloat( -i + mFlexibleSpaceImageHeight - mFab.getHeight() / 2,
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

        // Show-hide FAB
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

    private void postCheckinTweet() {
        if (BaseApplication.getInstance().getSharedPreferencesController()
                .isAlreadyTwitterLogged()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getString(R.string.tweet_checkin)).append(" ")
                    .append(mEvent.getTitle()).append(" ").append(mEvent.getHashtag());
            String tweet = stringBuilder.toString();
            mService.executeAction(BaseService.ACTIONS.TWEET_POST, tweet, getBindingKey());
        } else {
            showCheckinOverlay();
        }
    }

    private void loadEventDescription() {
        mEventTitle.setText(mEvent.getTitle());
        mEventImage.setImageBitmap(ConvertImage.convertByteToBitmap(mEvent.getEventLogo()));
        mEventStartDate.setText(CustomDateFormat.getDate(mEvent.getStartDate(), getActivity()));
        mEventEndDate.setText(CustomDateFormat.getDate(mEvent.getEndDate(), getActivity()));
        mEventAddress.setText(mEvent.getAddress());
        mEventCity.setText(mEvent.getCity());
        mEventCountry.setText(mEvent.getCountry());
        mEventLanguage.setText(mEvent.getLanguage());
        mEventAdditionalInfo.setText(mEvent.getAdditionalInfo());
        mEventFullDescription.setText(mEvent.getFullDescription());
    }


    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {
        showProgressOverlay();
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        switch (theAction){
            case SUBSCRIBER_CHECKIN:
                postCheckinTweet();
                break;
            case TWEET_POST:
                showCheckinOverlay();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        switch (theAction) {
            case SUBSCRIBER_CHECKIN:
                hideUtilsAndShowContentOverlay();
                Toast.makeText(getActivity(), getString(R.string.checkin_error), Toast.LENGTH_SHORT).show();
                break;
            default:
                showErrorOverlay();
                break;
        }
    }

    @Override
    public void onPauseFragment() {
    }

    @Override
    public void onResumeFragment() {
    }
}
