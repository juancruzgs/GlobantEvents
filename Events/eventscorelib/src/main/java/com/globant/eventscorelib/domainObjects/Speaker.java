package com.globant.eventscorelib.domainObjects;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


public class Speaker extends BaseObject implements Parcelable {

    private String mObjectID;
    private String mTitle;
    private String mName;
    private String mLastName;
    private String mBiography;
    private Bitmap mPicture;

    public Speaker() {
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

    public Bitmap getPicture() {
        return mPicture;
    }

    public void setPicture(Bitmap picture) {
        this.mPicture = picture;
    }

    private Speaker(Parcel in) {
        mObjectID = in.readString();
        mTitle = in.readString();
        mName = in.readString();
        mLastName = in.readString();
        mBiography = in.readString();
        mPicture = in.readParcelable(null);
    }

   public static final Parcelable.Creator<Speaker> CREATOR = new Parcelable.Creator<Speaker>() {
        @Override
        public Speaker createFromParcel(Parcel source) {
           return new Speaker(source);
        }

        @Override
        public Speaker[] newArray(int size) {
            return new Speaker[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mObjectID);
        dest.writeString(mTitle);
        dest.writeString(mName);
        dest.writeString(mLastName);
        dest.writeString(mBiography);
        dest.writeParcelable(mPicture, flags);
    }
}
