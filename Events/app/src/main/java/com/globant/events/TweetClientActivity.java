package com.globant.events;

import com.globant.eventscorelib.BaseTweetActivity;
import com.globant.eventscorelib.baseComponents.BaseService;


public class TweetClientActivity extends BaseTweetActivity {

    @Override
    protected Class<? extends BaseService> getServiceClass() {
        return ClientDataService.class;
    }
}
