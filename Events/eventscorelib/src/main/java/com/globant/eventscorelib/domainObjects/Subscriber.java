package com.globant.eventscorelib.domainObjects;

public class Subscriber {

    private String mObjectID;
    private String mName;
    private String mLastName;
    private String mEmail;
    private String mPhone;
    private String mOccupation;
    private String mTwitterUser;
    private String mCity;
    private String mCountry;
    private byte[] mPicture;
    private boolean mEnglish;
    private boolean mGlober;
    private boolean mPublic;
    private boolean mAccepted;
    private boolean mCheckIn;

    public Subscriber() {
    }

    public Subscriber(String name, String lastName, String email, String phone, String occupation, String twitterUser, String city, String country, byte[] picture, boolean english, boolean glober, boolean aPublic, boolean accepted, boolean checkIn) {
        mName = name;
        mLastName = lastName;
        mEmail = email;
        mPhone = phone;
        mOccupation = occupation;
        mTwitterUser = twitterUser;
        mCity = city;
        mCountry = country;
        mPicture = picture;
        mEnglish = english;
        mGlober = glober;
        mPublic = aPublic;
        mAccepted = accepted;
        mCheckIn = checkIn;
    }

    public String getObjectID() {
        return mObjectID;
    }

    public void setObjectID(String objectID) {
        mObjectID = objectID;
    }

    public boolean checkedIn() {
        return mCheckIn;
    }

    public void setCheckIn(boolean checkIn) {
        mCheckIn = checkIn;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getOccupation() {
        return mOccupation;
    }

    public void setOccupation(String occupation) {
        mOccupation = occupation;
    }

    public byte[] getPicture() {
        return mPicture;
    }

    public void setPicture(byte[] picture) {
        mPicture = picture;
    }

    public boolean speaksEnglish() {
        return mEnglish;
    }

    public void setEnglish(boolean english) {
        mEnglish = english;
    }

    public String getTwitterUser() {
        return mTwitterUser;
    }

    public void setTwitterUser(String twitterUser) {
        mTwitterUser = twitterUser;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public boolean isGlober() {
        return mGlober;
    }

    public void setGlober(boolean glober) {
        mGlober = glober;
    }

    public boolean isPublic() {
        return mPublic;
    }

    public void setPublic(boolean aPublic) {
        mPublic = aPublic;
    }

    public boolean isAccepted() {
        return mAccepted;
    }

    public void setAccepted(boolean accepted) {
        mAccepted = accepted;
    }
}


