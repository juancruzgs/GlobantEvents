package com.globant.eventscorelib.controllers;

import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.domainObjects.Subscriber;
import com.globant.eventscorelib.utils.ConvertImage;
import com.globant.eventscorelib.utils.CoreConstants;
import com.google.android.gms.maps.model.LatLng;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class CloudDatabaseController extends DatabaseController {

    @Override
    protected void pinObjectInBackground(ParseObject object) {
        object.pinInBackground();
    }

    @Override
    protected void queryFromLocalDatastore(ParseQuery query) {
    }

    public Event getEvent(String eventId) throws ParseException {
        ParseObject databaseEvent = getDatabaseEvent(eventId);
        return createDomainEventFromDatabase(databaseEvent);
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
        query.selectKeys(Collections.singletonList(CoreConstants.FIELD_OBJECT_ID));
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
        ParseQuery<ParseObject> eventsInnerQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        eventsInnerQuery.whereEqualTo(CoreConstants.FIELD_OBJECT_ID, eventId);

        ParseQuery<ParseObject> subscribersInnerQuery = ParseQuery.getQuery(CoreConstants.SUBSCRIBERS_TABLE);
        subscribersInnerQuery.whereEqualTo(CoreConstants.FIELD_OBJECT_ID, subscriberId);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(CoreConstants.EVENTS_TO_SUBSCRIBERS_TABLE);
        query.selectKeys(Collections.singletonList(CoreConstants.FIELD_OBJECT_ID));
        query.whereMatchesQuery(CoreConstants.FIELD_EVENTS, eventsInnerQuery);
        query.whereMatchesQuery(CoreConstants.FIELD_SUBSCRIBERS, subscribersInnerQuery);
        try {
            query.getFirst();
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public Event setCheckIn(String eventId, String subscriberMail) throws ParseException {
        ParseObject event = getDatabaseEvent(eventId);

        ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery(CoreConstants.SUBSCRIBERS_TABLE);
        innerQuery.whereEqualTo(CoreConstants.FIELD_EMAIL, subscriberMail);

        ParseQuery<ParseObject> eventToSubsQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TO_SUBSCRIBERS_TABLE);
        eventToSubsQuery.whereEqualTo(CoreConstants.FIELD_EVENTS, event);
        eventToSubsQuery.whereMatchesQuery(CoreConstants.FIELD_SUBSCRIBERS, innerQuery);
        eventToSubsQuery.whereEqualTo(CoreConstants.FIELD_ACCEPTED, true);
        ParseObject subscription = eventToSubsQuery.getFirst();
        subscription.put(CoreConstants.FIELD_CHECK_IN, true);
        subscription.save();
        return createDomainEventFromDatabase(event);
    }

    public void setAccepted(String eventId, List<String> subscribersId) throws ParseException {
        ParseQuery<ParseObject> eventsInnerQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        eventsInnerQuery.whereEqualTo(CoreConstants.FIELD_OBJECT_ID, eventId);

        ParseQuery<ParseObject> subscribersInnerQuery = ParseQuery.getQuery(CoreConstants.SUBSCRIBERS_TABLE);
        subscribersInnerQuery.whereContainedIn(CoreConstants.FIELD_OBJECT_ID, subscribersId);

        ParseQuery<ParseObject> eventToSubsQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TO_SUBSCRIBERS_TABLE);
        eventToSubsQuery.whereMatchesQuery(CoreConstants.FIELD_EVENTS, eventsInnerQuery);
        eventToSubsQuery.whereMatchesQuery(CoreConstants.FIELD_SUBSCRIBERS, subscribersInnerQuery);
        List<ParseObject> eventToSubs = eventToSubsQuery.find();
        for (ParseObject subscription : eventToSubs) {
            subscription.put(CoreConstants.FIELD_ACCEPTED, true);
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
        ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        innerQuery.whereEqualTo(CoreConstants.FIELD_OBJECT_ID, eventId);

        ParseQuery<ParseObject> eventsToSubscribersQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TO_SUBSCRIBERS_TABLE);
        eventsToSubscribersQuery.selectKeys(Arrays.asList(CoreConstants.FIELD_SUBSCRIBERS, CoreConstants.FIELD_ACCEPTED,
                CoreConstants.FIELD_CHECK_IN));
        eventsToSubscribersQuery.whereMatchesQuery(CoreConstants.FIELD_EVENTS, innerQuery);
        eventsToSubscribersQuery.include(CoreConstants.FIELD_SUBSCRIBERS);

        List<ParseObject> eventsToSubscribersList = eventsToSubscribersQuery.find();
        List<Subscriber> subscribersList = new ArrayList<>();
        for (ParseObject eventToSubscribersRow : eventsToSubscribersList) {
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

        List<Speaker> speakerList = domainEvent.getSpeakers();
        if (!speakerList.isEmpty()) {

            List<String> speakersIDs = new ArrayList<>();
            for (Speaker speaker : speakerList) {
                speakersIDs.add(createSpeaker(speaker));
            }

            addEventSpeakers(databaseEvent.getObjectId(), speakersIDs);
        }
    }

    public void updateEvent(Event domainEvent) throws ParseException {
        List<Speaker> oldSpeakerList = getEventSpeakers(domainEvent.getObjectID());
        List<String> IdListToRemove = new ArrayList<>();
        if (oldSpeakerList != null && !oldSpeakerList.isEmpty()) {
            for (Speaker speaker : oldSpeakerList)
                IdListToRemove.add(speaker.getObjectID());
        }

        List<Speaker> newSpeakerList = domainEvent.getSpeakers();
        List<String> IdListToAdd = new ArrayList<>();
        if (newSpeakerList != null && !newSpeakerList.isEmpty()) {
            for (Speaker speaker : newSpeakerList) {
                IdListToAdd.add(createSpeaker(speaker));
            }
        }

        if (!IdListToRemove.isEmpty() && !IdListToAdd.isEmpty()) {
            ParseQuery<ParseObject> eventsQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
            ParseObject event = eventsQuery.get(domainEvent.getObjectID());
            ParseQuery<ParseObject> speakersQuery = ParseQuery.getQuery(CoreConstants.SPEAKERS_TABLE);
            ParseRelation<ParseObject> relation = event.getRelation(CoreConstants.FIELD_SPEAKERS);

            if (!IdListToRemove.isEmpty())
                for (String speakerId : IdListToRemove) {
                    relation.remove(speakersQuery.get(speakerId));
                    (speakersQuery.get(speakerId)).delete();
                }

            if (!IdListToAdd.isEmpty())
                for (String speakerId : IdListToAdd)
                    relation.add(speakersQuery.get(speakerId));

            event.save();
        }

        // Update Event
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
        //TODO Delete entries from EventsToSubscribers too
        ParseQuery<ParseObject> eventsQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        ParseObject event = eventsQuery.get(domainEvent.getObjectID());

        List<Speaker> speakers = domainEvent.getSpeakers();
        List<String> IdList = new ArrayList<>();
        if (speakers != null && !speakers.isEmpty()) {
            for (Speaker speaker : speakers)
                IdList.add(speaker.getObjectID());
            deleteEventSpeakers(domainEvent.getObjectID(), IdList);
        }
        event.delete();
    }

    public String createSpeaker(Speaker domainSpeaker) throws ParseException {
        ParseObject databaseSpeaker = new ParseObject(CoreConstants.SPEAKERS_TABLE);
        setDatabaseSpeakerInformation(domainSpeaker, databaseSpeaker);
        databaseSpeaker.save();
        return databaseSpeaker.getObjectId();
    }

    public String createSubscriber(Subscriber domainSubscriber) throws ParseException {
        ParseObject databaseSubscriber = new ParseObject(CoreConstants.SUBSCRIBERS_TABLE);
        setDatabaseSubscriberInformation(domainSubscriber, databaseSubscriber);
        databaseSubscriber.save();
        return databaseSubscriber.getObjectId();
    }

    public String updateSubscriber(Subscriber domainSubscriber) throws ParseException {
        ParseObject databaseSubscriber = ParseObject.createWithoutData(CoreConstants.SUBSCRIBERS_TABLE, domainSubscriber.getObjectID());
        setDatabaseSubscriberInformation(domainSubscriber, databaseSubscriber);
        databaseSubscriber.save();
        return databaseSubscriber.getObjectId();
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
        domainSubscriber.setPicture(getBitmapFromDatabase(databaseSubscriber, CoreConstants.FIELD_PICTURE));
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
            byte[] imageIcon = ConvertImage.convertBitmapToByteArrayAndCompress(domainEvent.getIcon());
            databaseEvent.put(CoreConstants.FIELD_ICON, new ParseFile("picture.jpeg", imageIcon));
        }
        if (domainEvent.getEventLogo() != null) {
            byte[] imageLogo = ConvertImage.convertBitmapToByteArrayAndCompress(domainEvent.getEventLogo());
            databaseEvent.put(CoreConstants.FIELD_EVENT_LOGO, new ParseFile("picture.jpeg", imageLogo));
        }
        databaseEvent.put(CoreConstants.FIELD_FULL_DESCRIPTION, domainEvent.getFullDescription());
        String additionalInfo = domainEvent.getAdditionalInfo();
        databaseEvent.put(CoreConstants.FIELD_ADDITIONAL_INFO, additionalInfo != null ? additionalInfo : JSONObject.NULL);
        databaseEvent.put(CoreConstants.FIELD_ADDRESS, domainEvent.getAddress());
        databaseEvent.put(CoreConstants.FIELD_LANGUAGE, domainEvent.getLanguage());
        databaseEvent.put(CoreConstants.FIELD_HASHTAG, domainEvent.getHashtag());
        LatLng coordinates = domainEvent.getCoordinates();
        if (coordinates != null)
            databaseEvent.put(CoreConstants.FIELD_MAP_COORDINATES, new ParseGeoPoint(coordinates.latitude, coordinates.longitude));
    }

    private void setDatabaseSpeakerInformation(Speaker domainSpeaker, ParseObject databaseSpeaker) {
        databaseSpeaker.put(CoreConstants.FIELD_TITLE, domainSpeaker.getTitle());
        databaseSpeaker.put(CoreConstants.FIELD_NAME, domainSpeaker.getName());
        databaseSpeaker.put(CoreConstants.FIELD_LAST_NAME, domainSpeaker.getLastName());
        databaseSpeaker.put(CoreConstants.FIELD_BIOGRAPHY, domainSpeaker.getBiography());
        if (domainSpeaker.getPicture() != null) {
            byte[] image = ConvertImage.convertBitmapToByteArrayAndCompress(domainSpeaker.getPicture());
            databaseSpeaker.put(CoreConstants.FIELD_PICTURE, new ParseFile("picture.jpeg", image));
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
        } else {
            databaseSubscriber.put(CoreConstants.FIELD_TWITTER_USER, JSONObject.NULL);
        }
        databaseSubscriber.put(CoreConstants.FIELD_ENGLISH, domainSubscriber.speaksEnglish());
        databaseSubscriber.put(CoreConstants.FIELD_CITY, domainSubscriber.getCity());
        databaseSubscriber.put(CoreConstants.FIELD_COUNTRY, domainSubscriber.getCountry());
        if (domainSubscriber.getPicture() != null) {
            byte[] image = ConvertImage.convertBitmapToByteArrayAndCompress(domainSubscriber.getPicture());
            databaseSubscriber.put(CoreConstants.FIELD_PICTURE, new ParseFile("picture.jpeg", image));
        }
    }

    private void setDatabaseEventToSubscriberInformation(Subscriber domainSubscriber, ParseObject databaseSubscriber, ParseObject databaseEvent, ParseObject databaseEventToSubscriber) {
        databaseEventToSubscriber.put(CoreConstants.FIELD_SUBSCRIBERS, databaseSubscriber);
        databaseEventToSubscriber.put(CoreConstants.FIELD_EVENTS, databaseEvent);
        databaseEventToSubscriber.put(CoreConstants.FIELD_PUBLIC, domainSubscriber.isPublic());
        databaseEventToSubscriber.put(CoreConstants.FIELD_ACCEPTED, domainSubscriber.isAccepted());
        databaseEventToSubscriber.put(CoreConstants.FIELD_CHECK_IN, domainSubscriber.checkedIn());
    }

    public List<Subscriber> refreshSubscribers(String eventId, Date refreshDate) throws ParseException {
        ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TABLE);
        innerQuery.whereEqualTo(CoreConstants.FIELD_OBJECT_ID, eventId);

        ParseQuery<ParseObject> eventsToSubscribersQuery = ParseQuery.getQuery(CoreConstants.EVENTS_TO_SUBSCRIBERS_TABLE);
        eventsToSubscribersQuery.whereMatchesQuery(CoreConstants.FIELD_EVENTS, innerQuery);
        eventsToSubscribersQuery.whereGreaterThan(CoreConstants.FIELD_CREATED_AT, refreshDate);
        eventsToSubscribersQuery.include(CoreConstants.FIELD_SUBSCRIBERS);
        List<ParseObject> eventsToSubscribersList = eventsToSubscribersQuery.find();
        List<Subscriber> subscribersList = new ArrayList<>();
        for (ParseObject eventToSubscribersRow : eventsToSubscribersList) {
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
}
