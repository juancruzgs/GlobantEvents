package com.globant.eventmanager;

import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.baseComponents.CreditsActivity;

/**
 * Created by juan.soler on 21/04/2015.
 */
public class CreditsManagerActivity extends CreditsActivity {

    @Override
    protected Class<? extends BaseService> getServiceClass() {
        return ManagerDataService.class;
    }
}
