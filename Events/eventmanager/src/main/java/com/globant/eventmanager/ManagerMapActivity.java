package com.globant.eventmanager;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

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
        //TODO Set Result
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            List<Address> addresses;
            if (mMarker != null) {
                LatLng latLng = mMarker.getPosition();
                try {
                    addresses = mGeocoder.getFromLocation(latLng.latitude, latLng.longitude, CoreConstants.MAX_GEOCODER_RESULTS);
                    Address address = addresses.get(CoreConstants.ZERO);
                    Intent intent = new Intent();
                    intent.putExtra(CoreConstants.MAP_ADDRESS_INTENT, address);
                    setResult(ManagerMapActivity.RESULT_OK, intent);
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
