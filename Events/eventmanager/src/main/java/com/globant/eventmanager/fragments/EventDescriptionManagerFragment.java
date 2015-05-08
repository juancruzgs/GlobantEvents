package com.globant.eventmanager.fragments;

import android.view.View;

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
