package com.globant.eventscorelib.baseActivities;

import android.os.Bundle;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseActivity;
import com.globant.eventscorelib.baseFragments.BaseSubscriberFragment;

public class BaseSubscriberActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BaseSubscriberFragment())
                    .commit();
        }

    }

}
