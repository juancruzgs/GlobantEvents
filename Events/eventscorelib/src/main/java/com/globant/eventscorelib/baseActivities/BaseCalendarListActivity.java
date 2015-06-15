package com.globant.eventscorelib.baseActivities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.baseFragments.BaseCalendarListFragment;

public class BaseCalendarListActivity extends BaseActivity {

    public final static int CODE_CALENDAR = 2;
    public final static String KEY_CALENDAR_POS = "KEY_CALENDAR_POS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_calendar_list);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BaseCalendarListFragment())
                    .commit();
        }
    }


}
