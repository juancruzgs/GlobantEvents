package com.globant.eventscorelib.baseComponents;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.globant.eventscorelib.R;

/**
 * Created by ignaciopena on 4/1/15.
 */
public class BaseActivity extends ActionBarActivity {

    BroadcastReceiver mReceiver;

    @Override
    public void setContentView(int layoutResID) {
        ViewGroup mainContainer = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout frameLayout = (FrameLayout) mainContainer.findViewById(R.id.container);
        ViewGroup content = (ViewGroup) getLayoutInflater().inflate(layoutResID, frameLayout, false);
        frameLayout.addView(content);
        Toolbar toolbar =  (Toolbar) mainContainer.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        super.setContentView(mainContainer);
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
