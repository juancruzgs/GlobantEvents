package com.globant.eventmanager;

import android.content.Intent;
import android.view.MenuItem;

import com.globant.eventscorelib.baseComponents.BaseEventDescriptionFragment;
import com.globant.eventscorelib.baseComponents.CreditsActivity;

/**
 * Created by juan.soler on 21/04/2015.
 */
public class EventDescriptionManagerFragment extends BaseEventDescriptionFragment {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == com.globant.eventscorelib.R.id.action_map) {
            Intent intent = new Intent(getActivity(), ManagerMapActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
