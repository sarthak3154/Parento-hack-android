package com.stelle.stelleapp.getstarted.interfaces;

import com.stelle.stelleapp.getstarted.parsers.CompleteProfileRequestParser;
import com.stelle.stelleapp.getstarted.parsers.CompleteProfileResponseParser;
import com.stelle.stelleapp.getstarted.parsers.FacebookLoginRequestParser;
import com.stelle.stelleapp.getstarted.parsers.FacebookLoginResponseParser;

import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Sarthak on 22-04-2017
 */

public interface SplashWebServiceInterface {

    @POST("login/facebook")
    Observable<FacebookLoginResponseParser> facebookSignIn(@Body FacebookLoginRequestParser facebookLoginRequestParser);

    @PATCH("login/{email}/updateProfile")
    Observable<CompleteProfileResponseParser> updateProfile(@Path("email") String email, @Body CompleteProfileRequestParser requestParser);
}
