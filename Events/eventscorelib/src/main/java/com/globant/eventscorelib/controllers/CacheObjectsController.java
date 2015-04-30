package com.globant.eventscorelib.controllers;


import com.globant.eventscorelib.domainObjects.Event;

import java.util.List;

import twitter4j.Status;
import twitter4j.User;

public class CacheObjectsController {
    private List<twitter4j.Status> tweetList;
    private User user; // twitter
    private Event event;

    public List<Status> getTweetList() {
        return tweetList;
    }

    public void setTweetList(List<Status> tweetList) {
        this.tweetList = tweetList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getEventId () {
        return event.getObjectID();
    }

    public void setEventId (String id) {
        this.event.setObjectID(id);
    }
}
