package com.globant.eventscorelib.baseComponents;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.fragments.TweetFragment;
import com.globant.eventscorelib.fragments.TwitterStreamFragment;


public class BaseTwitterActivity extends BaseActivity {

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        showProgressOverlay();
        new TwitterLoaderResponse().execute(uri);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_twitter);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().addToBackStack(null)
                    .add(R.id.container, new TwitterStreamFragment())
                    .commit();
        }
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
                initTweetFragment();
//        		String eventname = FTApplication.getInstance().getCacheObjectsManager().event.title;
//        		String aditionalMsg = getString(R.string.initialtweetcomplement);
//        		String tweet = "#FlipThinking "+" "+aditionalMsg; TODO change first tweet
//        		FTApplication.getInstance().getTwitterManager().publishPost(tweet);
            }
        }
    }

    private void initTweetFragment() {
        getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .replace(R.id.container, new TweetFragment())
                .commit();
    }

    @Override
    public String getActivityTitle() {
        return "Twitter";
    }
}
