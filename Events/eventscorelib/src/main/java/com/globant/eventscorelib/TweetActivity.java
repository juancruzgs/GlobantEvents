package com.globant.eventscorelib;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.globant.eventscorelib.baseComponents.BaseActivity;
import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.fragments.TweetFragment;


public class TweetActivity extends BaseActivity {

    TweetFragment mTweetFragment;

    @Override
    protected Class<? extends BaseService> getServiceClass() {
        return BaseService.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        if (savedInstanceState == null) {
            mTweetFragment = new TweetFragment();
            getSupportFragmentManager().beginTransaction().addToBackStack(null)
                    .replace(R.id.container, mTweetFragment)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        Uri uri = getIntent().getData();
        if (uri!= null) {
            showProgressOverlay();
        new TwitterLoaderResponse().execute(uri); }
        super.onResume();
    }

    private class TwitterLoaderResponse extends AsyncTask<Uri, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Uri... params) {
            return BaseApplication.getInstance().getTwitterManager().getLoginResponse(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean response) {
            super.onPostExecute(response);
            if (response) {
                //    showProgressOverlay();
                mTweetFragment.changeUserInformation();
//        		String eventname = FTApplication.getInstance().getCacheObjectsManager().event.title; // TODO get event title
//        		String aditionalMsg = getString(R.string.initialtweetcomplement);
//        		String tweet = "#FlipThinking "+" "+aditionalMsg; TODO change first tweet
//        		FTApplication.getInstance().getTwitterManager().publishPost(tweet);
            }
        }
    }

    @Override
    public String getActivityTitle() {
        return "Twitter";
    }
}
