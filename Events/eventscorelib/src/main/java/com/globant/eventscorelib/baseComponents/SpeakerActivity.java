package com.globant.eventscorelib.baseComponents;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.fragments.SpeakerFragment;

public class SpeakerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SpeakerFragment())
                    .commit();
        }

    }
}
