package com.globant.eventmanager.activities;

import android.os.Bundle;

import com.globant.eventmanager.R;
import com.globant.eventmanager.fragments.EventHistoryManagerFragment;
import com.globant.eventscorelib.baseActivities.BaseActivity;


public class EventHistoryManagerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_event_history);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new EventHistoryManagerFragment())
                    .commit();
        }
    }
}