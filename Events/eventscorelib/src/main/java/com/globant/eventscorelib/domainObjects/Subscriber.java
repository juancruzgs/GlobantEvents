package com.globant.eventscorelib.domainObjects;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Subscriber extends BaseObject implements Parcelable {

    private String mObjectID;
    private String mName;
    private String mLastName;
    private String mEmail;
    private String mPhone;
    private String mOccupation;
    private String mTwitterUser;
    private String mCity;
    private String mCountry;
    private Bitmap mPicture;
    private boolean mEnglish;
    private boolean mGlober;
    private boolean mPublic;
    private boolean mAccepted;
    private boolean mCheckIn;
    private boolean mChecked;

    public Subscriber() {
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

    public Bitmap getPicture() {
        return mPicture;
    }

    public void setPicture(Bitmap picture) {
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

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean mChecked) {
        this.mChecked = mChecked;
    }

    private Subscriber(Parcel in) {
        mObjectID = in.readString();
        mName = in.readString();
        mLastName = in.readString();
        mEmail = in.readString();
        mPhone = in.readString();
        mOccupation = in.readString();
        mTwitterUser = in.readString();
        mCity = in.readString();
        mCountry = in.readString();
        mPicture = in.readParcelable(null);
        mEnglish = (in.readInt() == 1);
        mGlober = (in.readInt() == 1);
        mPublic = (in.readInt() == 1);
        mAccepted = (in.readInt() == 1);
        mCheckIn = (in.readInt() == 1);
        mChecked = (in.readInt() == 1);
    }

   public static final Parcelable.Creator<Subscriber> CREATOR = new Parcelable.Creator<Subscriber>() {
        @Override
        public Subscriber createFromParcel(Parcel source) {
            return new Subscriber(source);
        }

        @Override
        public Subscriber[] newArray(int size) {
            return new Subscriber[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mObjectID);
        dest.writeString(mName);
        dest.writeString(mLastName);
        dest.writeString(mEmail);
        dest.writeString(mPhone);
        dest.writeString(mOccupation);
        dest.writeString(mTwitterUser);
        dest.writeString(mCity);
        dest.writeString(mCountry);
        dest.writeParcelable(mPicture, flags);
        dest.writeInt(mEnglish ? 1 : 0);
        dest.writeInt(mGlober ? 1 : 0);
        dest.writeInt(mPublic ? 1 : 0);
        dest.writeInt(mAccepted ? 1 : 0);
        dest.writeInt(mCheckIn ? 1 : 0);
        dest.writeInt(mChecked ? 1 : 0);
    }
}


