package com.globant.events;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.globant.eventscorelib.baseComponents.BaseActivity;
import com.globant.eventscorelib.baseComponents.BaseFragment;


public class CreditsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
    }

    @Override
    public String getActivityTitle() {
        return "Credits";
    }

    @Override
    public String getFragmentTitle(BaseFragment fragment) {
        return null;
    }

}
