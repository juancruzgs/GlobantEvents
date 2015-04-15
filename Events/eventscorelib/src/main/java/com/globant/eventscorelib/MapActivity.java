package com.globant.eventscorelib;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.globant.eventscorelib.utils.CoreConstants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;


public abstract class MapActivity extends ActionBarActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;

    protected GoogleMap getGoogleMap() {
        return mGoogleMap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getMapLayout());
        prepareMapFragment();
        prepareToolbar();
    }

    protected abstract int getMapLayout();

    private void prepareMapFragment() {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
              .add(getMapContainer(), mapFragment)
              .commit();
        mapFragment.getMapAsync(this);
    }

    protected abstract int getMapContainer();

    private void prepareToolbar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getActivityTitle());
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbar_fragment_title);
        textView.setText("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected abstract String getActivityTitle();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        UiSettings uiSettings = mGoogleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setCompassEnabled(true);
        mGoogleMap.setMyLocationEnabled(true);
    }

    protected void changeCameraPosition(LatLng latLng) {
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(latLng)
                .zoom(CoreConstants.MAP_CAMERA_ZOOM)
                .build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                CoreConstants.MAP_CAMERA_ANIMATION_DURATION, null);
    }
}
