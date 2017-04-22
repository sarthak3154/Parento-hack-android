package com.stelle.stelleapp.homescreen.presenters;

import android.content.Context;

import com.stelle.stelleapp.R;
import com.stelle.stelleapp.dbmodels.UserPreferencesData;
import com.stelle.stelleapp.dbmodels.UserProfileData;
import com.stelle.stelleapp.homescreen.interfaces.HomeScreenWebServiceInterface;
import com.stelle.stelleapp.homescreen.interfaces.MapScreenContract;
import com.stelle.stelleapp.homescreen.models.FeaturedUsersModel;
import com.stelle.stelleapp.homescreen.parsers.UsersMapResponseParser;
import com.stelle.stelleapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Sarthak on 22-04-2017
 */

public class MapScreenPresenter implements MapScreenContract.Presenter {
    @Inject
    Context context;

    @Inject
    HomeScreenWebServiceInterface webServiceInterface;

    private MapScreenContract.View mapView;

    public MapScreenPresenter(MapScreenContract.View view) {
        this.mapView = view;
    }

    @Override
    public void callGetNearbyUsersApi() {
        if (Utils.isNetworkAvailable(context)) {
            UserProfileData userProfileData = UserProfileData.getUserData();
            if (userProfileData != null) {
                webServiceInterface.getNearbyUsers(userProfileData.getEmail(), UserPreferencesData.getUserPreferencesData().getLastKnownLongitude(),
                        UserPreferencesData.getUserPreferencesData().getLastKnownLatitude()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<UsersMapResponseParser>>() {
                            @Override
                            public void onCompleted() {
                                Timber.i("Call Nearby Users Api Success");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Timber.e(e.getMessage());
                                if (e.getMessage().equals("Index: 0, Size: 0")) {
                                    Timber.e("No Users");
                                } else {
                                    Utils.showToast(context, context.getString(R.string.string_error_something_went_wrong));
                                }
                            }

                            @Override
                            public void onNext(List<UsersMapResponseParser> responseParserList) {
                                if (responseParserList.size() == 0) {
                                    Timber.i("No users found Nearby");
                                } else {
                                    mapView.updateUsersMapMarkers(responseParserList);
                                }
                            }
                        });
            }
        }

    }

    @Override
    public ArrayList<FeaturedUsersModel> getFeaturesUsersModel(List<UsersMapResponseParser> responseParserList) {
        ArrayList<FeaturedUsersModel> list = new ArrayList<>();
        if (responseParserList != null && responseParserList.size() != 0) {
            for (int i = 0; i < responseParserList.size(); i++) {
                FeaturedUsersModel gripesModel = new FeaturedUsersModel(responseParserList.get(i));
                list.add(gripesModel);
            }
        }
        return list;
    }
}
