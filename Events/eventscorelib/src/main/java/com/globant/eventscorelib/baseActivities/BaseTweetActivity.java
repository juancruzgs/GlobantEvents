package com.globant.eventscorelib.baseActivities;

import android.content.Intent;
import android.os.Bundle;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseActivity;
import com.globant.eventscorelib.baseFragments.BaseTweetFragment;


public class BaseTweetActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new BaseTweetFragment())
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