package com.globant.eventscorelib;

import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.domainObjects.Subscriber;
import com.globant.eventscorelib.utils.CoreConstants;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class Database {

    ParseQuery<ParseObject> mQuery;
    List<ParseObject> mParseObjectList;
    ParseObject mParseObject;

    public List<Event> getAllEvents() throws ParseException {
        Event event = new Event();
        List<Event> events = new ArrayList<>();
        mQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        mParseObjectList = mQuery.find();
        for (int x = 0; x < mParseObjectList.size(); x++) {
            mParseObject = mParseObjectList.get(x);
            setEventAttributes(event);
            events.add(event);
        }
        return events;
    }

    private void setEventAttributes(Event event) throws ParseException {
        event.setObjectID(mParseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        event.setTitle(mParseObject.getString(CoreConstants.FIELD_TITLE));
        event.setShortDescription(mParseObject.getString(CoreConstants.FIELD_SHORT_DESCRIPTION));
        event.setFullDescription(mParseObject.getString(CoreConstants.FIELD_FULL_DESCRIPTION));
        event.setAdditionalInfo(mParseObject.getString(CoreConstants.FIELD_ADDITIONAL_INFO));
        event.setAddress(mParseObject.getString(CoreConstants.FIELD_ADDRESS));
        event.setQrCode(mParseObject.getString(CoreConstants.FIELD_QR_CODE));
        event.setCity(mParseObject.getString(CoreConstants.FIELD_CITY));
        event.setCountry(mParseObject.getString(CoreConstants.FIELD_COUNTRY));
        event.setCategory(mParseObject.getString(CoreConstants.FIELD_CATEGORY));
        event.setLanguage(mParseObject.getString(CoreConstants.FIELD_LANGUAGE));
        event.setHashtag(mParseObject.getString(CoreConstants.FIELD_HASHTAG));
        event.setStartDate(mParseObject.getDate(CoreConstants.FIELD_START_DATE));
        event.setEndDate(mParseObject.getDate(CoreConstants.FIELD_END_DATE));
        event.setPublic(mParseObject.getBoolean(CoreConstants.FIELD_PUBLIC));
        event.setIcon(mParseObject.getParseFile(CoreConstants.FIELD_ICON).getData());
        event.setEventLogo(mParseObject.getParseFile(CoreConstants.FIELD_EVENT_LOGO).getData());
        event.setLatitude(mParseObject.getParseGeoPoint(CoreConstants.FIELD_MAP_COORDINATES).getLatitude());
        event.setLongitude(mParseObject.getParseGeoPoint(CoreConstants.FIELD_MAP_COORDINATES).getLongitude());
        event.setSpeakers(getSpeakersByEventId(event.getObjectID()));
        event.setSubscribers(getSubscriberByEventId(event.getObjectID(), true));
    }

    public List<Speaker> getSpeakersByEventId(String objectID) {
//        List<Speaker> speakers = new ArrayList<>();
//        mQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
//        mParseObjectList = mQuery.find();
        return null;
    }

    public List<Subscriber> getSubscriberByEventId(String objectID, Boolean accepted) throws ParseException {
        Subscriber subscriber = new Subscriber();
        List<Subscriber> subscribers = new ArrayList<>();
        mQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TO_SUBSCRIBERS_TABLE);
        mQuery.whereEqualTo(CoreConstants.FIELD_EVENTS, objectID);
        mQuery.whereEqualTo(CoreConstants.FIELD_ACCEPTED, accepted);
        mParseObjectList = mQuery.find();
        for (int x = 0; x < mParseObjectList.size(); x++) {
            mParseObject = mParseObjectList.get(x);
            setSubscriberAttributes(subscriber);
            subscribers.add(subscriber);
        }
        return subscribers;
    }

    private void setSubscriberAttributes(Subscriber subscriber) throws ParseException {
        subscriber.setObjectID(mParseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setName(mParseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setLastName(mParseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setEmail(mParseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setPhone(mParseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setOccupation(mParseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setTwitterUser(mParseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setCity(mParseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setCountry(mParseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setPicture(mParseObject.getParseFile(CoreConstants.FIELD_OBJECT_ID).getData());
        subscriber.setEnglish(mParseObject.getBoolean(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setGlober(mParseObject.getBoolean(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setPublic(mParseObject.getBoolean(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setAccepted(mParseObject.getBoolean(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setCheckIn(mParseObject.getBoolean(CoreConstants.FIELD_OBJECT_ID));
    }

    public List<Subscriber> getSubscribersByEventId(String objectID) throws ParseException {
        Subscriber subscriber = new Subscriber();
        List<Subscriber> subscribers = new ArrayList<>();
        mQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TO_SUBSCRIBERS_TABLE);
        mQuery.whereEqualTo(CoreConstants.FIELD_EVENTS, objectID);
        mQuery.whereEqualTo(CoreConstants.FIELD_ACCEPTED, false);
        mParseObjectList = mQuery.find();
        for (int x = 0; x < mParseObjectList.size(); x++) {
            mParseObject = mParseObjectList.get(x);
            setSubscriberAttributes(subscriber);
            subscribers.add(subscriber);
        }
        return subscribers;
    }
}
