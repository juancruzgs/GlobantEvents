package com.globant.eventscorelib.controllers;

import android.graphics.Bitmap;

import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.utils.ConvertImage;
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

/**
 * Created by juan.soler on 5/13/2015.
 */
public abstract class DatabaseController {

    public List<Event> getEvents(boolean isGlober) throws ParseException {
        ParseQuery<ParseObject> eventsQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        queryFromLocalDatastore(eventsQuery);
        eventsQuery.whereGreaterThan(CoreConstants.FIELD_END_DATE, new Date());
        eventsQuery.orderByAscending(CoreConstants.FIELD_START_DATE);
        if (!isGlober) {
            eventsQuery.whereEqualTo(CoreConstants.FIELD_PUBLIC, true);
        }
        List<ParseObject> databaseEventsList = eventsQuery.find();
        List<Event> domainEventsList = new ArrayList<>();
        for (ParseObject databaseEvent : databaseEventsList) {
            pinObjectInBackground(databaseEvent);
            ParseRelation relation = databaseEvent.getRelation(CoreConstants.FIELD_SPEAKERS);
            ParseQuery relationQuery = relation.getQuery();
            queryFromLocalDatastore(relationQuery);
            List<ParseObject> databaseSpeakersList = relationQuery.find();
            List<Speaker> domainSpeakersList = new ArrayList<>();
            for (ParseObject databaseSpeaker : databaseSpeakersList) {
                pinObjectInBackground(databaseSpeaker);
                Speaker domainSpeaker = createDomainSpeakerFromDatabase(databaseSpeaker);
                domainSpeakersList.add(domainSpeaker);
            }
            Event domainEvent = createDomainEventFromDatabase(databaseEvent);
            domainEvent.setSpeakers(domainSpeakersList);
            domainEventsList.add(domainEvent);
        }
        return domainEventsList;
    }

    protected abstract void pinObjectInBackground(ParseObject object);
    protected abstract void queryFromLocalDatastore(ParseQuery query);

    protected Speaker createDomainSpeakerFromDatabase(ParseObject databaseSpeaker) throws ParseException {
        Speaker speaker = new Speaker();
        speaker.setObjectID(databaseSpeaker.getObjectId());
        speaker.setName(databaseSpeaker.getString(CoreConstants.FIELD_NAME));
        speaker.setLastName(databaseSpeaker.getString(CoreConstants.FIELD_LAST_NAME));
        speaker.setTitle(databaseSpeaker.getString(CoreConstants.FIELD_TITLE));
        speaker.setPicture(getBitmapFromDatabase(databaseSpeaker, CoreConstants.FIELD_PICTURE));
        speaker.setBiography(databaseSpeaker.getString(CoreConstants.FIELD_BIOGRAPHY));
        return speaker;
    }

    protected Event createDomainEventFromDatabase(ParseObject databaseEvent) throws ParseException {
        Event domainEvent = new Event();
        domainEvent.setObjectID(databaseEvent.getObjectId());
        domainEvent.setTitle(databaseEvent.getString(CoreConstants.FIELD_TITLE));
        domainEvent.setShortDescription(databaseEvent.getString(CoreConstants.FIELD_SHORT_DESCRIPTION));
        domainEvent.setCity(databaseEvent.getString(CoreConstants.FIELD_CITY));
        domainEvent.setCountry(databaseEvent.getString(CoreConstants.FIELD_COUNTRY));
        domainEvent.setCategory(databaseEvent.getString(CoreConstants.FIELD_CATEGORY));
        domainEvent.setStartDate(databaseEvent.getDate(CoreConstants.FIELD_START_DATE));
        domainEvent.setEndDate(databaseEvent.getDate(CoreConstants.FIELD_END_DATE));
        domainEvent.setPublic(databaseEvent.getBoolean(CoreConstants.FIELD_PUBLIC));
        domainEvent.setIcon(getBitmapFromDatabase(databaseEvent, CoreConstants.FIELD_ICON));
        domainEvent.setEventLogo(getBitmapFromDatabase(databaseEvent, CoreConstants.FIELD_EVENT_LOGO));
        domainEvent.setFullDescription(databaseEvent.getString(CoreConstants.FIELD_FULL_DESCRIPTION));
        domainEvent.setAdditionalInfo(databaseEvent.getString(CoreConstants.FIELD_ADDITIONAL_INFO));
        domainEvent.setAddress(databaseEvent.getString(CoreConstants.FIELD_ADDRESS));
        domainEvent.setLanguage(databaseEvent.getString(CoreConstants.FIELD_LANGUAGE));
        domainEvent.setHashtag(databaseEvent.getString(CoreConstants.FIELD_HASHTAG));
        domainEvent.setCoordinates(getCoordinatesFromDatabaseObject(databaseEvent));
        return domainEvent;
    }

    protected byte[] getImageFromDatabase(ParseObject databaseObject, String field) throws ParseException {
        ParseFile file = databaseObject.getParseFile(field);
        return file != null ? file.getData() : null;
    }

    protected Bitmap getBitmapFromDatabase(ParseObject databaseObject, String field) throws ParseException {
        ParseFile file = databaseObject.getParseFile(field);
        if (file != null){
            //TODO Change the hardcoded values. Set the screen width and max container height
            return ConvertImage.convertByteArrayToBitmap(file.getData(), 200, 200);
        } else {
            return null;
        }
    }

    private LatLng getCoordinatesFromDatabaseObject(ParseObject databaseObject) {
        ParseGeoPoint geoPoint = databaseObject.getParseGeoPoint(CoreConstants.FIELD_MAP_COORDINATES);
        return geoPoint != null ? new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()) : null;
    }
}
