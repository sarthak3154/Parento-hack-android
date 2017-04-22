package com.stelle.stelleapp.getstarted.parsers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sarthak on 22-04-2017
 */

public class CompleteProfileResponseParser {

    @SerializedName("state")
    @Expose
    private Integer state;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
