package com.globant.eventscorelib.baseActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.globant.eventscorelib.R;
import com.globant.eventscorelib.utils.CoreConstants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public abstract class BaseMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private CameraPosition mInitialCameraPosition;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        CameraPosition cameraPosition = mGoogleMap.getCameraPosition();
        outState.putParcelable(CoreConstants.MAP_CAMERA_POSITION_INTENT, cameraPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mInitialCameraPosition = (CameraPosition)savedInstanceState.get(CoreConstants.MAP_CAMERA_POSITION_INTENT);
    }

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
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbar_fragment_title);
        textView.setText("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        UiSettings uiSettings = mGoogleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setCompassEnabled(true);
        mGoogleMap.setMyLocationEnabled(true);
        if (mInitialCameraPosition != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mInitialCameraPosition));
        }
    }

    protected CameraPosition getInitialCameraPosition() {
        return mInitialCameraPosition;
    }

    protected Marker addMarkerToMap(LatLng latLng) {
        getGoogleMap().clear();
        return getGoogleMap().addMarker(new MarkerOptions()
                .position(latLng));
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
