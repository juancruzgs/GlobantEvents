package com.globant.eventmanager.components;

import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.utils.CoreConstants;

/**
 * Created by ariel.cattaneo on 13/04/2015.
 */
public class DataServiceManager extends BaseService {

    @Override
    protected String getTwitterCallbackURL() {
        return CoreConstants.TWITTER_CALLBACK_URL_MANAGER;
    }
}
