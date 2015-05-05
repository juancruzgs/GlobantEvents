package com.globant.events.components;

import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.utils.Logger;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

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
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Logger.d("Successfully subscribed to the broadcast channel.");
                } else {
                    Logger.e("Failed to subscribe for push", e);
                }
            }
        });
    }
}
