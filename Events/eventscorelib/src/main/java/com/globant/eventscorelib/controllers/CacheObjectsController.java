package com.globant.eventscorelib.controllers;


import android.os.Parcel;
import android.os.Parcelable;

import com.globant.eventscorelib.domainObjects.Event;
import com.globant.eventscorelib.domainObjects.Speaker;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.User;

public class CacheObjectsController implements Parcelable{

    private List<twitter4j.Status> mTweetList;
    private List<Speaker> mSpeakersList;
    private List<Event> mEventList;
    private User mUser; // twitter
    private Event mEvent;

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

    private CacheObjectsController(Parcel in) {
        mEventList = new ArrayList<>();
        in.readTypedList(mEventList, Event.CREATOR);
        mEvent = in.readParcelable(Event.class.getClassLoader());
        mSpeakersList= new ArrayList<>();
        in.readTypedList(mSpeakersList, Speaker.CREATOR);
        mTweetList = new ArrayList<>();
        in.readList(mTweetList, Twitter.class.getClassLoader());
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
        dest.writeParcelable(mEvent, 1);
        dest.writeTypedList(mSpeakersList);
        dest.writeList(mTweetList);
        dest.writeSerializable(mUser);

    }
}
