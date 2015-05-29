package com.globant.eventscorelib.baseActivities;

import android.content.Intent;
import android.os.Bundle;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseFragments.BaseTweetFragment;
import com.globant.eventscorelib.controllers.CacheObjectsController;
import com.globant.eventscorelib.utils.BaseEasterEggsBasket;
import com.globant.eventscorelib.utils.CoreConstants;

import twitter4j.User;

public class BaseTweetActivity extends BaseActivity {

    private static BaseTweetActivity ourInstance;
    private CacheObjectsController mCacheObjectsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        ourInstance = this;
        if (savedInstanceState != null) {
            mCacheObjectsController = savedInstanceState.getParcelable(CoreConstants.SAVE_INSTANCE_CACHE_OBJECTS);
        } else {
            mCacheObjectsController = new CacheObjectsController();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new BaseTweetFragment())
                    .commit();
        }
    }


    public static BaseTweetActivity getInstance() {
        return ourInstance;
    }

    public void setTwitterUser(User user) {
        ourInstance.mCacheObjectsController.setUser(user);
    }

    public User getTwitterUser() {
        return ourInstance.mCacheObjectsController.getUser();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        NewFragmentIntent fragment = (NewFragmentIntent)getSupportFragmentManager().findFragmentById(R.id.container);
        fragment.onNewIntent(intent);
    }

    public interface NewFragmentIntent {
        void onNewIntent(Intent intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CoreConstants.SAVE_INSTANCE_CACHE_OBJECTS, mCacheObjectsController);
    }

    @Override
    protected boolean usesEgg() {
        return false;
    }

    @Override
    protected BaseEasterEggsBasket.EASTEREGGS whichEgg() {
        return null;
    }
}