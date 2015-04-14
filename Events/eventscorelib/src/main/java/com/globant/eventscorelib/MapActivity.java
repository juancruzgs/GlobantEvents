package com.globant.eventscorelib;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.globant.eventscorelib.baseComponents.BaseActivity;
import com.globant.eventscorelib.baseComponents.BaseFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class MapActivity extends ActionBarActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private EditText mEditTextSearch;
    private Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
              .add(R.id.container, mapFragment)
              .commit();
        mapFragment.getMapAsync(this);

        mEditTextSearch = (EditText)findViewById(R.id.et_location);
        Button button = (Button)findViewById(R.id.btn_find);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Geocoder geocoder = new Geocoder(getBaseContext());
                List<Address> addresses;
                String searchText = mEditTextSearch.getText().toString();
                try {
                    addresses = geocoder.getFromLocationName(searchText, 1);
                    Address address = addresses.get(0);

                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    addMarkerToMap(latLng);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addMarkerToMap(LatLng latLng) {
        mGoogleMap.clear();
        mMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));
                //.title("Marker"));
        changeCameraPosition(latLng);
        //TODO Set Result
    }

    private void changeCameraPosition(LatLng latLng) {
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(latLng)
                .zoom(17)
                .build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000, null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                addMarkerToMap(latLng);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
