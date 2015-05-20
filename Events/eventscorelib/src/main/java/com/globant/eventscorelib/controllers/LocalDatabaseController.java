package com.globant.eventscorelib.controllers;

import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.utils.CoreConstants;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocalDatabaseController extends DatabaseController{

    @Override
    protected void pinObjectInBackground(ParseObject object) {}

    @Override
    protected void queryFromLocalDatastore(ParseQuery query) {
        query.fromLocalDatastore();
    }
}
