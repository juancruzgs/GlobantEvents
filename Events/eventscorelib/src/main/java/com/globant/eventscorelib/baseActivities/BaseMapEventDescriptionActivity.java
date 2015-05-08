package com.globant.eventscorelib.baseActivities;


import com.globant.eventscorelib.R;
import com.globant.eventscorelib.utils.CoreConstants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;


public class BaseMapEventDescriptionActivity extends BaseMapActivity {

    @Override
    protected int getMapLayout() {
        return R.layout.activity_map_event_description;
    }

    @Override
    protected int getMapContainer() {
        return R.id.container;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        LatLng latLng = getIntent().getParcelableExtra(CoreConstants.MAP_MARKER_POSITION_INTENT);
        if (latLng != null){
            addMarkerToMap(latLng);
            if (getInitialCameraPosition() == null) {
                //First create
                changeCameraPosition(latLng);
            }
        }
    }
}
