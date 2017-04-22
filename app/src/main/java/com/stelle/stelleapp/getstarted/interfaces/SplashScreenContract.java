package com.stelle.stelleapp.getstarted.interfaces;

import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.stelle.stelleapp.getstarted.parsers.FacebookLoginRequestParser;
import com.stelle.stelleapp.getstarted.parsers.FacebookLoginResponseParser;

import org.json.JSONException;

/**
 * Created by Sarthak on 22-04-2017
 */

public interface SplashScreenContract {

    interface View {

        void init();

        void goToFacebookLogin();

        void goToNextScreen();

        void showProgressBar(boolean show);

        void showFacebookErrorMessage(String errorMessage);

        void showHomeScreen();

        boolean isViewDestroyed();
    }

    interface Presenter {
        void onFacebookLoginApiSuccess(LoginResult loginResult);

        void onFacebookLoginApiFailure(String errorMessage);

        void onGraphApiSuccess(LoginResult loginResult, GraphResponse response) throws JSONException;

        void onGraphApiFailure(String errorMessage);

        void callCreateProfileApi(FacebookLoginRequestParser requestParser, LoginResult loginResult, GraphResponse graphResponse);

        void onCreateProfileApiSuccess(FacebookLoginResponseParser responseParser);

        void onCreateProfileApiFailure(String errorMessage);
    }
}
