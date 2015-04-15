package com.globant.eventscorelib;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseFragment;
import com.globant.eventscorelib.utils.Logger;
import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;


public class TwitterStreamFragment extends BaseFragment {

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView mRecyclerView;
    protected TweetListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    ActionButton mActionButton;
    private List<Status> mTweetList;
    private AsyncTask<Void, Void, Boolean> mTweetsLoader;
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }


    public TwitterStreamFragment() {
        // Required empty public constructor
    }


    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_twitter_stream, container,
                false);
        hideUtilsAndShowContentOverlay();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.tweet_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        wireUpFAB(rootView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        if (savedInstanceState != null) {
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager();
        return rootView;
    }

    private void wireUpFAB(View rootView) {
        mActionButton = (ActionButton) rootView.findViewById(R.id.action_button);
        mActionButton.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        mActionButton.setHideAnimation(ActionButton.Animations.ROLL_TO_DOWN);
    }

    public void setRecyclerViewLayoutManager() {
        int scrollPosition = 0;

        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        mLayoutManager = new LinearLayoutManager(getActivity());
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if ((newState == RecyclerView.SCROLL_STATE_DRAGGING) || (newState == RecyclerView.SCROLL_STATE_SETTLING)){
                    mActionButton.hide();
                }else{
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        mActionButton.show();
                    }
                }
            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }


    @Override
    public String getTitle() {
        return null;
    }


    @Override
    public void onResume() {
        super.onResume();
        mTweetsLoader = new TweetsLoader().execute();
    }


    private class TweetsLoader extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            mTweetList = BaseApplication.getInstance().getCacheObjectsManager().tweetList;
            try {
                if (mTweetList == null) {
                    mTweetList = BaseApplication.getInstance().getTwitterManager().getTweetList();
                    BaseApplication.getInstance().getCacheObjectsManager().tweetList = mTweetList;
                }
            } catch (Exception e) {
                Logger.e("LOADING TWITTER", e);
            }
            return (mTweetList != null);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result && mTweetList !=null) {
                if (getActivity() == null) return;
                mAdapter = new TweetListAdapter(mTweetList, getActivity());
                mRecyclerView.setAdapter(mAdapter);
                hideUtilsAndShowContentOverlay();
            } else {
                showErrorOverlay();
            }
        }
   }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStop() {
        if (mTweetsLoader != null && mTweetsLoader.getStatus() == AsyncTask.Status.RUNNING) {
            mTweetsLoader.cancel(false);
        }
        super.onStop();
    }

    // TODO onStop in TweetFragment
}
