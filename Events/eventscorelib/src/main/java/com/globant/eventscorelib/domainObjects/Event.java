package com.globant.eventscorelib.domainObjects;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

public class Event  extends BaseObject{

    private String mObjectID;
    private String mTitle;
    private String mShortDescription;
    private String mFullDescription;
    private String mAdditionalInfo;
    private String mAddress;
    private String mQrCode;
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

    public String getQrCode() {
        return mQrCode;
    }

    public void setQrCode(String qrCode) {
        mQrCode = qrCode;
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
}
