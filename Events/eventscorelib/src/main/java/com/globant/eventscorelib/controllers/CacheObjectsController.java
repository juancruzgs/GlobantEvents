package com.globant.eventscorelib.controllers;


import android.os.Parcel;
import android.os.Parcelable;

import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;
import com.globant.eventscorelib.domainObjects.Subscriber;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.User;

public class CacheObjectsController implements Parcelable{

    private List<twitter4j.Status> mTweetList;
    private List<Speaker> mSpeakersList;
    private List<Event> mEventList;
    private List<Subscriber> mSubscriberList;
    private User mUser; // twitter
    private Event mEvent;
    private List<Event> mEventHistory;

    public CacheObjectsController() {
    }

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

    public List<Event> getEventList () { return mEventList; }

    public void setEventList (List<Event> eventList) { mEventList = eventList; }

    public List<Subscriber> getSubscriberList () { return mSubscriberList; }

    public void setSubscriberList (List<Subscriber> subscriberList) { mSubscriberList = subscriberList; }

    public List<Event> getEventHistory() {
        return mEventHistory;
    }

    public void setEventHistory(List<Event> eventHistory) {
        mEventHistory = eventHistory;
    }

    private CacheObjectsController(Parcel in) {
        mEventList = new ArrayList<>();
        in.readTypedList(mEventList, Event.CREATOR);
        mEventHistory = new ArrayList<>();
        in.readTypedList(mEventHistory, Event.CREATOR);
        mSpeakersList= new ArrayList<>();
        in.readTypedList(mSpeakersList, Speaker.CREATOR);
        mSubscriberList= new ArrayList<>();
        in.readTypedList(mSubscriberList, Subscriber.CREATOR);
        mTweetList = new ArrayList<>();
        in.readList(mTweetList, Twitter.class.getClassLoader());
        mEvent = in.readParcelable(Event.class.getClassLoader());
        mUser = (User) in.readSerializable();
    }

    static final Parcelable.Creator<CacheObjectsController> CREATOR = new Parcelable.Creator<CacheObjectsController>() {
        @Override
        public CacheObjectsController createFromParcel(Parcel source) {
            return new CacheObjectsController(source);
        }

        @Override
        public CacheObjectsController[] newArray(int size) {
            return new CacheObjectsController[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mEventList);
        dest.writeTypedList(mEventHistory);
        dest.writeTypedList(mSpeakersList);
        dest.writeTypedList(mSubscriberList);
        dest.writeList(mTweetList);
        dest.writeParcelable(mEvent, 1);
        dest.writeSerializable(mUser);
    }
}
