package com.globant.eventmanager;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.globant.eventscorelib.MapActivity;
import com.globant.eventscorelib.utils.CoreConstants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class ManagerMapActivity extends MapActivity {

    private Marker mMarker;
    private Geocoder mGeocoder;
    private long mBackPressedTime;
    private long mUpPressedTime;

    @Override
    protected int getMapLayout() {
        return R.layout.activity_manager_map;
    }

    @Override
    protected int getMapContainer() {
        return R.id.container;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.title_activity_manager_map);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        mGeocoder = new Geocoder(getBaseContext());
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                addMarkerToMap(latLng);
            }
        });
    }

    private void addMarkerToMap(LatLng latLng) {
        getGoogleMap().clear();
        mMarker = getGoogleMap().addMarker(new MarkerOptions()
                .position(latLng));
        changeCameraPosition(latLng);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manager_map, menu);
        prepareSearchView(menu);
        return true;
    }

    private void prepareSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                List<Address> addresses;
                try {
                    //TODO AsyncTask
                    addresses = mGeocoder.getFromLocationName(s, CoreConstants.MAX_GEOCODER_RESULTS);
                    Address address = addresses.get(CoreConstants.ZERO);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    addMarkerToMap(latLng);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishActivityWithResult(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean handled = false;

        if (id == android.R.id.home) {
            finishActivityWithResult(false);
            handled = true;
        }

        if (!handled) {
            handled =  super.onOptionsItemSelected(item);
        }

        return handled;
    }

    private void finishActivityWithResult(boolean backButton) {
        if (mMarker != null) {
            finishActivityWithMarkerData();
        }
        else {
            finishActivityWithoutMarkerData(backButton);
        }
    }

    private void finishActivityWithMarkerData() {
        try {
            Address address = getMarkerLocation();
            setMapActivityResult(address);
            finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Address getMarkerLocation() throws IOException {
        List<Address> addresses;
        LatLng latLng = mMarker.getPosition();
        addresses = mGeocoder.getFromLocation(latLng.latitude, latLng.longitude, CoreConstants.MAX_GEOCODER_RESULTS);
        return addresses.get(CoreConstants.ZERO);
    }

    private void setMapActivityResult(Address address) {
        Intent intent = new Intent();
        intent.putExtra(CoreConstants.MAP_ADDRESS_INTENT, address);
        setResult(ManagerMapActivity.RESULT_OK, intent);
    }

    private void finishActivityWithoutMarkerData(boolean backButton) {
        if (isDoubleTapToExitDone(backButton)) {
            setResult(ManagerMapActivity.RESULT_CANCELED);
            finish();
        }
    }

    private boolean isDoubleTapToExitDone(boolean backButton) {
        long time;
        if (backButton){
            time = mBackPressedTime;
        }
        else {
            time = mUpPressedTime;
        }

        if (time + CoreConstants.DOUBLE_TAP_TIME_INTERVAL > System.currentTimeMillis())
        {
            return true;
        }
        else {
            Toast.makeText(getBaseContext(), getString(R.string.toast_double_tap_to_cancel), Toast.LENGTH_SHORT).show();
        }

        if (backButton){
            mBackPressedTime = System.currentTimeMillis();
        }
        else {
            mUpPressedTime = System.currentTimeMillis();
        }

        return false;
    }
}
