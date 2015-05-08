package com.globant.eventscorelib.controllers;

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
import java.util.Date;
import java.util.List;


public class CloudDataController {

    public List<Event> getEvents(boolean isGlober) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        query.whereGreaterThan(CoreConstants.FIELD_START_DATE, new Date());
        query.orderByAscending(CoreConstants.FIELD_START_DATE);
        if (!isGlober) {
            query.whereEqualTo(CoreConstants.FIELD_PUBLIC, true);
        }
        List<ParseObject> databaseEventsList = query.find();
        List<Event> domainEventsList = new ArrayList<>();
        for (ParseObject databaseEvent : databaseEventsList) {
            ParseRelation relation = databaseEvent.getRelation(CoreConstants.FIELD_SPEAKERS);
            ParseQuery relationQuery = relation.getQuery();
            List<ParseObject> databaseSpeakersList = relationQuery.find();
            List<Speaker> domainSpeakersList = new ArrayList<>();
            for (ParseObject databaseSpeaker : databaseSpeakersList) {
                Speaker domainSpeaker = createSpeakerFromDatabaseInformation(databaseSpeaker);
                domainSpeakersList.add(domainSpeaker);
            }
            Event domainEvent = createDomainEventFromDatabase(databaseEvent);
            domainEvent.setSpeakers(domainSpeakersList);
            domainEventsList.add(domainEvent);
        }
        return domainEventsList;
    }

    public Event getEvent(String eventId) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        ParseObject databaseEvent = query.get(eventId);
        return createDomainEventFromDatabase(databaseEvent);
    }

    private ParseObject getEventDatabase(String eventId) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        return query.get(eventId);
    }

    private ParseObject getSubscriberDatabase(String subscriberId) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.SUBSCRIBERS_TABLE);
        return query.get(subscriberId);
    }

    public ParseObject getSubscriberByEmail(String subscriberEmail) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.SUBSCRIBERS_TABLE);
        query.whereEqualTo(CoreConstants.FIELD_EMAIL, subscriberEmail);
        return query.getFirst();
    }

    public String getSubscriberIdByEmail(String subscriberEmail) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.SUBSCRIBERS_TABLE);
        query.whereEqualTo(CoreConstants.FIELD_EMAIL, subscriberEmail);
        String objectId;
        try {
            objectId = query.getFirst().getObjectId();
        } catch (ParseException e) {
            objectId = "";
        }
        return objectId;
    }

    public boolean isSubscribed(String subscriberId, String eventId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.EVENTS_TO_SUBSCRIBERS_TABLE);
        try {
            query.whereEqualTo(CoreConstants.FIELD_EVENTS, getEventDatabase(eventId));
            query.whereEqualTo(CoreConstants.FIELD_SUBSCRIBERS, getSubscriberDatabase(subscriberId));
            query.getFirst();
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public Event setCheckIn(String eventId, String subscriberMail) throws ParseException {
        ParseQuery<ParseObject> eventsQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        ParseObject event = eventsQuery.get(eventId);
        ParseObject subscriber = getSubscriberByEmail(subscriberMail);
        ParseQuery<ParseObject> eventToSubsQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TO_SUBSCRIBERS_TABLE);
        eventToSubsQuery.whereEqualTo(CoreConstants.FIELD_EVENTS, event);
        eventToSubsQuery.whereEqualTo(CoreConstants.FIELD_SUBSCRIBERS, subscriber);
        ParseObject subscription = eventToSubsQuery.getFirst();
        subscription.put(CoreConstants.FIELD_CHECK_IN, true);
        subscription.save();
        return createDomainEventFromDatabase(event);
    }

    public void setAccepted(String eventId, List<Subscriber> subscribers) throws ParseException {
        ParseQuery<ParseObject> eventToSubsQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TO_SUBSCRIBERS_TABLE);
        ParseObject event = getEventParseObject(eventId);
        eventToSubsQuery.whereEqualTo(CoreConstants.FIELD_EVENTS, event);
        for (Subscriber subscriber : subscribers) {
            ParseObject parseSubscriber = getSubscriberDatabase(subscriber.getObjectID());
            ParseQuery<ParseObject> row = eventToSubsQuery;
            row.whereEqualTo(CoreConstants.FIELD_SUBSCRIBERS, parseSubscriber);
            ParseObject subscription = row.getFirst();
            subscription.put(CoreConstants.FIELD_ACCEPTED, subscriber.isAccepted());
            subscription.save();
        }
    }

    private ParseObject getEventParseObject(String eventId) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        return query.get(eventId);
    }

    public List<Speaker> getEventSpeakers(String eventId) throws ParseException {
        List<Speaker> speakers = new ArrayList<>();
        ParseQuery<ParseObject> eventsQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        ParseObject event = eventsQuery.get(eventId);
        ParseQuery<ParseObject> speakersQuery = ParseQuery.getQuery(CoreConstants.SPEAKERS_TABLE);
        ParseRelation<ParseObject> relation = event.getRelation(CoreConstants.FIELD_SPEAKERS);
        List<ParseObject> speakersEvents = relation.getQuery().find();
        for (ParseObject speakerParseObject : speakersEvents) {
            speakers.add(createSpeakerFromDatabaseInformation(speakersQuery.get(speakerParseObject.getObjectId())));
        }
        return speakers;
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
            //eventToSubscriberRow get Accepted
            ParseObject databaseSubscriber = eventToSubscribersRow.getParseObject(CoreConstants.FIELD_SUBSCRIBERS);
            Boolean accepted = eventToSubscribersRow.getBoolean(CoreConstants.FIELD_ACCEPTED);
            Boolean checkin = eventToSubscribersRow.getBoolean(CoreConstants.FIELD_CHECK_IN);
            if (databaseSubscriber != null) {
                Subscriber domainSubscriber = createDomainSubscriberFromDatabase(databaseSubscriber, accepted, checkin);
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

    public void updateEvent(Event domainEvent) throws ParseException {
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

    public void createSpeaker(Speaker domainSpeaker) throws ParseException {
        ParseObject databaseSpeaker = new ParseObject(CoreConstants.SPEAKERS_TABLE);
        setDatabaseSpeakerInformation(domainSpeaker, databaseSpeaker);
        databaseSpeaker.save();
    }

    public String createSubscriber(Subscriber domainSubscriber) throws ParseException {
        ParseObject databaseSubscriber = new ParseObject(CoreConstants.SUBSCRIBERS_TABLE);
        setDatabaseSubscriberInformation(domainSubscriber, databaseSubscriber);
        databaseSubscriber.save();
        return databaseSubscriber.getObjectId();
    }

    public void createEventToSubscriber(Subscriber domainSubscriber, String eventId) throws ParseException {
        ParseObject databaseSubscriber = getSubscriberDatabase(domainSubscriber.getObjectID());
        ParseObject databaseEvent = getEventDatabase(eventId);
        ParseObject databaseEventToSubscriber = new ParseObject(CoreConstants.EVENTS_TO_SUBSCRIBERS_TABLE);
        setDatabaseEventToSubscriberInformation(domainSubscriber, databaseSubscriber, databaseEvent, databaseEventToSubscriber);
        databaseEventToSubscriber.save();
    }

    private byte[] getImageFromDatabase(ParseObject databaseObject, String field) throws ParseException {
        ParseFile file = databaseObject.getParseFile(field);
        if (file != null) {
            return file.getData();
        } else {
            return null;
        }
    }

    private double getCoordinatesFromDatabaseObject(ParseObject databaseObject, boolean latitude) {
        ParseGeoPoint geoPoint = databaseObject.getParseGeoPoint(CoreConstants.FIELD_MAP_COORDINATES);
        if (geoPoint != null) {
            if (latitude) {
                return geoPoint.getLatitude();
            } else {
                return geoPoint.getLongitude();
            }
        } else {
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

    private Subscriber createDomainSubscriberFromDatabase(ParseObject databaseSubscriber, Boolean accepted, Boolean checkin) throws ParseException {
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
        domainSubscriber.setAccepted(accepted);
        domainSubscriber.setCheckIn(checkin);
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
        databaseEvent.put(CoreConstants.FIELD_ICON, new ParseFile("picture.png", domainEvent.getIcon()));
        databaseEvent.put(CoreConstants.FIELD_EVENT_LOGO, new ParseFile("picture.png", domainEvent.getEventLogo()));
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
        databaseSpeaker.put(CoreConstants.FIELD_PICTURE, new ParseFile("picture.png", domainSpeaker.getPicture()));
    }

    private void setDatabaseSubscriberInformation(Subscriber domainSubscriber, ParseObject databaseSpeaker) {
        databaseSpeaker.put(CoreConstants.FIELD_NAME, domainSubscriber.getName());
        databaseSpeaker.put(CoreConstants.FIELD_LAST_NAME, domainSubscriber.getLastName());
        databaseSpeaker.put(CoreConstants.FIELD_EMAIL, domainSubscriber.getEmail());
        databaseSpeaker.put(CoreConstants.FIELD_PHONE, domainSubscriber.getPhone());
        databaseSpeaker.put(CoreConstants.FIELD_OCCUPATION, domainSubscriber.getOccupation());
        databaseSpeaker.put(CoreConstants.FIELD_GLOBER, domainSubscriber.isGlober());
        databaseSpeaker.put(CoreConstants.FIELD_TWITTER_USER, domainSubscriber.getTwitterUser());
        databaseSpeaker.put(CoreConstants.FIELD_ENGLISH, domainSubscriber.speaksEnglish());
        databaseSpeaker.put(CoreConstants.FIELD_CITY, domainSubscriber.getCity());
        databaseSpeaker.put(CoreConstants.FIELD_COUNTRY, domainSubscriber.getCountry());
        databaseSpeaker.put(CoreConstants.FIELD_PICTURE, new ParseFile("picture.png", domainSubscriber.getPicture()));
    }

    private void setDatabaseEventToSubscriberInformation(Subscriber domainSubscriber, ParseObject databaseSubscriber, ParseObject databaseEvent, ParseObject databaseEventToSubscriber) {
        databaseEventToSubscriber.put(CoreConstants.FIELD_SUBSCRIBERS, databaseSubscriber);
        databaseEventToSubscriber.put(CoreConstants.FIELD_EVENTS, databaseEvent);
        databaseEventToSubscriber.put(CoreConstants.FIELD_PUBLIC, domainSubscriber.isPublic());
        databaseEventToSubscriber.put(CoreConstants.FIELD_ACCEPTED, domainSubscriber.isAccepted());
        databaseEventToSubscriber.put(CoreConstants.FIELD_CHECK_IN, domainSubscriber.checkedIn());
    }

    private Speaker createSpeakerFromDatabaseInformation(ParseObject databaseSpeaker) throws ParseException {
        Speaker speaker = new Speaker();
        speaker.setName(databaseSpeaker.getString(CoreConstants.FIELD_NAME));
        speaker.setLastName(databaseSpeaker.getString(CoreConstants.FIELD_LAST_NAME));
        speaker.setTitle(databaseSpeaker.getString(CoreConstants.FIELD_TITLE));
        speaker.setPicture(getImageFromDatabase(databaseSpeaker, CoreConstants.FIELD_PICTURE));
        speaker.setBiography(databaseSpeaker.getString(CoreConstants.FIELD_BIOGRAPHY));
        return speaker;
    }
}
