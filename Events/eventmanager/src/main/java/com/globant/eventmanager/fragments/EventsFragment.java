package com.globant.eventmanager.fragments;

import android.content.Intent;
import android.view.View;

import com.globant.eventmanager.activities.MapEventCreationActivity;
import com.globant.eventscorelib.baseFragments.BaseEventsFragment;
import com.globant.eventscorelib.utils.CoreConstants;

/**
 * Created by david.burgos
 */

public class EventsFragment extends BaseEventsFragment {

    @Override
    protected void prepareImageButton() {
        super.prepareImageButton();

        mEditTextMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapEventCreationActivity.class);
                intent.putExtra(CoreConstants.MAP_MARKER_POSITION_INTENT, mLatLng);
                startActivityForResult(intent, CoreConstants.MAP_MANAGER_REQUEST);
            }
        });
    }
}
