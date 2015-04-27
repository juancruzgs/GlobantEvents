package com.globant.eventscorelib.controllers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.globant.eventscorelib.utils.CoreConstants;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by juan.soler on 20/04/2015.
 */
public class GeocoderController {

    Geocoder mGeocoder;

    public GeocoderController(Context context) {
        mGeocoder = new Geocoder(context);
    }

    public LatLng getCoordinatesFromAddress(String addressString){
        List<Address> addresses;
        try {
            addresses = mGeocoder.getFromLocationName(addressString, CoreConstants.MAX_GEOCODER_RESULTS);
            Address address = addresses.get(CoreConstants.ZERO);
            return new LatLng(address.getLatitude(), address.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public  Address getAddressFromCoordinates(LatLng latLng){
        List<Address> addresses;
        try {
            addresses = mGeocoder.getFromLocation(latLng.latitude, latLng.longitude, CoreConstants.MAX_GEOCODER_RESULTS);
            return addresses.get(CoreConstants.ZERO);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
