package com.globant.events.fragments;

import android.content.Intent;
import android.view.View;

import com.globant.events.activities.MapClientActivity;
import com.globant.eventscorelib.baseFragments.BaseEventDescriptionFragment;
import com.globant.eventscorelib.utils.CoreConstants;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by juan.soler on 21/04/2015.
 */
public class EventDescriptionClientFragment extends BaseEventDescriptionFragment {

    @Override
    protected void prepareMapIconButton() {
        mMapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapClientActivity.class);
                intent.putExtra(CoreConstants.MAP_MARKER_POSITION_INTENT, mEvent.getCoordinates());
                startActivity(intent);
            }
        });
    }
}

