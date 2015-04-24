package com.globant.eventscorelib.baseComponents;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.fragments.CreditsFragment;
import com.globant.eventscorelib.fragments.SubscriberFragment;

public class SubscriberActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SubscriberFragment())
                    .commit();
        }

    }

}
