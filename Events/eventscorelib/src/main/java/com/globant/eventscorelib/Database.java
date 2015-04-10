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
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.List;

public class Database {

    public List<Event> getEvents() throws ParseException {
        Event domainEvent;
        List<Event> events = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        List<ParseObject> parseObjectList = query.find();
        ParseObject databaseEvent;
        for (int x = 0; x < parseObjectList.size(); x++) {
            databaseEvent = parseObjectList.get(x);
            domainEvent = createEventFromDatabase(databaseEvent);
            events.add(domainEvent);
        }
        return events;
    }

       public Event getEvent(String eventId) throws ParseException {
        Event event;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        ParseObject parseObject = query.get(eventId);
        event = createEventFromDatabase(parseObject);
        return event;
    }

    private Event createEventFromDatabase(ParseObject databaseEvent) throws ParseException {
        Event domainEvent = new Event();
        byte[] image;
        domainEvent.setObjectID(databaseEvent.getObjectId());
        domainEvent.setTitle(databaseEvent.getString(CoreConstants.FIELD_TITLE));
        domainEvent.setShortDescription(databaseEvent.getString(CoreConstants.FIELD_SHORT_DESCRIPTION));
        domainEvent.setCity(databaseEvent.getString(CoreConstants.FIELD_CITY));
        domainEvent.setCountry(databaseEvent.getString(CoreConstants.FIELD_COUNTRY));
        domainEvent.setCategory(databaseEvent.getString(CoreConstants.FIELD_CATEGORY));
        domainEvent.setStartDate(databaseEvent.getDate(CoreConstants.FIELD_START_DATE));
        domainEvent.setEndDate(databaseEvent.getDate(CoreConstants.FIELD_END_DATE));
        domainEvent.setPublic(databaseEvent.getBoolean(CoreConstants.FIELD_PUBLIC));
        image = getImageFromDatabase(databaseEvent, CoreConstants.FIELD_ICON);
        domainEvent.setIcon(image);
        image = getImageFromDatabase(databaseEvent, CoreConstants.FIELD_EVENT_LOGO);
        domainEvent.setEventLogo(image);
        domainEvent.setFullDescription(databaseEvent.getString(CoreConstants.FIELD_FULL_DESCRIPTION));
        domainEvent.setAdditionalInfo(databaseEvent.getString(CoreConstants.FIELD_ADDITIONAL_INFO));
        domainEvent.setAddress(databaseEvent.getString(CoreConstants.FIELD_ADDRESS));
        domainEvent.setQrCode(databaseEvent.getString(CoreConstants.FIELD_QR_CODE));
        domainEvent.setLanguage(databaseEvent.getString(CoreConstants.FIELD_LANGUAGE));
        domainEvent.setHashtag(databaseEvent.getString(CoreConstants.FIELD_HASHTAG));
        domainEvent.setLatitude(databaseEvent.getParseGeoPoint(CoreConstants.FIELD_MAP_COORDINATES).getLatitude());
        domainEvent.setLongitude(databaseEvent.getParseGeoPoint(CoreConstants.FIELD_MAP_COORDINATES).getLongitude());
        getEventSubscribers(databaseEvent);
        return domainEvent;
    }

    private byte[] getImageFromDatabase(ParseObject object, String field) throws ParseException {
        ParseFile file = object.getParseFile(field);
        if (file != null){
            return file.getData();
        }
        else {
            return null;
        }
    }

    public List<Speaker> getSpeakersByEventId(String eventId) {
//        List<Speaker> speakers = new ArrayList<>();
//        mQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
//        mParseObjectList = mQuery.find();
        return null;
    }

    public List<Subscriber> getEventSubscribers(ParseObject event) throws ParseException {
        List<Subscriber> subscribersList = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.EVENTS_TO_SUBSCRIBERS_TABLE);
        query.whereEqualTo(CoreConstants.FIELD_EVENTS, event);
        query.include(CoreConstants.FIELD_SUBSCRIBERS);

        List<ParseObject> eventsToSubscribersList = query.find();
        for (int x = 0; x < eventsToSubscribersList.size(); x++) {
            ParseObject eventToSubscribersRow = eventsToSubscribersList.get(x);
            ParseObject databaseSubscriber = eventToSubscribersRow.getParseObject(CoreConstants.FIELD_SUBSCRIBERS);
            Subscriber domainSubscriber = createSubscribersFromDatabase(databaseSubscriber);
            subscribersList.add(domainSubscriber);
        }
        return subscribersList;
    }

    private Subscriber createSubscribersFromDatabase(ParseObject parseObject) throws ParseException {
        Subscriber subscriber = new Subscriber();
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
        return subscriber;
    }

    public void createEvent(Event event) throws ParseException {
        ParseObject parseObject = new ParseObject(CoreConstants.EVENTS_TABLE);
        setParseObjectWithEventColumns(event, parseObject);
        parseObject.save();

    }

    public void addEventSpeakers(String eventId, List<String> speakersIds) throws ParseException {
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        ParseObject parseObject = query2.get(eventId);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.SPEAKERS_TABLE);
        ParseRelation<ParseObject> relation = parseObject.getRelation(CoreConstants.FIELD_SPEAKERS);
        for (String speakerId : speakersIds) {
            relation.add(query.get(speakerId));
        }
        parseObject.save();
    }

    public void deleteEventSpeakers(String eventId, List<String> speakersIds) throws ParseException {
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        ParseObject parseObject = query2.get(eventId);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.SPEAKERS_TABLE);
        ParseRelation<ParseObject> relation = parseObject.getRelation(CoreConstants.FIELD_SPEAKERS);
        for (String speakerId : speakersIds) {
            relation.remove(query.get(speakerId));
        }
        parseObject.save();
    }

    public void  updateEvent(Event event) throws ParseException {
        ParseObject parseObject = ParseObject.createWithoutData(CoreConstants.EVENTS_TABLE, event.getObjectID());
        setParseObjectWithEventColumns(event, parseObject);
        parseObject.save();
    }

    private void setParseObjectWithEventColumns(Event event, ParseObject parseObject) {
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
    }

    public void createSpeaker (Speaker speaker) throws ParseException {
        ParseObject parseObject = new ParseObject(CoreConstants.SPEAKERS_TABLE);
        setParseObjectWithSpeakerColumns(speaker, parseObject);
        parseObject.save();
    }

    private void setParseObjectWithSpeakerColumns(Speaker speaker, ParseObject parseObject) {
        parseObject.put(CoreConstants.FIELD_TITLE, speaker.getTitle());
        parseObject.put(CoreConstants.FIELD_NAME, speaker.getName());
        parseObject.put(CoreConstants.FIELD_LAST_NAME, speaker.getLastName());
        parseObject.put(CoreConstants.FIELD_BIOGRAPHY, speaker.getBiography());
        parseObject.put(CoreConstants.FIELD_PICTURE, speaker.getPicture());

    }



}
