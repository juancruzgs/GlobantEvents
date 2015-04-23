package com.globant.eventmanager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.globant.eventscorelib.BaseTweetActivity;
import com.globant.eventscorelib.baseComponents.BaseService;


public class TweetManagerActivity extends BaseTweetActivity {

    @Override
    protected Class<? extends BaseService> getServiceClass() {
        return ManagerDataService.class;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = getIntent().getData();
        if (uri != null) {
            getService().executeAction(BaseService.ACTIONS.TWITTER_LOADER_RESPONSE, uri);
        }
    }

    @Override
    public Activity getBindingActivity() {
        return this;
    }

    @Override
    public Object getBindingKey() {
        return null;
    }

    @Override
    public void onStartAction(BaseService.ACTIONS theAction) {
        showProgressOverlay();
    }

    @Override
    public void onFinishAction(BaseService.ACTIONS theAction, Object result) {
        switch (theAction) {
            case TWITTER_LOADER_RESPONSE:
                if ((Boolean) result) {
                    getService().executeAction(BaseService.ACTIONS.GET_TWITTER_USER, null);
                }
                break;
        }
    }


    @Override
    public void onFailAction(BaseService.ACTIONS theAction, Exception e) {
        showErrorOverlay();
    }
}
