package com.globant.eventscorelib;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.globant.eventscorelib.baseComponents.BaseActivity;
import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.fragments.TweetFragment;


public abstract class BaseTweetActivity extends BaseActivity implements BaseService.ActionListener {

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().addToBackStack(null)
                    .replace(R.id.container, new TweetFragment())
                    .commit();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = getIntent().getData();
        if (uri != null) {
            getService().executeAction(BaseService.ACTIONS.TWITTER_LOADER_RESPONSE, uri);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Uri uri = getIntent().getData();
//        if (uri != null) {
//            getService().executeAction(BaseService.ACTIONS.TWITTER_LOADER_RESPONSE, uri);
//        }
//    }




//    private class TwitterLoaderResponse extends AsyncTask<Uri, Void, Boolean> {
//        @Override
//        protected Boolean doInBackground(Uri... params) {
//            return BaseApplication.getInstance().getTwitterManager().getLoginResponse(params[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Boolean response) {
//            super.onPostExecute(response);
//            if (response) {
//                //    showProgressOverlay();
//                mTweetFragment.changeUserInformation();
////        		String eventname = FTApplication.getInstance().getCacheObjectsManager().event.title; // TODO get event title
////        		String aditionalMsg = getString(R.string.initialtweetcomplement);
////        		String tweet = "#FlipThinking "+" "+aditionalMsg; TODO change first tweet
////        		FTApplication.getInstance().getTwitterManager().publishPost(tweet);
//            }
//        }
//    }

    @Override
    public String getActivityTitle() {
        return "Twitter";
    }
}
