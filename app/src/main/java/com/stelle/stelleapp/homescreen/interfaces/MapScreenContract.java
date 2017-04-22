package com.stelle.stelleapp.homescreen.interfaces;

import com.stelle.stelleapp.homescreen.models.FeaturedUsersModel;
import com.stelle.stelleapp.homescreen.parsers.UsersMapResponseParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sarthak on 22-04-2017
 */

public interface MapScreenContract {

    interface View {
        void init();

        void updateUsersMapMarkers(List<UsersMapResponseParser> responseParserList);
    }

    interface Presenter {
        void callGetNearbyUsersApi();

        ArrayList<FeaturedUsersModel> getFeaturesUsersModel(List<UsersMapResponseParser> responseParserList);

    }
}
