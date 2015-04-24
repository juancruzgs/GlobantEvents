package com.globant.eventscorelib.baseActivities;

import android.os.Bundle;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseActivities.BaseActivity;
import com.globant.eventscorelib.baseFragments.BaseCreditsFragment;


public class BaseCreditsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BaseCreditsFragment())
                    .commit();
        }
    }
}
