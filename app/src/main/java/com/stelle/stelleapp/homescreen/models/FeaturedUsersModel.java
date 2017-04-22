package com.stelle.stelleapp.homescreen.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.stelle.stelleapp.homescreen.parsers.UsersMapResponseParser;

/**
 * Created by Sarthak on 14-04-2017
 */

public class FeaturedUsersModel implements Parcelable {
    private String id;
    private String name;
    private String email;
    private String gender;
    private Double latitude;
    private Double longitude;
    private String baseUrl;
    private String imagePublicId;
    private String baseUrlPostFix;

    protected FeaturedUsersModel(Parcel in) {
        this.id = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.baseUrl = in.readString();
        this.baseUrlPostFix = in.readString();
        this.imagePublicId = in.readString();
        this.name = in.readString();
        this.email = in.readString();
        this.gender = in.readString();
    }

    public FeaturedUsersModel(UsersMapResponseParser responseParser) {
        id = responseParser.getId();
        longitude = responseParser.getLoc().get(0);
        latitude = responseParser.getLoc().get(1);
        baseUrl = responseParser.getPublic_host();
        baseUrlPostFix = responseParser.getVersion();
        imagePublicId = responseParser.getPublic_id();
        name = responseParser.getName();
        email = responseParser.getEmail();
        gender = responseParser.getGender();
    }

    public static final Creator<FeaturedUsersModel> CREATOR = new Creator<FeaturedUsersModel>() {
        @Override
        public FeaturedUsersModel createFromParcel(Parcel in) {
            return new FeaturedUsersModel(in);
        }

        @Override
        public FeaturedUsersModel[] newArray(int size) {
            return new FeaturedUsersModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeDouble(this.latitude);
        parcel.writeDouble(this.longitude);
        parcel.writeString(this.baseUrl);
        parcel.writeString(this.baseUrlPostFix);
        parcel.writeString(this.imagePublicId);
        parcel.writeString(this.name);
        parcel.writeString(this.email);
        parcel.writeString(this.gender);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getImagePublicId() {
        return imagePublicId;
    }

    public void setImagePublicId(String imagePublicId) {
        this.imagePublicId = imagePublicId;
    }

    public String getBaseUrlPostFix() {
        return baseUrlPostFix;
    }

    public void setBaseUrlPostFix(String baseUrlPostFix) {
        this.baseUrlPostFix = baseUrlPostFix;
    }
}
