package com.globant.eventmanager.fragments;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.globant.eventmanager.R;
import com.globant.eventmanager.activities.EventsManagerPagerActivity;
import com.globant.eventscorelib.baseFragments.BaseEventDescriptionFragment;

public class EventDescriptionManagerFragment extends BaseEventDescriptionFragment {

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_edit_event, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.events_action_update) {
            Intent intent = new Intent(getActivity(), EventsManagerPagerActivity.class);
            EventsManagerPagerActivity.mEvent = mEvent;
            EventsFragment.mEventAction = EventsFragment.ActionType.EDIT_EVENT;
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);

    }
}