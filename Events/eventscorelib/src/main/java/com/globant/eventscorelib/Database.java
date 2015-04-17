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

    public Database(){
    }

    public List<Event> getEvents() throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        List<ParseObject> databaseEventsList = query.find();
        List<Event> domainEventsList = new ArrayList<>();
        for (ParseObject databaseEvent : databaseEventsList) {
            Event domainEvent = createDomainEventFromDatabase(databaseEvent);
            domainEventsList.add(domainEvent);
        }
        return domainEventsList;
    }

    public Event getEvent(String eventId) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        ParseObject databaseEvent = query.get(eventId);
        return createDomainEventFromDatabase(databaseEvent);
    }

    public List<Speaker> getEventSpeakers(String eventId) {
//        List<Speaker> speakers = new ArrayList<>();
//        mQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
//        mParseObjectList = mQuery.find();
        return null;
    }

    public List<Subscriber> getEventSubscribers(String eventId) throws ParseException {
        ParseQuery<ParseObject> eventsQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        ParseObject event = eventsQuery.get(eventId);
        ParseQuery<ParseObject> eventsToSubscribersQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TO_SUBSCRIBERS_TABLE);
        eventsToSubscribersQuery.whereEqualTo(CoreConstants.FIELD_EVENTS, event);
        eventsToSubscribersQuery.include(CoreConstants.FIELD_SUBSCRIBERS);

        List<ParseObject> eventsToSubscribersList = eventsToSubscribersQuery.find();
        List<Subscriber> subscribersList = new ArrayList<>();
        for (ParseObject eventToSubscribersRow : eventsToSubscribersList) {
            ParseObject databaseSubscriber = eventToSubscribersRow.getParseObject(CoreConstants.FIELD_SUBSCRIBERS);
            if (databaseSubscriber != null) {
                Subscriber domainSubscriber = createDomainSubscriberFromDatabase(databaseSubscriber);
                subscribersList.add(domainSubscriber);
            }
        }
        return subscribersList;
    }

    public void createEvent(Event domainEvent) throws ParseException {
        ParseObject databaseEvent = new ParseObject(CoreConstants.EVENTS_TABLE);
        setDatabaseEventInformation(domainEvent, databaseEvent);
        databaseEvent.save();
    }

    public void  updateEvent(Event domainEvent) throws ParseException {
        ParseObject databaseEvent = ParseObject.createWithoutData(CoreConstants.EVENTS_TABLE, domainEvent.getObjectID());
        setDatabaseEventInformation(domainEvent, databaseEvent);
        databaseEvent.save();
    }

    public void addEventSpeakers(String eventId, List<String> speakersIds) throws ParseException {
        ParseQuery<ParseObject> eventsQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        ParseObject event = eventsQuery.get(eventId);
        ParseQuery<ParseObject> speakersQuery = ParseQuery.getQuery(CoreConstants.SPEAKERS_TABLE);
        ParseRelation<ParseObject> relation = event.getRelation(CoreConstants.FIELD_SPEAKERS);
        for (String speakerId : speakersIds) {
            relation.add(speakersQuery.get(speakerId));
        }
        event.save();
    }

    public void deleteEventSpeakers(String eventId, List<String> speakersIds) throws ParseException {
        ParseQuery<ParseObject> eventsQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        ParseObject event = eventsQuery.get(eventId);
        ParseQuery<ParseObject> speakersQuery = ParseQuery.getQuery(CoreConstants.SPEAKERS_TABLE);
        ParseRelation<ParseObject> relation = event.getRelation(CoreConstants.FIELD_SPEAKERS);
        for (String speakerId : speakersIds) {
            relation.remove(speakersQuery.get(speakerId));
        }
        event.save();
    }

    public void createSpeaker (Speaker domainSpeaker) throws ParseException {
        ParseObject databaseSpeaker = new ParseObject(CoreConstants.SPEAKERS_TABLE);
        setDatabaseSpeakerInformation(domainSpeaker, databaseSpeaker);
        databaseSpeaker.save();
    }

    private byte[] getImageFromDatabase(ParseObject databaseObject, String field) throws ParseException {
        ParseFile file = databaseObject.getParseFile(field);
        if (file != null) {
            return file.getData();
        } else {
            return null;
        }
    }

    private double getCoordinatesFromDatabaseObject(ParseObject databaseObject, boolean latitude){
        ParseGeoPoint geoPoint = databaseObject.getParseGeoPoint(CoreConstants.FIELD_MAP_COORDINATES);
        if (geoPoint != null){
            if (latitude){
                return geoPoint.getLatitude();
            }
            else {
                return geoPoint.getLongitude();
            }
        }
        else {
            return CoreConstants.ZERO;
        }
    }

    private Event createDomainEventFromDatabase(ParseObject databaseEvent) throws ParseException {
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
        domainEvent.setIcon(getImageFromDatabase(databaseEvent, CoreConstants.FIELD_ICON));
        domainEvent.setEventLogo(getImageFromDatabase(databaseEvent, CoreConstants.FIELD_EVENT_LOGO));
        domainEvent.setFullDescription(databaseEvent.getString(CoreConstants.FIELD_FULL_DESCRIPTION));
        domainEvent.setAdditionalInfo(databaseEvent.getString(CoreConstants.FIELD_ADDITIONAL_INFO));
        domainEvent.setAddress(databaseEvent.getString(CoreConstants.FIELD_ADDRESS));
        domainEvent.setQrCode(databaseEvent.getString(CoreConstants.FIELD_QR_CODE));
        domainEvent.setLanguage(databaseEvent.getString(CoreConstants.FIELD_LANGUAGE));
        domainEvent.setHashtag(databaseEvent.getString(CoreConstants.FIELD_HASHTAG));
        domainEvent.setLatitude(getCoordinatesFromDatabaseObject(databaseEvent, true));
        domainEvent.setLongitude(getCoordinatesFromDatabaseObject(databaseEvent, false));
        return domainEvent;
    }

    private Subscriber createDomainSubscriberFromDatabase(ParseObject databaseSubscriber) throws ParseException {
        Subscriber domainSubscriber = new Subscriber();
        domainSubscriber.setObjectID(databaseSubscriber.getObjectId());
        domainSubscriber.setName(databaseSubscriber.getString(CoreConstants.FIELD_NAME));
        domainSubscriber.setLastName(databaseSubscriber.getString(CoreConstants.FIELD_LAST_NAME));
        domainSubscriber.setEmail(databaseSubscriber.getString(CoreConstants.FIELD_EMAIL));
        domainSubscriber.setPhone(databaseSubscriber.getString(CoreConstants.FIELD_PHONE));
        domainSubscriber.setOccupation(databaseSubscriber.getString(CoreConstants.FIELD_OCCUPATION));
        domainSubscriber.setTwitterUser(databaseSubscriber.getString(CoreConstants.FIELD_TWITTER_USER));
        domainSubscriber.setCity(databaseSubscriber.getString(CoreConstants.FIELD_CITY));
        domainSubscriber.setCountry(databaseSubscriber.getString(CoreConstants.FIELD_COUNTRY));
        domainSubscriber.setPicture(getImageFromDatabase(databaseSubscriber, CoreConstants.FIELD_PICTURE));
        domainSubscriber.setEnglish(databaseSubscriber.getBoolean(CoreConstants.FIELD_ENGLISH));
        domainSubscriber.setGlober(databaseSubscriber.getBoolean(CoreConstants.FIELD_GLOBER));
        domainSubscriber.setPublic(databaseSubscriber.getBoolean(CoreConstants.FIELD_PUBLIC));
        domainSubscriber.setAccepted(databaseSubscriber.getBoolean(CoreConstants.FIELD_ACCEPTED));
        domainSubscriber.setCheckIn(databaseSubscriber.getBoolean(CoreConstants.FIELD_CHECK_IN));
        return domainSubscriber;
    }

    private void setDatabaseEventInformation(Event domainEvent, ParseObject databaseEvent) {
        databaseEvent.put(CoreConstants.FIELD_TITLE, domainEvent.getTitle());
        databaseEvent.put(CoreConstants.FIELD_CITY, domainEvent.getShortDescription());
        databaseEvent.put(CoreConstants.FIELD_COUNTRY, domainEvent.getCountry());
        databaseEvent.put(CoreConstants.FIELD_CATEGORY, domainEvent.getCategory());
        databaseEvent.put(CoreConstants.FIELD_START_DATE, domainEvent.getStartDate());
        databaseEvent.put(CoreConstants.FIELD_END_DATE, domainEvent.getEndDate());
        databaseEvent.put(CoreConstants.FIELD_PUBLIC, domainEvent.isPublic());
        databaseEvent.put(CoreConstants.FIELD_ICON, new ParseFile(domainEvent.getIcon()));
        databaseEvent.put(CoreConstants.FIELD_EVENT_LOGO, new ParseFile(domainEvent.getEventLogo()));
        databaseEvent.put(CoreConstants.FIELD_FULL_DESCRIPTION, domainEvent.getFullDescription());
        databaseEvent.put(CoreConstants.FIELD_ADDITIONAL_INFO, domainEvent.getAdditionalInfo());
        databaseEvent.put(CoreConstants.FIELD_ADDRESS, domainEvent.getAddress());
        databaseEvent.put(CoreConstants.FIELD_QR_CODE, domainEvent.getQrCode());
        databaseEvent.put(CoreConstants.FIELD_LANGUAGE, domainEvent.getLanguage());
        databaseEvent.put(CoreConstants.FIELD_HASHTAG, domainEvent.getHashtag());
        databaseEvent.put(CoreConstants.FIELD_MAP_COORDINATES, new ParseGeoPoint(domainEvent.getLatitude(), domainEvent.getLongitude()));
    }

    private void setDatabaseSpeakerInformation(Speaker domainSpeaker, ParseObject databaseSpeaker) {
        databaseSpeaker.put(CoreConstants.FIELD_TITLE, domainSpeaker.getTitle());
        databaseSpeaker.put(CoreConstants.FIELD_NAME, domainSpeaker.getName());
        databaseSpeaker.put(CoreConstants.FIELD_LAST_NAME, domainSpeaker.getLastName());
        databaseSpeaker.put(CoreConstants.FIELD_BIOGRAPHY, domainSpeaker.getBiography());
        databaseSpeaker.put(CoreConstants.FIELD_PICTURE, domainSpeaker.getPicture());
    }
}
