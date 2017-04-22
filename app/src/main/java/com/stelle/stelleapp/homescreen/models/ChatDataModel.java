package com.stelle.stelleapp.homescreen.models;

import java.sql.Timestamp;
import java.util.Map;

/**
 * Created by Sarthak on 17-04-2017
 */

public class ChatDataModel {

    private String name;
    private String uid;
    private Long postedAt;
    private String photoUrl;
    private String text;

    public ChatDataModel() {

    }

    public ChatDataModel(String name, String uid, Long postedAt, String photoUrl, String text) {
        this.name = name;
        this.uid = uid;
        this.postedAt = postedAt;
        this.photoUrl = photoUrl;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(Long postedAt) {
        this.postedAt = postedAt;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
