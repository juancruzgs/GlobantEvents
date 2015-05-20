package com.globant.eventscorelib.controllers;

import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.domainObjects.Subscriber;
import com.globant.eventscorelib.utils.CoreConstants;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CloudDatabaseController extends DatabaseController{

    @Override
    protected void pinObjectInBackground(ParseObject object) {
        object.pinInBackground();
    }

    @Override
    protected void queryFromLocalDatastore(ParseQuery query) {}

    public Event getEvent(String eventId) throws ParseException {
        ParseObject databaseEvent = getDatabaseEvent(eventId);
        return createDomainEventFromDatabase(databaseEvent);
    }

    public Event getEventWithSpeakers(String eventId) throws ParseException {
        ParseObject databaseEvent = getDatabaseEvent(eventId);
        ParseRelation relation = databaseEvent.getRelation(CoreConstants.FIELD_SPEAKERS);
        ParseQuery relationQuery = relation.getQuery();
        List<ParseObject> databaseSpeakersList = relationQuery.find();
        List<Speaker> domainSpeakersList = new ArrayList<>();
        for (ParseObject databaseSpeaker : databaseSpeakersList) {
            Speaker domainSpeaker = createDomainSpeakerFromDatabase(databaseSpeaker);
            domainSpeakersList.add(domainSpeaker);
        }
        Event domainEvent = createDomainEventFromDatabase(databaseEvent);
        domainEvent.setSpeakers(domainSpeakersList);
        return domainEvent;
    }


    public List<Event> getEventHistory() throws ParseException {
        ParseQuery<ParseObject> eventsQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        eventsQuery.orderByDescending(CoreConstants.FIELD_START_DATE);
        List<ParseObject> databaseEventsList = eventsQuery.find();
        List<Event> domainEventsList = new ArrayList<>();
        for (ParseObject databaseEvent : databaseEventsList) {
             Event domainEvent = createDomainEventFromDatabase(databaseEvent);
             domainEventsList.add(domainEvent);
        }
        return domainEventsList;
    }

    private ParseObject getDatabaseEvent(String eventId) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        return query.get(eventId);
    }

    private ParseObject getDatabaseSubscriber(String subscriberId) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.SUBSCRIBERS_TABLE);
        return query.get(subscriberId);
    }

    public String getSubscriberId(String subscriberEmail) {
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
            query.whereEqualTo(CoreConstants.FIELD_EVENTS, getDatabaseEvent(eventId));
            query.whereEqualTo(CoreConstants.FIELD_SUBSCRIBERS, getDatabaseSubscriber(subscriberId));
            query.getFirst();
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public Event setCheckIn(String eventId, String subscriberMail) throws ParseException {
        ParseObject event = getDatabaseEvent(eventId);
        ParseObject subscriber = getDatabaseSubscriberByEmail(subscriberMail);
        ParseQuery<ParseObject> eventToSubsQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TO_SUBSCRIBERS_TABLE);
        eventToSubsQuery.whereEqualTo(CoreConstants.FIELD_EVENTS, event);
        eventToSubsQuery.whereEqualTo(CoreConstants.FIELD_SUBSCRIBERS, subscriber);
        eventToSubsQuery.whereEqualTo(CoreConstants.FIELD_ACCEPTED, true);
        ParseObject subscription = eventToSubsQuery.getFirst();
        subscription.put(CoreConstants.FIELD_CHECK_IN, true);
        subscription.save();
        return createDomainEventFromDatabase(event);
    }

    private ParseObject getDatabaseSubscriberByEmail(String subscriberEmail) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.SUBSCRIBERS_TABLE);
        query.whereEqualTo(CoreConstants.FIELD_EMAIL, subscriberEmail);
        return query.getFirst();
    }

    public void setAccepted(String eventId, List<Subscriber> subscribers) throws ParseException {
        ParseQuery<ParseObject> eventToSubsQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TO_SUBSCRIBERS_TABLE);
        ParseObject event = getDatabaseEvent(eventId);
        eventToSubsQuery.whereEqualTo(CoreConstants.FIELD_EVENTS, event);
        for (Subscriber subscriber : subscribers) {
            ParseObject databaseSubscriber = getDatabaseSubscriber(subscriber.getObjectID());
            eventToSubsQuery.whereEqualTo(CoreConstants.FIELD_SUBSCRIBERS, databaseSubscriber);
            ParseObject subscription = eventToSubsQuery.getFirst();
            subscription.put(CoreConstants.FIELD_ACCEPTED, subscriber.isAccepted());
            subscription.save();
        }
    }

    public List<Speaker> getEventSpeakers(String eventId) throws ParseException {
        List<Speaker> speakers = new ArrayList<>();
        ParseObject event = getDatabaseEvent(eventId);
        ParseRelation<ParseObject> relation = event.getRelation(CoreConstants.FIELD_SPEAKERS);
        List<ParseObject> speakersEvents = relation.getQuery().find();
        for (ParseObject databaseSpeaker : speakersEvents) {
            speakers.add(createDomainSpeakerFromDatabase(databaseSpeaker));
        }
        return speakers;
    }

    public List<Subscriber> getEventSubscribers(String eventId) throws ParseException {
        ParseObject event = getDatabaseEvent(eventId);
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

    public void deleteEvent(Event domainEvent) throws ParseException {
        ParseQuery<ParseObject> eventsQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        ParseObject event = eventsQuery.get(domainEvent.getObjectID());

        List<Speaker> speakers = domainEvent.getSpeakers();
        List<String>IdList = new ArrayList<>();
        if (speakers != null && !speakers.isEmpty()){
            for (Speaker speaker: speakers)
              IdList.add(speaker.getObjectID());
            deleteEventSpeakers(domainEvent.getObjectID(),IdList);
        }
        event.delete();
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

    public void updateSubscriber(Subscriber domainSubscriber) throws ParseException {
        ParseObject databaseSubscriber = ParseObject.createWithoutData(CoreConstants.SUBSCRIBERS_TABLE, domainSubscriber.getObjectID());
        setDatabaseSubscriberInformation(domainSubscriber, databaseSubscriber);
        databaseSubscriber.save();
    }

    public void createEventToSubscriber(Subscriber domainSubscriber, String eventId) throws ParseException {
        ParseObject databaseSubscriber = getDatabaseSubscriber(domainSubscriber.getObjectID());
        ParseObject databaseEvent = getDatabaseEvent(eventId);
        ParseObject databaseEventToSubscriber = new ParseObject(CoreConstants.EVENTS_TO_SUBSCRIBERS_TABLE);
        setDatabaseEventToSubscriberInformation(domainSubscriber, databaseSubscriber, databaseEvent, databaseEventToSubscriber);
        databaseEventToSubscriber.save();
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
        databaseEvent.put(CoreConstants.FIELD_SHORT_DESCRIPTION, domainEvent.getShortDescription());
        databaseEvent.put(CoreConstants.FIELD_CITY, domainEvent.getCity());
        databaseEvent.put(CoreConstants.FIELD_COUNTRY, domainEvent.getCountry());
        databaseEvent.put(CoreConstants.FIELD_CATEGORY, domainEvent.getCategory());
        databaseEvent.put(CoreConstants.FIELD_START_DATE, domainEvent.getStartDate());
        databaseEvent.put(CoreConstants.FIELD_END_DATE, domainEvent.getEndDate());
        databaseEvent.put(CoreConstants.FIELD_PUBLIC, domainEvent.isPublic());
        if (domainEvent.getIcon() != null) {
            databaseEvent.put(CoreConstants.FIELD_ICON, new ParseFile("picture.png", domainEvent.getIcon()));
        }
        if (domainEvent.getEventLogo() != null) {
            databaseEvent.put(CoreConstants.FIELD_EVENT_LOGO, new ParseFile("picture.png", domainEvent.getEventLogo()));
        }
        databaseEvent.put(CoreConstants.FIELD_FULL_DESCRIPTION, domainEvent.getFullDescription());
        databaseEvent.put(CoreConstants.FIELD_ADDITIONAL_INFO, domainEvent.getAdditionalInfo());
        databaseEvent.put(CoreConstants.FIELD_ADDRESS, domainEvent.getAddress());
        databaseEvent.put(CoreConstants.FIELD_LANGUAGE, domainEvent.getLanguage());
        databaseEvent.put(CoreConstants.FIELD_HASHTAG, domainEvent.getHashtag());
        LatLng coordinates = domainEvent.getCoordinates();
        databaseEvent.put(CoreConstants.FIELD_MAP_COORDINATES, new ParseGeoPoint(coordinates.latitude, coordinates.longitude));
    }

    private void setDatabaseSpeakerInformation(Speaker domainSpeaker, ParseObject databaseSpeaker) {
        databaseSpeaker.put(CoreConstants.FIELD_TITLE, domainSpeaker.getTitle());
        databaseSpeaker.put(CoreConstants.FIELD_NAME, domainSpeaker.getName());
        databaseSpeaker.put(CoreConstants.FIELD_LAST_NAME, domainSpeaker.getLastName());
        databaseSpeaker.put(CoreConstants.FIELD_BIOGRAPHY, domainSpeaker.getBiography());
        if (domainSpeaker.getPicture() != null) {
            databaseSpeaker.put(CoreConstants.FIELD_PICTURE, new ParseFile("picture.png", domainSpeaker.getPicture()));
        }
    }

    private void setDatabaseSubscriberInformation(Subscriber domainSubscriber, ParseObject databaseSubscriber) {
        databaseSubscriber.put(CoreConstants.FIELD_NAME, domainSubscriber.getName());
        databaseSubscriber.put(CoreConstants.FIELD_LAST_NAME, domainSubscriber.getLastName());
        databaseSubscriber.put(CoreConstants.FIELD_EMAIL, domainSubscriber.getEmail());
        databaseSubscriber.put(CoreConstants.FIELD_PHONE, domainSubscriber.getPhone());
        databaseSubscriber.put(CoreConstants.FIELD_OCCUPATION, domainSubscriber.getOccupation());
        databaseSubscriber.put(CoreConstants.FIELD_GLOBER, domainSubscriber.isGlober());
        if (domainSubscriber.getTwitterUser() != null) {
            databaseSubscriber.put(CoreConstants.FIELD_TWITTER_USER, domainSubscriber.getTwitterUser());
        }
        else{
            databaseSubscriber.put(CoreConstants.FIELD_TWITTER_USER, JSONObject.NULL);
        }
        databaseSubscriber.put(CoreConstants.FIELD_ENGLISH, domainSubscriber.speaksEnglish());
        databaseSubscriber.put(CoreConstants.FIELD_CITY, domainSubscriber.getCity());
        databaseSubscriber.put(CoreConstants.FIELD_COUNTRY, domainSubscriber.getCountry());
        if (domainSubscriber.getPicture() != null) {
            databaseSubscriber.put(CoreConstants.FIELD_PICTURE, new ParseFile("picture.png", domainSubscriber.getPicture()));
        }
    }

    private void setDatabaseEventToSubscriberInformation(Subscriber domainSubscriber, ParseObject databaseSubscriber, ParseObject databaseEvent, ParseObject databaseEventToSubscriber) {
        databaseEventToSubscriber.put(CoreConstants.FIELD_SUBSCRIBERS, databaseSubscriber);
        databaseEventToSubscriber.put(CoreConstants.FIELD_EVENTS, databaseEvent);
        databaseEventToSubscriber.put(CoreConstants.FIELD_PUBLIC, domainSubscriber.isPublic());
        databaseEventToSubscriber.put(CoreConstants.FIELD_ACCEPTED, domainSubscriber.isAccepted());
        databaseEventToSubscriber.put(CoreConstants.FIELD_CHECK_IN, domainSubscriber.checkedIn());
    }
}
