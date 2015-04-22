package com.globant.eventmanager;

import com.globant.eventscorelib.BaseTweetActivity;
import com.globant.eventscorelib.baseComponents.BaseService;


public class TweetManagerActivity extends BaseTweetActivity {

    @Override
    protected Class<? extends BaseService> getServiceClass() {
        return ManagerDataService.class;
    }
}
