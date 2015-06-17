package com.globant.eventscorelib.baseFragments;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareDialog;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseActivity;
import com.globant.eventscorelib.baseActivities.BaseCreditsActivity;
import com.globant.eventscorelib.baseActivities.BaseEventListActivity;
import com.globant.eventscorelib.baseActivities.BaseSubscriberActivity;
import com.globant.eventscorelib.baseAdapters.BaseEventsListAdapter;
import com.globant.eventscorelib.baseAdapters.GetEventInformation;
import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.controllers.SharedPreferencesController;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.BaseEventListActionListener;
import com.globant.eventscorelib.utils.CoreConstants;
import com.globant.eventscorelib.utils.PushNotifications;
import com.globant.eventscorelib.utils.CustomDateFormat;
import com.globant.eventscorelib.utils.JSONSharedPreferences;
import com.globant.eventscorelib.utils.Logger;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nineoldandroids.view.ViewHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class BaseEventListFragment extends BaseFragment implements ObservableScrollViewCallbacks, GetEventInformation {

    private static final String TAG = "EventListFragment";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mEventId;
    private String mSubscriberMail;

    private BaseService.ActionListener mActionListener;
    private String mBindingKey;

    private static final String KEY_WAITING = "KEY_WAITING";
    private boolean mWaitingForList = false;
    public static boolean mIsDataSetChanged = false;

    public void updateEventList(List<Event> eventsList) {
        mEventList = eventsList;
        if (mEventList != null) {
            mRecyclerView.setAdapter(getAdapter());
        } else {
            showErrorOverlay();
        }
        hideUtilsAndShowContentOverlay();
        ((BaseEventListActivity) getActivity()).setEventList(mEventList);
        scrollTo(CoreConstants.SCROLL_TOP);

        try {
            JSONObject eventArray = JSONSharedPreferences.loadJSONObject(BaseApplication.getInstance(),
                    BaseApplication.getInstance().getApplicationInfo().name, JSONSharedPreferences.KEY_CALENDAR);

            for (Event event : eventsList) {
                String eventId = event.getObjectID();
                if (eventArray.has(eventId)) {
                    JSONObject eventJSON = eventArray.getJSONObject(eventId);
                    long calendarId = eventJSON.getInt(CoreConstants.CALENDAR_SELF_ID);
                    if (!mService.existsCalendar(calendarId)) {
                        JSONSharedPreferences.removeEvent(getActivity(), event);
                    }

                    Date lastUpdateDb = event.getUpdatedAt();
                    String lastUpdateCalStr = (String) eventJSON.get(CoreConstants.CALENDAR_EVENT_LAST_UPDATE);
                    // Thu Jun 11 15:37:48 GMT-03:00 2015
                    //DateFormat dateFormat = DateFormat.getDateTimeInstance();
                    //Date lastUpdateCal = dateFormat.parse(lastUpdateCalStr);
                    Date lastUpdateCal = CustomDateFormat.parseCompleteDate(lastUpdateCalStr, getActivity());
                    if (lastUpdateCal.before(lastUpdateDb)) {
                        mService.executeAction(BaseService.ACTIONS.UPDATE_EVENT_IN_CALENDAR, mActionListener.getBindingKey(),
                                eventJSON.getInt(CoreConstants.CALENDAR_SELF_ID),
                                eventJSON.getLong(CoreConstants.CALENDAR_EVENT_ID), event);
                    }
                }
            }
        }
        catch (JSONException e) {
            Logger.e("Problems with the internal event info while trying to update the event", e);
        }
        catch (NullPointerException e) {
            Logger.e("Problems getting the calendar event's last update date", e);
        }

        mWaitingForList = false;
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void updateEventListFail() {
        mSwipeRefreshLayout.setRefreshing(false);
        hideUtilsAndShowContentOverlay();
    }

    public void removeEventFromCalendar(String bindingKey, Long eventId) {
        mService.executeAction(BaseService.ACTIONS.REMOVE_EVENT_FROM_CALENDAR, bindingKey,
                                /*calendarData.getInt(CoreConstants.CALENDAR_SELF_ID),*/
                eventId);
    }

    protected enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER,
        STAGGEREDGRID_LAYOUT_MANAGER
    }

    private LayoutManagerType mCurrentLayoutManagerType;
    protected ObservableRecyclerView mRecyclerView;
    private List<Event> mEventList;

    protected abstract int getFragmentLayout();

    protected abstract boolean getIsGlober();

    protected abstract BaseEventsListAdapter getAdapter();

    protected int getEventListRecyclerView() {
        return R.id.list_recycler_view;
    }

    public List<Event> getEventList() {
        return mEventList;
    }

    public BaseEventListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBindingKey = this.getClass().getSimpleName();// + new Date().toString();
        if (savedInstanceState != null) {
            mWaitingForList = savedInstanceState.getBoolean(KEY_WAITING, false);
        }
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
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshEventList();
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
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState.getSerializable(CoreConstants.KEY_LAYOUT_MANAGER);
        }
        RecyclerView.LayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new StaggeredGridLayoutManager(2, 1);
            mCurrentLayoutManagerType = LayoutManagerType.STAGGEREDGRID_LAYOUT_MANAGER;
        } else {
            layoutManager = new LinearLayoutManager(getActivity());
            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(CoreConstants.KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        savedInstanceState.putBoolean(KEY_WAITING, mWaitingForList);
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
                int speed = 2;
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    speed = 7;
                }
                movementY = ScrollUtils.getFloat((cardY - (childHeight * 3)) * (-z * speed), -(childHeight * ((Math.round(height / childHeight)) - 1)), 10);
                movementX = ScrollUtils.getFloat((cardY - (childHeight * 3)) * (-z * speed), -(childHeight * ((Math.round(height / childHeight)) - 1)), 0);

                // Translations
                ViewHelper.setTranslationY(titleView, movementY);
                ViewHelper.setTranslationX(titleView, (-movementX) / 2.5f);
                ViewHelper.setTranslationY(dateView, movementY / 2);
                ViewHelper.setTranslationX(dateView, -(movementX * 1.5f));
                ViewHelper.setTranslationX(locationView, -(movementX * 3));

                // Alphas
                float alpha = ScrollUtils.getFloat((cardY - (childHeight * 3)) * (z * speed), 0, 255) / 64;
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

    public void postCheckinTweet(Event event) {
        if (BaseApplication.getInstance().getSharedPreferencesController().isAlreadyTwitterLogged()) {
            String tweet = getString(R.string.tweet_checkin) + " " + event.getTitle() + " " + event.getHashtag();
            mService.executeAction(BaseService.ACTIONS.TWEET_POST, mBindingKey, tweet);
        } else {
            showCheckinOverlay();
        }
    }

    @Override
    public void setService(BaseService service) {
        super.setService(service);
        showProgressOverlay();
        mActionListener = mService.getActionListener(mBindingKey);
        if (mActionListener == null) {
            mActionListener = new BaseEventListActionListener();
            ((BaseEventListActionListener) mActionListener).setActivity((BaseActivity) getActivity());
            ((BaseEventListActionListener) mActionListener).setBindingKey(mBindingKey);
            mService.subscribeActor(mActionListener);
        }

        ((BaseEventListActionListener) mActionListener).setFragment(this);
        mEventList = ((BaseEventListActivity) getActivity()).getEventList();
        if (mEventList == null) {
            if (!mWaitingForList) {
                boolean isOnline = ((BaseActivity) getActivity()).isOnline();
                mService.executeAction(BaseService.ACTIONS.EVENT_LIST, mBindingKey, getIsGlober(), isOnline);
                mWaitingForList = true;
            }
        } else {
            mRecyclerView.setAdapter(getAdapter());
            hideUtilsAndShowContentOverlay();
        }
        if (mEventId != null) {
            mService.executeAction(BaseService.ACTIONS.SUBSCRIBER_CHECKIN, mBindingKey,
                    mEventId, mSubscriberMail);
        }
    }

    @Override
    public void onResume() {
        if (mService != null && mIsDataSetChanged) {
            mSwipeRefreshLayout.setRefreshing(true);
            refreshEventList();
            BaseEventListFragment.mIsDataSetChanged = false;
        }
        super.onResume();
    }

    private void refreshEventList() {
        mService.executeAction(BaseService.ACTIONS.EVENTS_LIST_REFRESH, mBindingKey, getIsGlober());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (data != null && data.hasExtra(CoreConstants.FIELD_EVENTS)) {
            mEventList.add((Event) data.getParcelableExtra(CoreConstants.FIELD_EVENTS));
        }

        if (scanResult != null) {
            showProgressOverlay();
            mEventId = scanResult.getContents();
            mSubscriberMail = SharedPreferencesController.getUserEmail(getActivity());
            if (mService != null) {
                mService.executeAction(BaseService.ACTIONS.SUBSCRIBER_CHECKIN, mBindingKey,
                        mEventId, mSubscriberMail);
                PushNotifications.unsubscribeToChannel(getString(R.string.prefix_participants) + mEventId);
                PushNotifications.subscribeToChannel(getString(R.string.prefix_checkin) + mEventId);
            }
            //Else do the action when the service is available }
        }
    }

    @Override
    public Event getEvent(int position) {
        return mEventList.get(position);
    }

    public void scrollTo(String position) {
        int itemCount = mRecyclerView.getAdapter().getItemCount();
        if (itemCount > 0) {
            final int start, stop;
            if (position.equals(CoreConstants.SCROLL_TOP)) {
                start = itemCount - 1;
                stop = 0;
            } else {
                start = 0;
                stop = itemCount - 1;
            }
            mRecyclerView.scrollToPosition(start);
            ScrollUtils.addOnGlobalLayoutListener(mRecyclerView, new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.smoothScrollToPosition(stop);
                }
            });
        }
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
                getActivity().overridePendingTransition(R.anim.top_in, R.anim.nothing);
                handled = true;
            } else {
                if (id == R.id.action_checkin) {
                    IntentIntegrator intentIntegrator = IntentIntegrator.forSupportFragment(this);
                    intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                    intentIntegrator.initiateScan();
                    handled = true;
                } else {
                    if (id == R.id.menu_refresh){
                        mSwipeRefreshLayout.setRefreshing(true);
                        refreshEventList();
                        handled = true;
                    }
                }
            }
        }

        if (!handled) {
            handled = super.onOptionsItemSelected(item);
        }
        return handled;
    }
}
