package com.globant.eventscorelib.domainObjects;


public class Speaker extends BaseObject{

    private String mObjectID;
    private String mTitle;
    private String mName;
    private String mLastName;
    private String mBiography;
    private byte[] mPicture;

    public Speaker() {
    }

    public Speaker(String title, String name, String lastName, String biography, byte[] picture) {
        mTitle = title;
        mName = name;
        mLastName = lastName;
        mBiography = biography;
        mPicture = picture;
    }

    public String getObjectID() {
        return mObjectID;
    }

    public void setObjectID(String objectID) {
        mObjectID = objectID;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getBiography() {
        return mBiography;
    }

    public void setBiography(String biography) {
        this.mBiography = biography;
    }

    public byte[] getPicture() {
        return mPicture;
    }

    public void setPicture(byte[] picture) {
        this.mPicture = picture;
    }
}
