package com.stelle.stelleapp.homescreen.interfaces;

import com.stelle.stelleapp.homescreen.parsers.UsersMapResponseParser;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Sarthak on 22-04-2017
 */

public interface HomeScreenWebServiceInterface {

    String PARAM_PATH_EMAIL = "email";
    String PARAM_PATH_FROM_EMAIL = "fromEmail";
    String PARAM_PATH_TO_EMAIL = "toEmail";
    String PARAM_PATH_LATITUDE = "lat";
    String PARAM_PATH_LONGITUDE = "lon";
    String QUERY_LATITUDE = "lat";
    String QUERY_LONGITUDE = "lon";
    String PARAM_PATH_STATE = "state";

    @GET("api/{" + PARAM_PATH_EMAIL + "}/getNearbyUsers")
    Observable<List<UsersMapResponseParser>> getNearbyUsers(@Path(PARAM_PATH_EMAIL) String email, @Query(QUERY_LONGITUDE) double lon,
                                                            @Query(QUERY_LATITUDE) double lat);


}
