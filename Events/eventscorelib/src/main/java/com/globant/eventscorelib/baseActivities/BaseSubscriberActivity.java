package com.globant.eventscorelib.baseActivities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseFragments.BaseSubscriberFragment;
import com.globant.eventscorelib.controllers.SharedPreferencesController;

public class BaseSubscriberActivity extends BaseActivity {

    BaseSubscriberFragment baseSubscriberFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber);
        prepareToolbar();
        baseSubscriberFragment= new BaseSubscriberFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, baseSubscriberFragment)
                    .commit();
        }


    }

    private void prepareToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (SharedPreferencesController.getUserFirstName(this) != null) {
            getSupportActionBar().setTitle(SharedPreferencesController.getUserFirstName(this) + " " + SharedPreferencesController.getUserLastName(this));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        baseSubscriberFragment.tintAllGrey();

    }
}


