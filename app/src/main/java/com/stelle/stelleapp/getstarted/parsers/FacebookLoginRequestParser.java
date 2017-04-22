package com.stelle.stelleapp.getstarted.parsers;

import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;

/**
 * Created by Sarthak on 22-04-2017
 */

public class FacebookLoginRequestParser {
    @SerializedName("data")
    @Expose
    public FacebookLoginRequestDataParser data;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lon")
    @Expose
    private double lon;

    public FacebookLoginRequestParser(FacebookLoginRequestDataParser data) {
        this.data = data;
    }

    public FacebookLoginRequestDataParser getData() {
        return data;
    }

    public void setData(FacebookLoginRequestDataParser data) {
        this.data = data;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
