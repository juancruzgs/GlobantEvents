package com.globant.eventmanager;

import android.content.Intent;

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
    }
}
