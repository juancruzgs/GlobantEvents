package com.globant.eventscorelib.baseActivities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseFragments.BaseSubscriberFragment;
import com.globant.eventscorelib.controllers.SharedPreferencesController;

public class BaseSubscriberActivity extends BaseActivity {

    BaseSubscriberFragment mBaseSubscriberFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber);
        prepareToolbar();
        if (savedInstanceState == null) {
            mBaseSubscriberFragment = new BaseSubscriberFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mBaseSubscriberFragment)
                    .commit();
        }
        if (savedInstanceState != null) {
            //Restore the fragment's instance
             mBaseSubscriberFragment= (BaseSubscriberFragment)getSupportFragmentManager().getFragment(
                    savedInstanceState, "mContent");

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
        mBaseSubscriberFragment.tintAllGrey();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mBaseSubscriberFragment);


    }
}


