package com.globant.eventmanager.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.globant.eventmanager.R;
import com.globant.eventmanager.fragments.EventListManagerFragment;
import com.globant.eventscorelib.baseActivities.BaseActivity;
import com.globant.eventscorelib.baseActivities.BaseEventListActivity;
import com.globant.eventscorelib.controllers.SharedPreferencesController;


public class EventsListManagerActivity extends BaseEventListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new EventListManagerFragment())
                    .commit();
        }
        SharedPreferencesController.setHintParticipantsShowed(false, this);
    }
}
