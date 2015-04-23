package com.globant.eventmanager;

import android.os.Bundle;

import com.globant.eventscorelib.baseComponents.BaseActivity;
import com.globant.eventscorelib.baseComponents.BaseService;


public class EventsListManagerActivity extends BaseActivity {

    @Override
    protected Class<? extends BaseService> getServiceClass() {
        return ManagerDataService.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new EventListManagerFragment())
                    .commit();
        }
    }
}
