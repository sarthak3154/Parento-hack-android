package com.stelle.stelleapp.dbmodels;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.stelle.stelleapp.getstarted.parsers.FacebookLoginResponseParser;

import java.util.List;

/**
 * Created by Sarthak on 22-04-2017
 */
@Table(name = "user_profile_data")
public class UserProfileData extends Model {

    @Column(name = "uId")
    private String uId;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "gender")
    private String gender;
    @Column(name = "imageUrl")
    private String imageUrl;

    public UserProfileData() {

    }

    public UserProfileData(String uId, String name, String email, String gender, String imageUrl) {
        this.uId = uId;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.imageUrl = imageUrl;
    }

    public static void saveUserData(FacebookLoginResponseParser responseParser) {
        if (responseParser != null) {
            deleteUserData();
            UserProfileData userProfileData = new UserProfileData(responseParser.getuId(), responseParser.getName(), responseParser.getEmail(),
                    responseParser.getGender(), responseParser.getImageUrl());
            userProfileData.save();
        }
    }

    public static UserProfileData getUserData() {
        List<UserProfileData> userData = new Select().from(UserProfileData.class).execute();
        if (userData != null && !userData.isEmpty()) {
            return userData.get(0);
        }
        return null;
    }

    public static void deleteUserData() {
        new Delete().from(UserProfileData.class).execute();
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
        this.save();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.save();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.save();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        this.save();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        this.save();
    }
}
