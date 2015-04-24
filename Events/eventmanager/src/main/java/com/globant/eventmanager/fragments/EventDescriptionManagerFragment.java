package com.globant.eventmanager.fragments;

import android.content.Intent;
import android.view.MenuItem;

import com.globant.eventmanager.activities.MapManagerActivity;
import com.globant.eventscorelib.baseFragments.BaseEventDescriptionFragment;

/**
 * Created by juan.soler on 21/04/2015.
 */
public class EventDescriptionManagerFragment extends BaseEventDescriptionFragment {

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
