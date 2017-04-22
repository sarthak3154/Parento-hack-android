package com.stelle.stelleapp.getstarted.parsers;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sarthak on 22-04-2017
 */

public class FacebookLoginRequestAuthHashParser {
    @SerializedName("uid")
    @Expose
    public String uid;
    @SerializedName("credentials")
    @Expose
    public FacebookLoginRequestCredentialsParser credentials;
    @SerializedName("info")
    @Expose
    public JsonObject info;

    public FacebookLoginRequestAuthHashParser(String uid, FacebookLoginRequestCredentialsParser credentials, JsonObject info) {
        this.uid = uid;
        this.credentials = credentials;
        this.info = info;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public FacebookLoginRequestCredentialsParser getCredentials() {
        return credentials;
    }

    public void setCredentials(FacebookLoginRequestCredentialsParser credentials) {
        this.credentials = credentials;
    }

    public JsonObject getInfo() {
        return info;
    }

    public void setInfo(JsonObject info) {
        this.info = info;
    }
}
