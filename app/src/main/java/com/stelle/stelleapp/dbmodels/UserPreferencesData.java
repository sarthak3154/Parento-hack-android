package com.stelle.stelleapp.dbmodels;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Sarthak on 22-04-2017
 */

@Table(name = "user_preferences_data")
public class UserPreferencesData extends Model {

    @Column(name = "is_user_logged_in")
    private boolean isUserLoggedIn;

    @Column(name = "last_known_latitude")
    private double lastKnownLatitude;

    @Column(name = "last_known_longitude")
    private double lastKnownLongitude;

    public UserPreferencesData() {
        super();
    }

    public static UserPreferencesData getUserPreferencesData() {

        List<UserPreferencesData> userPreferencesData = new Select().from(UserPreferencesData.class).execute();
        if (userPreferencesData != null && !userPreferencesData.isEmpty()) {

            return userPreferencesData.get(0);
        } else {
            return new UserPreferencesData();
        }
    }

    public boolean isUserLoggedIn() {
        return isUserLoggedIn;
    }

    public void setUserLoggedIn(boolean userLoggedIn) {
        isUserLoggedIn = userLoggedIn;
        this.save();
    }

    public double getLastKnownLatitude() {
        return lastKnownLatitude;
    }

    public void setLastKnownLatitude(double lastKnownLatitude) {
        this.lastKnownLatitude = lastKnownLatitude;
        this.save();
    }

    public double getLastKnownLongitude() {
        return lastKnownLongitude;
    }

    public void setLastKnownLongitude(double lastKnownLongitude) {
        this.lastKnownLongitude = lastKnownLongitude;
        this.save();
    }
}
