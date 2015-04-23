package com.globant.eventscorelib.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.BaseTweetActivity;
import com.globant.eventscorelib.adapters.TweetListAdapter;
import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseFragment;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.utils.CoreConstants;
import com.software.shell.fab.ActionButton;

import java.util.List;

import twitter4j.Status;


public abstract class BaseTwitterStreamFragment extends BaseFragment implements BaseService.ActionListener{

    private LayoutManagerType mCurrentLayoutManagerType;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ActionButton mActionButton;
    private List<Status> mTweetList;

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
        showProgressOverlay();
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        switch (theAction) {
            case TWEETS_LIST:
            mTweetList = (List<Status>) result;
            BaseApplication.getInstance().getCacheObjectsManager().tweetList = mTweetList;
            if (mTweetList != null) {
                if (getActivity() == null) return;
                setAdapterRecyclerView();
                hideUtilsAndShowContentOverlay();
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
                showErrorOverlay();
            }
            break;
        }
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public BaseTwitterStreamFragment() {
        // Required empty public constructor
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return BaseTwitterStreamFragment.this;
    }

    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.globant.eventscorelib.R.layout.fragment_twitter_stream, container,
                false);
        hideUtilsAndShowContentOverlay();
        prepareRecyclerView(rootView);
        prepareSwipeRefreshLayout(rootView);
        wireUpFAB(rootView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        if (savedInstanceState != null) {
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(CoreConstants.KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager();
        return rootView;
    }

    @Override
    public String getTitle() {
        return "Stream";
    }

    private void prepareSwipeRefreshLayout(View rootView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BaseApplication.getInstance().getCacheObjectsManager().tweetList = null;
                mService.executeAction(BaseService.ACTIONS.TWEETS_LIST, "#GameOfThrones"); // TODO: put the event hashtag
            }
        });
    }

    private void prepareRecyclerView(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.tweet_list_recycler_view);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if ((newState == RecyclerView.SCROLL_STATE_DRAGGING) || (newState == RecyclerView.SCROLL_STATE_SETTLING)) {
                    mActionButton.hide();
                } else {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        mActionButton.show();
                    }
                }
            }
        });
    }

    private void wireUpFAB(View rootView) {
        mActionButton = (ActionButton) rootView.findViewById(R.id.action_button);
        mActionButton.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        mActionButton.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), getTweetActivityClass());
                startActivity(intent);
            }
        });
    }

    protected abstract Class<? extends BaseTweetActivity> getTweetActivityClass();

    public void setRecyclerViewLayoutManager() {
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
    public void onResume() {
        super.onResume();
        mTweetList = BaseApplication.getInstance().getCacheObjectsManager().tweetList;
        if (mTweetList != null) {
            setAdapterRecyclerView();
        }
    }

    private void setAdapterRecyclerView() {
        TweetListAdapter mAdapter = new TweetListAdapter(mTweetList, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setService(BaseService service) {
        super.setService(service);
        mTweetList = BaseApplication.getInstance().getCacheObjectsManager().tweetList;
        if (mTweetList == null) {
            mService.executeAction(BaseService.ACTIONS.TWEETS_LIST, "GameOfThrones"); // TODO: put the event hashtag
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(CoreConstants.KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }
}
