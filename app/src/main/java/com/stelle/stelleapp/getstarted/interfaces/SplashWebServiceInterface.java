package com.stelle.stelleapp.getstarted.interfaces;

import com.stelle.stelleapp.getstarted.parsers.FacebookLoginRequestParser;
import com.stelle.stelleapp.getstarted.parsers.FacebookLoginResponseParser;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Sarthak on 22-04-2017
 */

public interface SplashWebServiceInterface {

    @POST("login/facebook")
    Observable<FacebookLoginResponseParser> facebookSignIn(@Body FacebookLoginRequestParser facebookLoginRequestParser);
}
