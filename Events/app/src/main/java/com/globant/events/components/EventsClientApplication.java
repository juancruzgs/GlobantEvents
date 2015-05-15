package com.globant.events.components;

import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.utils.PushNotifications;

/**
 * Created by paula.baudo on 4/14/2015.
 */
public class EventsClientApplication extends BaseApplication {
    @Override
    public Class<? extends BaseService> getServiceClass() {
        return DataServiceClient.class;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PushNotifications.subscribeToChannel("");
    }
}
