package com.globant.eventscorelib.baseFragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseEventDetailPagerActivity;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseAdapters.BaseParticipantsListAdapter;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Subscriber;
import com.globant.eventscorelib.utils.CoreConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gonzalo.lodi on 5/14/2015.
 */
public abstract class BaseParticipantsFragment extends BaseFragment implements BasePagerActivity.FragmentLifecycle, BaseService.ActionListener{

    private static final String TAG = "EventParticipantsFragment";
    private List<Subscriber> mSubscribers;
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected ObservableRecyclerView mRecyclerView;
    protected BaseParticipantsListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected Boolean scrolling = false;
    private Event mEvent;
    private AppCompatTextView mTextViewNoSubscribers;
    private String mBindingKey;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public Event getEvent() {
        return mEvent;
    }

    public List<Subscriber> getSubscribers() {
        return mSubscribers;
    }

    public ObservableRecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public Activity getBindingActivity() {
        return getActivity();
    }

    @Override
    public String getBindingKey() {
        return mBindingKey;
    }

    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {
        switch (theAction) {
            case PARTICIPANT_LIST:
                if (!mSwipeRefreshLayout.isRefreshing()){
                    showProgressOverlay();
                }
                break;
        }
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        switch ( theAction ) {
            case PARTICIPANT_LIST:
                mSubscribers = (List<Subscriber>) result;
                ((BaseEventDetailPagerActivity) getActivity()).setSubscriberList(mSubscribers);
                setRecyclerViewAdapter();
                if (mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                    mAdapter.notifyDataSetChanged();
                }else {
                    hideUtilsAndShowContentOverlay();
                }
                hideUtilsAndShowContentOverlay();
                initializeAcceptedSubscribers();
                break;
        }
    }

    protected abstract void initializeAcceptedSubscribers();

    protected abstract BaseParticipantsListAdapter getAdapter ();

    protected void setRecyclerViewAdapter() {
        if (mSubscribers.size() == 0){
            mTextViewNoSubscribers.setVisibility(View.VISIBLE);
        } else {
            mAdapter = getAdapter();
            mRecyclerView.setAdapter(mAdapter);
            showHint();
        }
    }

    protected abstract void showHint();

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    public Boolean getScrolling() {
        return scrolling;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBindingKey = this.getClass().getSimpleName() + new Date().toString();
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return this;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getFragmentLayout(), container, false);
        hideUtilsAndShowContentOverlay();
        rootView.setTag(TAG);
        setRetainInstance(true);
        prepareSwipeRefreshLayout(rootView);
        wireUpViews(savedInstanceState, rootView);
        return rootView;
    }

    protected abstract int getFragmentLayout();

    private void wireUpViews(Bundle savedInstanceState, View rootView) {
        mRecyclerView = (ObservableRecyclerView) rootView.findViewById(R.id.list_recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        if (savedInstanceState != null) {
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(CoreConstants.KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        mTextViewNoSubscribers = (AppCompatTextView) rootView.findViewById(R.id.text_view_no_participants);

    }

    protected abstract void cancelAnimationOnRefresh();

    private void prepareSwipeRefreshLayout(View rootView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(com.globant.eventscorelib.R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cancelAnimationOnRefresh();
                refreshParticipants();
            }
        });
    }

    protected void refreshParticipants() {
        mService.executeAction(BaseService.ACTIONS.SET_ACCEPTED, getBindingKey(), mEvent.getObjectID(), mSubscribers);
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public String getTitle() {
        return "Participants";
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(CoreConstants.KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onPauseFragment() {
    }

    @Override
    public void onResumeFragment(){
        mSubscribers = ((BaseEventDetailPagerActivity) getActivity()).getSubscriberList();
        if (mSubscribers == null) {
            mEvent = ((BaseEventDetailPagerActivity) getActivity()).getEvent();
            String eventId = mEvent.getObjectID();
            mService.executeAction(BaseService.ACTIONS.PARTICIPANT_LIST, getBindingKey(), eventId);
        }
        else {
            setRecyclerViewAdapter();
        }
    }

}
