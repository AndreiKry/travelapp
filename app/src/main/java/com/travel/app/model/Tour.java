package com.travel.app.model;

import java.util.UUID;

public class Tour {

    private UUID mId;
    private String mThumbnailUrl;
    private String mTitle;
    private String mHotel;
    private String mCountry;
    private String mDescription;
    private String mFrom;
    private String mDate;
    private String mPrice;
    private String mStar;
    private String mDay;
    private String mPeople;
    private String mMeal;
    private String mPhone;

    public Tour() {
        mId = UUID.randomUUID();
    }

    public UUID getId() {
        return mId;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        mThumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title.replaceAll("<.*?>","");
    }

    public String getHotel() {
        return mHotel;
    }

    public void setHotel(String hotel) {
        mHotel = hotel;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description.replaceAll("<.*?>","");
    }

    public String getFrom() {
        return mFrom;
    }

    public void setFrom(String from) {
        mFrom = from;
    }

    public String getDate() {

        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getStar() {
        return mStar;
    }

    public void setStar(String star) {
        mStar = star;
    }

    public String getDay() {
        return mDay;
    }

    public void setDay(String day) {
        mDay = day;
    }

    public String getPeople() {
        return mPeople;
    }

    public void setPeople(String people) {
        mPeople = people;
    }

    public String getMeal() {
        return mMeal;
    }

    public void setMeal(String meal) {
        mMeal = meal;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }
}
