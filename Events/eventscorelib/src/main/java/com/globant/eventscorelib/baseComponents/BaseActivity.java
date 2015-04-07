package com.globant.eventscorelib.baseComponents;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * Created by ignaciopena on 4/1/15.
 */
public class BaseActivity extends ActionBarActivity {

    BroadcastReceiver mReceiver;

    @Override
    public void setContentView(View view) {
        //super.onCreateView();
        super.setContentView(view);
    }

    @Override
    protected void onResume() {
        mReceiver = new ConnectionBroadcastReceiver();
        registerReceiver(mReceiver,
                         new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }
}
