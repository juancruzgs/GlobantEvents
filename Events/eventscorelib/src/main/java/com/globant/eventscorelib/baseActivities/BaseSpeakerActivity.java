package com.globant.eventscorelib.baseActivities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseFragments.BaseSpeakerFragment;


public class BaseSpeakerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BaseSpeakerFragment())
                    .commit();
        }
        setContentView(R.layout.activity_base_speaker);
    }
}
