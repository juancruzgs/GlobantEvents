package com.globant.events;


import com.globant.eventscorelib.baseComponents.BaseMapActivity;
import com.globant.eventscorelib.utils.CoreConstants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;


public class ClientMapActivity extends BaseMapActivity {
    @Override
    protected int getMapLayout() {
        return R.layout.activity_client_map;
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
        }
    }
}
