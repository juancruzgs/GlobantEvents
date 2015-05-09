package com.globant.eventmanager.fragments;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import com.globant.eventmanager.activities.EventsManagerActivity;
import com.globant.eventmanager.activities.MapManagerActivity;
import com.globant.eventscorelib.baseFragments.BaseEventDescriptionFragment;
import com.globant.eventscorelib.baseFragments.BaseEventsFragment;

/**
 * Created by juan.soler on 21/04/2015.
 */
public class EventDescriptionManagerFragment extends BaseEventDescriptionFragment {

    @Override
    public void wireUpViews(View rootView) {
        super.wireUpViews(rootView);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventsManagerActivity.class);
                BaseEventsFragment.mEventAction = BaseEventsFragment.ActionType.EDIT_EVENT;
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == com.globant.eventscorelib.R.id.action_map) {
            Intent intent = new Intent(getActivity(), MapManagerActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
