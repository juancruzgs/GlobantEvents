package com.globant.eventmanager.fragments;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import com.globant.eventmanager.activities.MapManagerActivity;
import com.globant.eventscorelib.baseFragments.BaseEventDescriptionFragment;

public class EventDescriptionManagerFragment extends BaseEventDescriptionFragment {


    @Override
    protected void prepareMapIconButton() {
        mMapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}