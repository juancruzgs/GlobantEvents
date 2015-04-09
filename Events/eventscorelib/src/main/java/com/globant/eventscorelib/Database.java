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

    public List<Event> getEventList() throws ParseException {
        Event event = new Event();
        List<Event> events = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        List<ParseObject> parseObjectList = query.find();
        ParseObject parseObject;
        for (int x = 0; x < parseObjectList.size(); x++) {
            parseObject = parseObjectList.get(x);
            setEvent(event, parseObject);
            events.add(event);
        }
        return events;
    }

    public List<Event> getEventDetails() throws ParseException {
        Event event = new Event();
        List<Event> events = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        List<ParseObject> parseObjectList = query.find();
        ParseObject parseObject;
        for (int x = 0; x < parseObjectList.size(); x++) {
            parseObject = parseObjectList.get(x);
            setEventDetails(event, parseObject);
            events.add(event);
        }
        return events;
    }

    private void setEvent(Event event, ParseObject parseObject) throws ParseException {
        event.setObjectID(parseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        event.setTitle(parseObject.getString(CoreConstants.FIELD_TITLE));
        event.setShortDescription(parseObject.getString(CoreConstants.FIELD_SHORT_DESCRIPTION));
        event.setAddress(parseObject.getString(CoreConstants.FIELD_ADDRESS));
        event.setCity(parseObject.getString(CoreConstants.FIELD_CITY));
        event.setCountry(parseObject.getString(CoreConstants.FIELD_COUNTRY));
        event.setCategory(parseObject.getString(CoreConstants.FIELD_CATEGORY));
        event.setStartDate(parseObject.getDate(CoreConstants.FIELD_START_DATE));
        event.setEndDate(parseObject.getDate(CoreConstants.FIELD_END_DATE));
        event.setPublic(parseObject.getBoolean(CoreConstants.FIELD_PUBLIC));
        event.setIcon(parseObject.getParseFile(CoreConstants.FIELD_ICON).getData());
        event.setEventLogo(parseObject.getParseFile(CoreConstants.FIELD_EVENT_LOGO).getData());
    }

    private void setEventDetails(Event event, ParseObject parseObject) throws ParseException {
        event.setObjectID(parseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        event.setTitle(parseObject.getString(CoreConstants.FIELD_TITLE));
        event.setShortDescription(parseObject.getString(CoreConstants.FIELD_SHORT_DESCRIPTION));
        event.setFullDescription(parseObject.getString(CoreConstants.FIELD_FULL_DESCRIPTION));
        event.setAdditionalInfo(parseObject.getString(CoreConstants.FIELD_ADDITIONAL_INFO));
        event.setAddress(parseObject.getString(CoreConstants.FIELD_ADDRESS));
        event.setQrCode(parseObject.getString(CoreConstants.FIELD_QR_CODE));
        event.setCity(parseObject.getString(CoreConstants.FIELD_CITY));
        event.setCountry(parseObject.getString(CoreConstants.FIELD_COUNTRY));
        event.setCategory(parseObject.getString(CoreConstants.FIELD_CATEGORY));
        event.setLanguage(parseObject.getString(CoreConstants.FIELD_LANGUAGE));
        event.setHashtag(parseObject.getString(CoreConstants.FIELD_HASHTAG));
        event.setStartDate(parseObject.getDate(CoreConstants.FIELD_START_DATE));
        event.setEndDate(parseObject.getDate(CoreConstants.FIELD_END_DATE));
        event.setPublic(parseObject.getBoolean(CoreConstants.FIELD_PUBLIC));
        event.setIcon(parseObject.getParseFile(CoreConstants.FIELD_ICON).getData());
        event.setEventLogo(parseObject.getParseFile(CoreConstants.FIELD_EVENT_LOGO).getData());
        event.setLatitude(parseObject.getParseGeoPoint(CoreConstants.FIELD_MAP_COORDINATES).getLatitude());
        event.setLongitude(parseObject.getParseGeoPoint(CoreConstants.FIELD_MAP_COORDINATES).getLongitude());
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
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.EVENTS_TO_SUBSCRIBERS_TABLE);
        query.whereEqualTo(CoreConstants.FIELD_EVENTS, objectID);
        query.whereEqualTo(CoreConstants.FIELD_ACCEPTED, accepted);
        List<ParseObject> parseObjectList = query.find();
        ParseObject parseObject;
        for (int x = 0; x < parseObjectList.size(); x++) {
            parseObject = parseObjectList.get(x);
            setSubscriberAttributes(subscriber, parseObject);
            subscribers.add(subscriber);
        }
        return subscribers;
    }

    private void setSubscriberAttributes(Subscriber subscriber, ParseObject parseObject) throws ParseException {
        subscriber.setObjectID(parseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setName(parseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setLastName(parseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setEmail(parseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setPhone(parseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setOccupation(parseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setTwitterUser(parseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setCity(parseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setCountry(parseObject.getString(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setPicture(parseObject.getParseFile(CoreConstants.FIELD_OBJECT_ID).getData());
        subscriber.setEnglish(parseObject.getBoolean(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setGlober(parseObject.getBoolean(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setPublic(parseObject.getBoolean(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setAccepted(parseObject.getBoolean(CoreConstants.FIELD_OBJECT_ID));
        subscriber.setCheckIn(parseObject.getBoolean(CoreConstants.FIELD_OBJECT_ID));
    }
}
