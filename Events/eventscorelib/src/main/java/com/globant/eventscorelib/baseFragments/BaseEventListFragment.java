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

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseCreditsActivity;
import com.globant.eventscorelib.baseActivities.BaseSubscriberActivity;
import com.globant.eventscorelib.baseAdapters.BaseEventsListAdapter;
import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseListeners.GetEventInformation;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.utils.CoreConstants;
import com.nineoldandroids.view.ViewHelper;

import java.util.List;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public abstract class BaseEventListFragment extends BaseFragment implements ObservableScrollViewCallbacks, BaseService.ActionListener, GetEventInformation {

    private static final String TAG = "EventListFragment";
    private SwipeRefreshLayout mSwipeRefreshLayout;
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

    public BaseEventListFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                mService.executeAction(BaseService.ACTIONS.EVENT_LIST, getIsGlober(), getBindingKey());
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
        int scrollPosition = CoreConstants.ZERO;
        if (savedInstanceState != null) {
            mCurrentLayoutManagerType = (LayoutManagerType)savedInstanceState.getSerializable(CoreConstants.KEY_LAYOUT_MANAGER);
        }
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);

        ScrollUtils.addOnGlobalLayoutListener(mRecyclerView, new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollToPosition(1);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(CoreConstants.KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

        if (mRecyclerView.getChildCount() > 0) {
            float height = mRecyclerView.getChildAt(0).getHeight();
            float childHeight = mRecyclerView.getChildAt(0).findViewById(R.id.event_title_text_view).getHeight();

            float z = childHeight / height;
            float movementY, movementX;

            for (int n = 0; n < mRecyclerView.getChildCount(); n++) {

                View cardView = mRecyclerView.getChildAt(n);
                View titleView = mRecyclerView.getChildAt(n).findViewById(R.id.event_title_text_view);
                View dateView = mRecyclerView.getChildAt(n).findViewById(R.id.event_date_text_view);
                View locationView = mRecyclerView.getChildAt(n).findViewById(R.id.event_location_text_view);

                // Set translation movement
                float cardY = cardView.getY();
                movementY = ScrollUtils.getFloat((cardY - (childHeight * 3)) * (-z * 2), -(childHeight * ((Math.round(height/childHeight))-1)), 10);
                movementX = ScrollUtils.getFloat((cardY - (childHeight * 3)) * (-z * 2), -(childHeight * ((Math.round(height/childHeight))-1)), 0);

                // Translate Title
                ViewHelper.setTranslationY(titleView, movementY);
                ViewHelper.setTranslationX(titleView, (-movementX) / 2.5f);

                //Translate Date
                ViewHelper.setTranslationY(dateView, movementY / 2);
                ViewHelper.setTranslationX(dateView, -(movementX * 1.5f));

                // Alpha of Date
                float alpha = ScrollUtils.getFloat(cardY * z, 0, 255);
                ViewHelper.setAlpha(mRecyclerView.getChildAt(n).findViewById(R.id.event_date_text_view), 1 - (alpha / 128));

                // Translate Location
                ViewHelper.setTranslationX(locationView, -(movementX * 3));

                //Alpha of Location
                ViewHelper.setAlpha(locationView, 1 - (alpha / 128));

                //((TextView) titleView).setText(String.format("%.02f", movementY) + " | " + String.format("%.02f", cardY) + " | " + childHeight + " | " + height);
            }
        }
    }

    @Override
    public void onDownMotionEvent() {}

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {}

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
            }
        }

        if (!handled) {
            handled = super.onOptionsItemSelected(item);
        }
        return handled;
    }

    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {}

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        switch (theAction) {
            case EVENT_LIST:
                mEventList = (List<Event>) result;
                if (mEventList != null) {
                    mRecyclerView.setAdapter(getAdapter());
                } else {
                    showErrorOverlay();
                }
                mSwipeRefreshLayout.setRefreshing(false);
                break;
        }
        hideUtilsAndShowContentOverlay();
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }

    @Override
    public void setService(BaseService service) {
        super.setService(service);
        mService.executeAction(BaseService.ACTIONS.EVENT_LIST, getIsGlober(), getBindingKey());
        showProgressOverlay();
    }

    @Override
    public void getEvent(int position) {
        Event event = mEventList.get(position);
        BaseApplication.getInstance().setEvent(event);
    }

    @Override
    public Activity getBindingActivity() {
        return getActivity();
    }
}
