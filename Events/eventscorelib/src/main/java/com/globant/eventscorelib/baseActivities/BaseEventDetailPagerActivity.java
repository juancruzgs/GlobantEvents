package com.globant.eventscorelib.baseActivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.globant.eventscorelib.controllers.CacheObjectsController;

import java.util.List;

import twitter4j.Status;

/**
 * Created by paula.baudo on 5/4/2015.
 */
public class BaseEventDetailPagerActivity extends BasePagerActivity {

    private static BaseEventDetailPagerActivity ourInstance;
    private CacheObjectsController mCacheObjectsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ourInstance = this;
        mCacheObjectsController = new CacheObjectsController();
    }

    public static BaseEventDetailPagerActivity getInstance(){
        return ourInstance;
    }

    public void setTweetList (List<Status> twitterList) {
        mCacheObjectsController.setTweetList(twitterList);
    }

    public List<Status> getTweetList () {
        return mCacheObjectsController.getTweetList();
    }

    @Override
    protected List<Fragment> getFragments() {
        return null;
    }

    @Override
    protected List<String> getTitlesList() {
        return null;
    }
}
