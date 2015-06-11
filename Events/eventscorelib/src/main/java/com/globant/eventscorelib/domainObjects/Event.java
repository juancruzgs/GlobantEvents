package com.globant.eventscorelib.domainObjects;

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
    private byte[] mIcon;
    private byte[] mEventLogo;
    private Date mUpdatedAt;

    private LatLng mCoordinates;
    private List<Speaker> mSpeakers;
    private List<Subscriber> mSubscribers;

    //private Long mCalendarID;

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

    public byte[] getIcon() {
        return mIcon;
    }

    public void setIcon(byte[] icon) {
        mIcon = icon;
    }

    public byte[] getEventLogo() {
        return mEventLogo;
    }

    public void setEventLogo(byte[] eventLogo) {
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

    public Date getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public List<Subscriber> getSubscribers() {
        return mSubscribers;
    }

    public void setSubscribers(List<Subscriber> subscribers) {
        mSubscribers = subscribers;
    }

/*
    public Long getCalendarID() {
        return mCalendarID;
    }

    public void setCalendarID(Long calendarID) {
        mCalendarID = calendarID;
    }
*/

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
        int sizeEventLogo = in.readInt();
        if (sizeEventLogo != 0) {
            mEventLogo = new byte[sizeEventLogo];
            in.readByteArray(mEventLogo);
        }
        int sizeIcon = in.readInt();
        if (sizeIcon != 0) {
            mIcon = new byte[sizeIcon];
            in.readByteArray(mIcon);
        }
        mPublic = (in.readInt() == 1);
        mStartDate = (Date) in.readSerializable();
        mEndDate = (Date) in.readSerializable();
        mSpeakers = new ArrayList<>();
        in.readTypedList(mSpeakers, Speaker.CREATOR);
        mSubscribers = new ArrayList<>();
        in.readTypedList(mSubscribers, Subscriber.CREATOR);
        //mCalendarID = (Long)in.readValue(Long.class.getClassLoader());
        mUpdatedAt = (Date) in.readSerializable();
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
        if (mEventLogo != null) {
            dest.writeInt(mEventLogo.length);
            dest.writeByteArray(mEventLogo);
        } else {
            dest.writeInt(0);
        }
        if (mIcon != null) {
            dest.writeInt(mIcon.length);
            dest.writeByteArray(mIcon);
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(mPublic ? 1 : 0);
        dest.writeSerializable(mStartDate);
        dest.writeSerializable(mEndDate);
        dest.writeTypedList(mSpeakers);
        dest.writeTypedList(mSubscribers);
        //dest.writeValue(mCalendarID);
        dest.writeSerializable(mUpdatedAt);
    }
}
