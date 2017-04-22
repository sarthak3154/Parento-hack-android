package com.stelle.stelleapp.getstarted.presenters;

import android.content.Context;
import android.os.Bundle;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.google.gson.JsonParser;
import com.stelle.stelleapp.R;
import com.stelle.stelleapp.dbmodels.UserPreferencesData;
import com.stelle.stelleapp.dbmodels.UserProfileData;
import com.stelle.stelleapp.getstarted.interfaces.SplashScreenContract;
import com.stelle.stelleapp.getstarted.interfaces.SplashWebServiceInterface;
import com.stelle.stelleapp.getstarted.parsers.FacebookLoginRequestAuthHashParser;
import com.stelle.stelleapp.getstarted.parsers.FacebookLoginRequestCredentialsParser;
import com.stelle.stelleapp.getstarted.parsers.FacebookLoginRequestDataParser;
import com.stelle.stelleapp.getstarted.parsers.FacebookLoginRequestParser;
import com.stelle.stelleapp.getstarted.parsers.FacebookLoginResponseParser;
import com.stelle.stelleapp.utils.AppConstants;
import com.stelle.stelleapp.utils.Utils;

import org.json.JSONException;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Sarthak on 14-04-2017
 */

public class SplashScreenPresenter implements SplashScreenContract.Presenter {

    @Inject
    Context context;

    @Inject
    SplashWebServiceInterface serviceInterface;

    private final String EXTRA_FB_FIELDS = "fields";
    private final String EXTRA_FB_FIELDS_VALUE = "name,email,gender,picture";
    private SplashScreenContract.View splashView;

    public SplashScreenPresenter(SplashScreenContract.View view) {
        this.splashView = view;
    }

    @Override
    public void onFacebookLoginApiSuccess(final LoginResult loginResult) {
        // Calling Graph Api
        final GraphRequest request = new GraphRequest(
                loginResult.getAccessToken().getCurrentAccessToken(),
                loginResult.getAccessToken().getCurrentAccessToken().getUserId(),
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        if (response.getError() == null) {
                            Timber.i("onGraphApi Success");
                            onGraphApiSuccess(loginResult, response);
                        } else {
                            onGraphApiFailure(response.getError().getErrorMessage());
                        }
                    }
                }
        );
        Timber.i("Graph version" + request.getVersion());
        Bundle requestParameters = new Bundle();
        requestParameters.putString(EXTRA_FB_FIELDS, EXTRA_FB_FIELDS_VALUE);
        request.setParameters(requestParameters);

        if (Utils.isNetworkAvailable(context)) {
            Timber.i("Graph api called");
            splashView.showProgressBar(true);
            request.executeAsync();
        } else {
            splashView.showFacebookErrorMessage(context.getString(R.string.string_error_no_network));
        }
    }

    @Override
    public void onFacebookLoginApiFailure(String errorMessage) {

    }

    @Override
    public void onGraphApiSuccess(LoginResult loginResult, GraphResponse response) {
        String responseString = response.getRawResponse();
        Timber.i("Graph Api Response: " + responseString);
        if (getFacebookLoginParser(loginResult, response) != null) {
            callCreateProfileApi(getFacebookLoginParser(loginResult, response), loginResult, response);
        } else {
            Timber.e("Unable to parse Graph Api JSON data");
            splashView.showFacebookErrorMessage(context.getString(R.string.string_error_graph_api_failed));
        }
    }

    @Override
    public void onGraphApiFailure(String errorMessage) {
        Timber.e(errorMessage);
        splashView.showFacebookErrorMessage(context.getString(R.string.string_error_graph_api_failed));
    }

    @Override
    public void callCreateProfileApi(FacebookLoginRequestParser requestParser, LoginResult loginResult, GraphResponse graphResponse) {
        Timber.i("Call create profile api");
        if (Utils.isNetworkAvailable(context)) {
            serviceInterface.facebookSignIn(requestParser).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(new Func1<Throwable, Observable<? extends FacebookLoginResponseParser>>() {
                        @Override
                        public Observable<? extends FacebookLoginResponseParser> call(Throwable throwable) {
                            return null;
                        }
                    }).subscribe(new Subscriber<FacebookLoginResponseParser>() {
                @Override
                public void onCompleted() {
                    Timber.i("Completed create profile api call");
                }

                @Override
                public void onError(Throwable e) {
                    Timber.e(e.getMessage());
                    onCreateProfileApiFailure(e.getMessage());
                }

                @Override
                public void onNext(FacebookLoginResponseParser facebookLoginResponseParser) {
                    if (facebookLoginResponseParser.getState().equals(AppConstants.API_RESPONSE_SUCCESS)) {
                        onCreateProfileApiSuccess(facebookLoginResponseParser);
                    } else {
                        onCreateProfileApiFailure(facebookLoginResponseParser.getMsg());
                    }
                }
            });
        }
    }

    @Override
    public void onCreateProfileApiSuccess(FacebookLoginResponseParser responseParser) {
        Timber.i("createProfileApiSuccess");
        UserProfileData.saveUserData(responseParser);
        splashView.showProgressBar(false);
        setUserLoggedinStatus(true);
        splashView.showHomeScreen();
    }

    private void setUserLoggedinStatus(boolean isLoggedIn) {
        UserPreferencesData preferencesData = UserPreferencesData.getUserPreferencesData();
        preferencesData.setUserLoggedIn(isLoggedIn);
        Timber.i("User Logged in: " + isLoggedIn);
    }

    @Override
    public void onCreateProfileApiFailure(String errorMessage) {
        splashView.showFacebookErrorMessage(context.getString(R.string.string_error_something_went_wrong));
    }

    private FacebookLoginRequestParser getFacebookLoginParser(LoginResult loginResult,
                                                              GraphResponse graphResponse) {

        FacebookLoginRequestCredentialsParser facebookRequestCredentialsParser = new FacebookLoginRequestCredentialsParser(
                loginResult.getAccessToken().getCurrentAccessToken().getToken(),
                loginResult.getAccessToken().getCurrentAccessToken().getExpires().getTime()
        );

        FacebookLoginRequestAuthHashParser facebookAuthHashParser;

        facebookAuthHashParser = new FacebookLoginRequestAuthHashParser(
                loginResult.getAccessToken().getUserId(), facebookRequestCredentialsParser, (new JsonParser()).parse(graphResponse.getRawResponse()).getAsJsonObject()
        );

        FacebookLoginRequestDataParser data = new FacebookLoginRequestDataParser(facebookAuthHashParser);
        FacebookLoginRequestParser request = new FacebookLoginRequestParser(data);
        double lat = UserPreferencesData.getUserPreferencesData().getLastKnownLatitude();
        double lon = UserPreferencesData.getUserPreferencesData().getLastKnownLongitude();
        if (lat != 0 && lon != 0) {
            request.setLat(lat);
            request.setLon(lon);
        }
        return request;
    }
}
