package com.globant.eventscorelib.controllers;


import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;

import java.util.List;

import twitter4j.Status;
import twitter4j.User;

public class CacheObjectsController {

    private List<twitter4j.Status> mTweetList;
    private List<Speaker> mSpeakersList;
    private User mUser; // twitter
    private Event mEvent;

    public List<Status> getTweetList() {
        return mTweetList;
    }

    public void setTweetList(List<Status> tweetList) {
        this.mTweetList = tweetList;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    public Event getEvent() {
        return mEvent;
    }

    public void setEvent(Event event) {
        this.mEvent = event;
    }

    public List<Speaker> getSpeakersList() {
        return mSpeakersList;
    }

    public void setSpeakersList(List<Speaker> speakersList) {
        mSpeakersList = speakersList;
    }
}
