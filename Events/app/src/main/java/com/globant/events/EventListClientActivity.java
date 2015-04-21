package com.globant.events;

import android.os.Bundle;
import android.view.Menu;

import com.globant.eventscorelib.baseComponents.BaseActivity;
import com.globant.eventscorelib.baseComponents.BaseService;


public class EventListClientActivity extends BaseActivity {

    @Override
    protected Class<? extends BaseService> getServiceClass() {
        return ClientDataService.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_stream);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new EventListClientFragment())
                    .commit();
        }
    }

    @Override
    public String getActivityTitle() {
        return getString(R.string.title_activity_events_stream);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_events_stream, menu);
        return true;
    }

}
