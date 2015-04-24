package com.globant.events.fragments;

import android.content.Intent;
import android.view.MenuItem;

import com.globant.events.activities.MapClientActivity;
import com.globant.eventscorelib.baseFragments.BaseEventDescriptionFragment;
import com.globant.eventscorelib.utils.CoreConstants;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by juan.soler on 21/04/2015.
 */
public class EventDescriptionClientFragment extends BaseEventDescriptionFragment{

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == com.globant.eventscorelib.R.id.action_map) {
            Intent intent = new Intent(getActivity(), MapClientActivity.class);
            LatLng latLng = new LatLng(-38.011021, -57.535314);
            intent.putExtra(CoreConstants.MAP_MARKER_POSITION_INTENT, latLng);
            startActivity(intent);
            return true;
        }
        else {
            return false;
        }
    }
}
