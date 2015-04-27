package com.globant.events.components;

import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseService;

/**
 * Created by paula.baudo on 4/14/2015.
 */
public class EventsClientApplication extends BaseApplication {
    @Override
    public Class<? extends BaseService> getServiceClass() {
        return DataServiceClient.class;
    }
}
