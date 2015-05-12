package com.globant.eventscorelib.baseFragments;


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
import android.widget.TextView;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseEventDetailPagerActivity;
import com.globant.eventscorelib.baseActivities.BasePagerActivity;
import com.globant.eventscorelib.baseActivities.BaseTweetActivity;
import com.globant.eventscorelib.baseAdapters.BaseTweetListAdapter;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.software.shell.fab.ActionButton;

import java.util.Date;
import java.util.List;

import twitter4j.Status;


public class BaseTwitterStreamFragment extends BaseFragment implements BaseService.ActionListener, BasePagerActivity.FragmentLifecycle {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ActionButton mActionButton;
    private List<Status> mTweetList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTextViewNoTweets;
    private String mBindingKey;

    public BaseTwitterStreamFragment() {
        // Required empty public constructor
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
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        if (theAction == BaseService.ACTIONS.TWEETS_LIST){
            mTweetList = (List<Status>) result;
            if (mTweetList != null) {
                ((BaseEventDetailPagerActivity) getActivity()).setTweetList(mTweetList);
                setRecyclerViewAdapter();
            } else {
                showErrorOverlay();
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
        hideUtilsAndShowContentOverlay();
    }

    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }

    @Override
    public BaseService.ActionListener getActionListener() {
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBindingKey = this.getClass().getSimpleName() + new Date().toString();
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
        mTextViewNoTweets = (TextView)rootView.findViewById(R.id.text_view_no_tweets);
        setRecyclerViewLayoutManager();
        setRetainInstance(true);
        return rootView;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_fragment_tweets_stream);
    }

    private void prepareSwipeRefreshLayout(View rootView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((BaseEventDetailPagerActivity) getActivity()).setTweetList(null);
                mService.executeAction(BaseService.ACTIONS.TWEETS_LIST, getBindingKey(), "#GameOfThrones"); // TODO: put the event hashtag
                mSwipeRefreshLayout.setRefreshing(true);
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
                Intent intent = new Intent(getActivity(), BaseTweetActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    private void setRecyclerViewAdapter() {
        BaseTweetListAdapter adapter = new BaseTweetListAdapter(mTweetList, getActivity());
        mRecyclerView.setAdapter(adapter);
        if (mTweetList.size()<1){
            mTextViewNoTweets.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPauseFragment() {
    }

    @Override
    public void onResumeFragment() {
        mTweetList = ((BaseEventDetailPagerActivity) getActivity()).getTweetList();
        if (mTweetList == null) {
            mService.executeAction(BaseService.ACTIONS.TWEETS_LIST, getBindingKey(), "GameOfThrones"); // TODO: put the event hashtag
            showProgressOverlay();
        }
        else {
            setRecyclerViewAdapter();
        }
    }
}
