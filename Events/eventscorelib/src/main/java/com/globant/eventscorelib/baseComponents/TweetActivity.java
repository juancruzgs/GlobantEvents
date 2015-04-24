package com.globant.eventscorelib.baseComponents;

import android.content.Intent;
import android.os.Bundle;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.fragments.TweetFragment;


public class TweetActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new TweetFragment())
                    .commit();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        NewFragmentIntent fragment = (NewFragmentIntent)getSupportFragmentManager().findFragmentById(R.id.container);
        fragment.onNewIntent(intent);
    }

    public interface NewFragmentIntent {
        public void onNewIntent(Intent intent);
    }
}