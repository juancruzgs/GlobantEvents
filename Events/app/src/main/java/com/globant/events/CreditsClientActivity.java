package com.globant.events;

import com.globant.eventscorelib.baseComponents.BaseActivity;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseComponents.CreditsActivity;

/**
 * Created by juan.soler on 21/04/2015.
 */
public class CreditsClientActivity extends CreditsActivity {

    @Override
    protected Class<? extends BaseService> getServiceClass() {
        return ClientDataService.class;
    }
}
