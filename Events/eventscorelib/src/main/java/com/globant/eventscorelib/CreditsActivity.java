package com.globant.eventscorelib;

import android.os.Bundle;

import com.globant.eventscorelib.baseComponents.BaseActivity;
import com.globant.eventscorelib.baseComponents.BaseService;
import com.globant.eventscorelib.fragments.CreditsFragment;


public class CreditsActivity extends BaseActivity {

    @Override
    protected Class<? extends BaseService> getServiceClass() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new CreditsFragment())
                    .commit();
        }
    }

    @Override
    public String getActivityTitle() {
        return getString(R.string.title_activity_credits);
    }

}
