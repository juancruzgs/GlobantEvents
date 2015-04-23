package com.globant.eventmanager;


import com.globant.eventscorelib.baseComponents.BaseApplication;
import com.globant.eventscorelib.baseComponents.BaseService;

/**
 * Created by juan.soler on 09/04/2015.
 */
public class EventsManagerApplication extends BaseApplication{
    @Override
    public Class<? extends BaseService> getServiceClass() {
        return ManagerDataService.class;
    }
}
