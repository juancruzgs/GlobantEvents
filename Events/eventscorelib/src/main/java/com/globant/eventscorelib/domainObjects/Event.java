package com.globant.eventscorelib.domainObjects;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event extends BaseObject implements Parcelable {

    private String mObjectID;
    private String mTitle;
    private String mShortDescription;
    private String mFullDescription;
    private String mAdditionalInfo;
    private String mAddress;
    private String mCity;
    private String mCountry;
    private String mCategory;
    private String mLanguage;
    private String mHashtag;
    private Date mStartDate;
    private Date mEndDate;
    private boolean mPublic;
    private Bitmap mIcon;
    private Bitmap mEventLogo;

    private LatLng mCoordinates;
    private List<Speaker> mSpeakers;
    private List<Subscriber> mSubscribers;

    public Event() {
    }

    public Event(String title) {
        mTitle = title;
    }

    public LatLng getCoordinates() {
        return mCoordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        mCoordinates = coordinates;
    }

    public String getObjectID() {
        return mObjectID;
    }

    public void setObjectID(String objectID) {
        mObjectID = objectID;
    }

    public String getHashtag() {
        return mHashtag;
    }

    public void setHashtag(String hashtag) {
        mHashtag = hashtag;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String language) {
        mLanguage = language;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        mShortDescription = shortDescription;
    }

    public String getFullDescription() {
        return mFullDescription;
    }

    public void setFullDescription(String fullDescription) {
        mFullDescription = fullDescription;
    }

    public String getAdditionalInfo() {
        return mAdditionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        mAdditionalInfo = additionalInfo;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date endDate) {
        mEndDate = endDate;
    }

    public boolean isPublic() {
        return mPublic;
    }

    public void setPublic(boolean aPublic) {
        mPublic = aPublic;
    }

    public Bitmap getIcon() {
        return mIcon;
    }

    public void setIcon(Bitmap icon) {
        mIcon = icon;
    }

    public Bitmap getEventLogo() {
        return mEventLogo;
    }

    public void setEventLogo(Bitmap eventLogo) {
        mEventLogo = eventLogo;
    }

    public List<Speaker> getSpeakers() {
        return mSpeakers;
    }

    public void setSpeakers(List<Speaker> speakers) {
        mSpeakers = speakers;
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

    public List<Subscriber> getSubscribers() {
        return mSubscribers;
    }

    public void setSubscribers(List<Subscriber> subscribers) {
        mSubscribers = subscribers;
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private Event(Parcel in) {
        mObjectID = in.readString();
        mTitle = in.readString();
        mShortDescription = in.readString();
        mFullDescription = in.readString();
        mAdditionalInfo = in.readString();
        mCoordinates = in.readParcelable(LatLng.class.getClassLoader());
        mAddress = in.readString();
        mCity = in.readString();
        mCountry = in.readString();
        mCategory = in.readString();
        mLanguage = in.readString();
        mHashtag = in.readString();
        mEventLogo = in.readParcelable(null);
        mIcon = in.readParcelable(null);
        mPublic = (in.readInt() == 1);
        mStartDate = (Date) in.readSerializable();
        mEndDate = (Date) in.readSerializable();
//        mSpeakers = new ArrayList<>();
//        in.readTypedList(mSpeakers, Speaker.CREATOR);
//        mSubscribers = new ArrayList<>();
//        in.readTypedList(mSubscribers, Subscriber.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mObjectID);
        dest.writeString(mTitle);
        dest.writeString(mShortDescription);
        dest.writeString(mFullDescription);
        dest.writeString(mAdditionalInfo);
        dest.writeParcelable(mCoordinates, 1);
        dest.writeString(mAddress);
        dest.writeString(mCity);
        dest.writeString(mCountry);
        dest.writeString(mCategory);
        dest.writeString(mLanguage);
        dest.writeString(mHashtag);
        dest.writeParcelable(mEventLogo, flags);
        dest.writeParcelable(mIcon, flags);
        dest.writeInt(mPublic ? 1 : 0);
        dest.writeSerializable(mStartDate);
        dest.writeSerializable(mEndDate);
//        dest.writeTypedList(mSpeakers);
//        dest.writeTypedList(mSubscribers);
    }
}
