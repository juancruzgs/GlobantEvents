package com.globant.eventscorelib.baseActivities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.globant.eventscorelib.R;

public class BaseCalendarListActivity extends BaseActivity {

    public final static int CODE_CALENDAR = 2;
    public final static String KEY_CALENDAR_POS = "KEY_CALENDAR_POS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_calendar_list);
    }


}
