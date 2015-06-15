package com.globant.eventmanager;

import android.content.Context;
import android.location.Address;
import android.test.InstrumentationTestCase;

import com.globant.eventscorelib.controllers.GeocoderController;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by juan.soler on 6/10/2015.
 */
public class GeocoderControllerTest extends InstrumentationTestCase {

    GeocoderController mGeocoderController;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Context context = getInstrumentation().getTargetContext().getApplicationContext();
        mGeocoderController = new GeocoderController(context);
    }

    @Override
    protected void tearDown() throws Exception {
        mGeocoderController = null;
        super.tearDown();
    }

    public void testGetCoordinatesFromAddress(){
        LatLng latLng = mGeocoderController.getCoordinatesFromAddress("Falucho 1114, Mar del Plata");
        assertEquals(-38.0126486, latLng.latitude);
        assertEquals(-57.5366404, latLng.longitude);
    }

    public void testGetAddressFromCoordinates(){
        LatLng latLng = new LatLng(-38.0126486, -57.5366404);
        Address address = mGeocoderController.getAddressFromCoordinates(latLng);
        assertEquals("Falucho, Mar del Plata", address.getThoroughfare() + ", " + address.getLocality());
    }
}
