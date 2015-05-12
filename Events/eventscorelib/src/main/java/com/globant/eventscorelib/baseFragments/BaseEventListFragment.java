package com.globant.eventscorelib.baseFragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseCreditsActivity;
import com.globant.eventscorelib.baseActivities.BaseEventListActivity;
import com.globant.eventscorelib.baseActivities.BaseSubscriberActivity;
import com.globant.eventscorelib.baseAdapters.BaseEventsListAdapter;
import com.globant.eventscorelib.baseAdapters.BaseEventsListViewHolder;
import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.controllers.SharedPreferencesController;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.CoreConstants;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nineoldandroids.view.ViewHelper;

import java.util.Date;
import java.util.List;

public abstract class BaseEventListFragment extends BaseFragment implements ObservableScrollViewCallbacks, BaseService.ActionListener, BaseEventsListViewHolder.GetEventInformation {

    private static final String TAG = "EventListFragment";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mEventId;
    private String mSubscriberMail;

    private String mBindingKey;

    protected enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    private LayoutManagerType mCurrentLayoutManagerType;
    private ObservableRecyclerView mRecyclerView;
    private List<Event> mEventList;

    protected abstract int getFragmentLayout();
    protected abstract boolean getIsGlober();
    protected abstract BaseEventsListAdapter getAdapter();

    protected int getEventListRecyclerView() {
        return R.id.event_list_recycler_view;
    }

    public ObservableRecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public List<Event> getEventList() {
        return mEventList;
    }

