package com.globant.eventscorelib;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseFragment;
import com.globant.eventscorelib.utils.Logger;

import java.util.List;

import twitter4j.Status;


public class TwitterStreamFragment extends BaseFragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private TweetAdapter mAdapter;
    private List<Status> tweetList;

    public TwitterStreamFragment() {
        // Required empty public constructor
    }


    @Override
    protected View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_twitter_stream, container,
                false);
        mListView = (ListView) rootView.findViewById(R.id.list);
        prepareSwipeRefresgLayout(rootView);
        hideUtilsAndShowContentOverlay();
        return rootView;
    }

    private void prepareSwipeRefresgLayout(View rootView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BaseApplication.getInstance().getCacheObjectsManager().tweetList = null;
                new TweetsLoader().execute("");
            }
        });
    }

    @Override
    public String getTitle() {
        return "Twitter Stream";
    }


    @Override
    public void onResume() {
        new TweetsLoader().execute("");
        super.onResume();
    }


    private class TweetsLoader extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            tweetList = BaseApplication.getInstance().getCacheObjectsManager().tweetList;
            try {
                if (tweetList == null) {
                    tweetList = BaseApplication.getInstance().getTwitterManager().getTweetList();
                    BaseApplication.getInstance().getCacheObjectsManager().tweetList = tweetList;
                }
            } catch (Exception e) {
                Logger.e("LOADING TWITTER", e);
            }
            if (tweetList != null) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result && tweetList!=null) {
                if (getActivity() == null) return;
                mAdapter = new TweetAdapter(getActivity(), tweetList);
                mListView.setAdapter(mAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
                hideUtilsAndShowContentOverlay();
            } else {
                showErrorOverlay();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

    }

}
