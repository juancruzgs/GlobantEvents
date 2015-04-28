package com.globant.eventscorelib.baseActivities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseActivity;
import com.globant.eventscorelib.baseFragments.BaseSubscriberFragment;
import com.globant.eventscorelib.controllers.SharedPreferencesController;
import com.globant.eventscorelib.utils.CoreConstants;

public class BaseSubscriberActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber);
        prepareToolbar();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BaseSubscriberFragment())
                    .commit();
        }

    }

    private void prepareToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (SharedPreferencesController.getUserFirstName(this) != null) {
            getSupportActionBar().setTitle(SharedPreferencesController.getUserFirstName(this) + " " + SharedPreferencesController.getUserLastName(this));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}


