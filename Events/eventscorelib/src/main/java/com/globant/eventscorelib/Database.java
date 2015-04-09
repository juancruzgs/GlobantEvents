package com.globant.eventscorelib;

import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.domainObjects.Subscriber;
import com.globant.eventscorelib.utils.CoreConstants;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class Database {

    public List<Event> getEvents() throws ParseException {
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

       public Event getEvent(String eventId) throws ParseException {
        Event event = new Event();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        ParseObject parseObject = query.get(eventId);
        setEvent(event, parseObject);
        return event;
    }

    private void setEvent(Event event, ParseObject parseObject) throws ParseException {
        event.setObjectID(parseObject.getObjectId());
        event.setTitle(parseObject.getString(CoreConstants.FIELD_TITLE));
        event.setShortDescription(parseObject.getString(CoreConstants.FIELD_SHORT_DESCRIPTION));
        event.setCity(parseObject.getString(CoreConstants.FIELD_CITY));
        event.setCountry(parseObject.getString(CoreConstants.FIELD_COUNTRY));
        event.setCategory(parseObject.getString(CoreConstants.FIELD_CATEGORY));
        event.setStartDate(parseObject.getDate(CoreConstants.FIELD_START_DATE));
        event.setEndDate(parseObject.getDate(CoreConstants.FIELD_END_DATE));
        event.setPublic(parseObject.getBoolean(CoreConstants.FIELD_PUBLIC));
        event.setIcon(parseObject.getParseFile(CoreConstants.FIELD_ICON).getData());
        event.setEventLogo(parseObject.getParseFile(CoreConstants.FIELD_EVENT_LOGO).getData());
        event.setFullDescription(parseObject.getString(CoreConstants.FIELD_FULL_DESCRIPTION));
        event.setAdditionalInfo(parseObject.getString(CoreConstants.FIELD_ADDITIONAL_INFO));
        event.setAddress(parseObject.getString(CoreConstants.FIELD_ADDRESS));
        event.setQrCode(parseObject.getString(CoreConstants.FIELD_QR_CODE));
        event.setLanguage(parseObject.getString(CoreConstants.FIELD_LANGUAGE));
        event.setHashtag(parseObject.getString(CoreConstants.FIELD_HASHTAG));
        event.setLatitude(parseObject.getParseGeoPoint(CoreConstants.FIELD_MAP_COORDINATES).getLatitude());
        event.setLongitude(parseObject.getParseGeoPoint(CoreConstants.FIELD_MAP_COORDINATES).getLongitude());
        event.setSpeakers(getSpeakersByEventId(event.getObjectID()));
        event.setSubscribers(getSubscriberByEventId(event.getObjectID()));
    }


    public List<Speaker> getSpeakersByEventId(String eventId) {
//        List<Speaker> speakers = new ArrayList<>();
//        mQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
//        mParseObjectList = mQuery.find();
        return null;
    }

    public List<Subscriber> getSubscriberByEventId(String eventId) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.EVENTS_TO_SUBSCRIBERS_TABLE);
        query.whereEqualTo(CoreConstants.FIELD_EVENTS, eventId);


        ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery(CoreConstants.SUBSCRIBERS_TABLE);
        innerQuery.whereMatchesKeyInQuery("objectId", "subscribers", query);
        List<ParseObject> parseObjectList = innerQuery.find();
        return null;
        //TODO Terminar
//        Subscriber subscriber = new Subscriber();
//        List<Subscriber> subscribers = new ArrayList<>();
//        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.EVENTS_TO_SUBSCRIBERS_TABLE);
//        query.whereEqualTo(CoreConstants.FIELD_EVENTS, eventId);
//        List<ParseObject> parseObjectList = query.find();
//        ParseObject parseObject;
//        for (int x = 0; x < parseObjectList.size(); x++) {
//            parseObject = parseObjectList.get(x);
//            setSubscriber(subscriber, parseObject);
//            subscribers.add(subscriber);
//        }
//        return subscribers;
        }

    private void setSubscriber(Subscriber subscriber, ParseObject parseObject) throws ParseException {
        subscriber.setObjectID(parseObject.getObjectId());
        subscriber.setName(parseObject.getString(CoreConstants.FIELD_NAME));
        subscriber.setLastName(parseObject.getString(CoreConstants.FIELD_LAST_NAME));
        subscriber.setEmail(parseObject.getString(CoreConstants.FIELD_EMAIL));
        subscriber.setPhone(parseObject.getString(CoreConstants.FIELD_PHONE));
        subscriber.setOccupation(parseObject.getString(CoreConstants.FIELD_OCCUPATION));
        subscriber.setTwitterUser(parseObject.getString(CoreConstants.FIELD_TWITTER_USER));
        subscriber.setCity(parseObject.getString(CoreConstants.FIELD_CITY));
        subscriber.setCountry(parseObject.getString(CoreConstants.FIELD_COUNTRY));
        subscriber.setPicture(parseObject.getParseFile(CoreConstants.FIELD_PICTURE).getData());
        subscriber.setEnglish(parseObject.getBoolean(CoreConstants.FIELD_ENGLISH));
        subscriber.setGlober(parseObject.getBoolean(CoreConstants.FIELD_GLOBER));
        subscriber.setPublic(parseObject.getBoolean(CoreConstants.FIELD_PUBLIC));
        subscriber.setAccepted(parseObject.getBoolean(CoreConstants.FIELD_ACCEPTED));
        subscriber.setCheckIn(parseObject.getBoolean(CoreConstants.FIELD_CHECK_IN));
    }

    public void createEvent(Event event) throws ParseException {
        ParseObject parseObject = new ParseObject(CoreConstants.EVENTS_TABLE);
        parseObject.put(CoreConstants.FIELD_TITLE, event.getTitle());
        parseObject.put(CoreConstants.FIELD_CITY, event.getShortDescription());
        parseObject.put(CoreConstants.FIELD_COUNTRY, event.getCountry());
        parseObject.put(CoreConstants.FIELD_CATEGORY, event.getCategory());
        parseObject.put(CoreConstants.FIELD_START_DATE, event.getStartDate());
        parseObject.put(CoreConstants.FIELD_END_DATE, event.getEndDate());
        parseObject.put(CoreConstants.FIELD_PUBLIC, event.isPublic());
        parseObject.put(CoreConstants.FIELD_ICON, new ParseFile(event.getIcon()));
        parseObject.put(CoreConstants.FIELD_EVENT_LOGO, new ParseFile(event.getEventLogo()));
        parseObject.put(CoreConstants.FIELD_FULL_DESCRIPTION, event.getFullDescription());
        parseObject.put(CoreConstants.FIELD_ADDITIONAL_INFO, event.getAdditionalInfo());
        parseObject.put(CoreConstants.FIELD_ADDRESS, event.getAddress());
        parseObject.put(CoreConstants.FIELD_QR_CODE, event.getQrCode());
        parseObject.put(CoreConstants.FIELD_LANGUAGE, event.getLanguage());
        parseObject.put(CoreConstants.FIELD_HASHTAG, event.getHashtag());
        parseObject.put(CoreConstants.FIELD_MAP_COORDINATES, new ParseGeoPoint(event.getLatitude(), event.getLongitude()));
        parseObject.save();
    }

    public void  updateEvent(Event event) {
        ParseObject parseObject = ParseObject.createWithoutData(CoreConstants.EVENTS_TABLE, event.getObjectID());
        parseObject.put("title", "sdsds");
    }


}