    public BaseEventListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBindingKey = this.getClass().getSimpleName() + new Date().toString();
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getFragmentLayout(), container, false);
        rootView.setTag(TAG);
        prepareRecyclerView(rootView);
        prepareSwipeRefreshLayout(rootView);
        setRecyclerViewLayoutManager(savedInstanceState);
        hideUtilsAndShowContentOverlay();
        setHasOptionsMenu(true);
        return rootView;
    }

    private void prepareRecyclerView(View rootView) {
        mRecyclerView = (ObservableRecyclerView) rootView.findViewById(getEventListRecyclerView());
        mRecyclerView.setScrollViewCallbacks(this);
    }

    private void prepareSwipeRefreshLayout(View rootView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.events_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mService.executeAction(BaseService.ACTIONS.EVENT_LIST, getBindingKey(), getIsGlober());
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public String getTitle() {
//        return getString(R.string.title_fragment_events_stream);
        return "";
    }

    public void setRecyclerViewLayoutManager(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mCurrentLayoutManagerType = (LayoutManagerType)savedInstanceState.getSerializable(CoreConstants.KEY_LAYOUT_MANAGER);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(CoreConstants.KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

        if (mRecyclerView.getChildCount() > 0) {

            for (int n = 0; n < mRecyclerView.getChildCount(); n++) {

                float height = mRecyclerView.getChildAt(n).getHeight();
                float childHeight = mRecyclerView.getChildAt(n).findViewById(R.id.event_title_text_view).getHeight();
                float z = childHeight / height;
                float movementY, movementX, cardY;

                View cardView = mRecyclerView.getChildAt(n);
                View titleView = mRecyclerView.getChildAt(n).findViewById(R.id.event_title_text_view);
                View dateView = mRecyclerView.getChildAt(n).findViewById(R.id.event_date_text_view);
                View locationView = mRecyclerView.getChildAt(n).findViewById(R.id.event_location_text_view);
                View TypeLogoView = mRecyclerView.getChildAt(n).findViewById(R.id.imageView_Event_Type_Logo);

                // Set translation movement
                cardY = cardView.getY();
                movementY = ScrollUtils.getFloat((cardY - (childHeight * 3)) * (-z * 2), -(childHeight * ((Math.round(height / childHeight)) - 1)), 10);
                movementX = ScrollUtils.getFloat((cardY - (childHeight * 3)) * (-z * 2), -(childHeight * ((Math.round(height / childHeight)) - 1)), 0);

                // Translations
                ViewHelper.setTranslationY(titleView, movementY);
                ViewHelper.setTranslationX(titleView, (-movementX) / 2.5f);
                ViewHelper.setTranslationY(dateView, movementY / 2);
                ViewHelper.setTranslationX(dateView, -(movementX * 1.5f));
                ViewHelper.setTranslationX(locationView, -(movementX * 3));

                // Alphas
                float alpha = ScrollUtils.getFloat((cardY - (childHeight * 3)) * (z * 2), 0, 255) / 64;
                ViewHelper.setAlpha(dateView, 1 - (alpha));
                ViewHelper.setAlpha(locationView, 1 - (alpha));
                ViewHelper.setAlpha(TypeLogoView, 1 - (alpha));

            }
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
        inflater.inflate(R.menu.menu_event_list_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean handled = false;

        if (id == R.id.action_credits) {
            Intent intentCredits = new Intent(getActivity(), BaseCreditsActivity.class);
            startActivity(intentCredits);
            handled = true;
        } else {
            if (id == R.id.action_profile) {
                Intent intentSubscriber = new Intent(getActivity(), BaseSubscriberActivity.class);
                startActivity(intentSubscriber);
                handled = true;
            } else {
                if (id == R.id.action_checkin) {
                    IntentIntegrator intentIntegrator = IntentIntegrator.forSupportFragment(this);
                    intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                    intentIntegrator.initiateScan();
                    handled = true;
                }
            }
        }

        if (!handled) {
            handled = super.onOptionsItemSelected(item);
        }
        return handled;
    }

    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        switch (theAction) {
            case EVENT_LIST:
                mEventList = (List<Event>) result;
                if (mEventList != null) {
                    ((BaseEventListActivity)getActivity()).setEventList(mEventList);
                    mRecyclerView.setAdapter(getAdapter());
                } else {
                    showErrorOverlay();
                }
                mSwipeRefreshLayout.setRefreshing(false);
                hideUtilsAndShowContentOverlay();
                break;
            case SUBSCRIBER_CHECKIN:
                postCheckinTweet((Event) result);
                break;
            case TWEET_POST:
                showCheckinOverlay();
                break;
            default:
                hideUtilsAndShowContentOverlay();
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

    private void postCheckinTweet(Event event) {
        if (BaseApplication.getInstance().getSharedPreferencesController().isAlreadyTwitterLogged()) {
            String tweet = getString(R.string.tweet_checkin) + " " + event.getTitle() + " " + event.getHashtag();
            mService.executeAction(BaseService.ACTIONS.TWEET_POST, getBindingKey(), tweet);
        } else {
            showCheckinOverlay();
        }
    }

    @Override
    public void setService(BaseService service) {
        super.setService(service);
        showProgressOverlay();
        mEventList = ((BaseEventListActivity)getActivity()).getEventList();
        if (mEventList == null) {
            mService.executeAction(BaseService.ACTIONS.EVENT_LIST, getBindingKey(), getIsGlober());
        } else {
            mRecyclerView.setAdapter(getAdapter());
            hideUtilsAndShowContentOverlay();
        }
        // TODO: See how the mCheckInParameters[] can be better used
        if (mEventId != null) {
            mService.executeAction(BaseService.ACTIONS.SUBSCRIBER_CHECKIN, getBindingKey(),
                    mEventId, mSubscriberMail);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            showProgressOverlay();
            mEventId = scanResult.getContents();
            mSubscriberMail = SharedPreferencesController.getUserEmail(getActivity());
            if (mService != null) {
                mService.executeAction(BaseService.ACTIONS.SUBSCRIBER_CHECKIN, getBindingKey(),
                        mEventId, mSubscriberMail);
            }
            //Else do the action when the service is available }
        }
    }

    @Override
    public Event getEvent(int position) {
        return mEventList.get(position);
    }

    @Override
    public Activity getBindingActivity() {
        return getActivity();
    }

    @Override
    public String getBindingKey() {
        return mBindingKey;
    }


}
