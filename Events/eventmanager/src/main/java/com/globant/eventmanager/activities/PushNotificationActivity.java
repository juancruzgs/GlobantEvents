package com.globant.eventmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.globant.eventmanager.R;
import com.globant.eventmanager.fragments.PushNotificationFragment;


public class PushNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_notification, new PushNotificationFragment())
                    .commit();
        }
    }

}
