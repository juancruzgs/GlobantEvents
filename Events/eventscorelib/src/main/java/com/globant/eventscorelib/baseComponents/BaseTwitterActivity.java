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
        new TwitterLoaderResponse().execute(uri);

        getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .replace(com.globant.eventscorelib.R.id.container, new TweetFragment())
                .commit();
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

    @Override
    protected void onResume() {
        super.onResume();
        Uri uri = getIntent().getData();
        new TwitterLoaderResponse().execute(uri);
    }

    private class TwitterLoaderResponse extends AsyncTask<Uri, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Uri... params) {
            boolean response = BaseApplication.getInstance().getTwitterManager().getLoginResponse(params[0]);
            return response;
        }

        @Override
        protected void onPostExecute(Boolean response) {
            super.onPostExecute(response);
            if (response) {
//        		String eventname = FTApplication.getInstance().getCacheObjectsManager().event.title;
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

    @Override
    public String getFragmentTitle(BaseFragment fragment) {
        return fragment.getTitle();
    }
}
