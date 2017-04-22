package com.stelle.stelleapp.getstarted.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.stelle.stelleapp.R;
import com.stelle.stelleapp.dbmodels.UserPreferencesData;
import com.stelle.stelleapp.getstarted.presenters.SplashScreenPresenter;
import com.stelle.stelleapp.getstarted.interfaces.SplashScreenContract;
import com.stelle.stelleapp.homescreen.activities.HomeScreenActivity;
import com.stelle.stelleapp.utils.AppConstants;
import com.stelle.stelleapp.utils.Utils;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.extra.Scale;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class SplashActivity extends LocationRequestActivity implements SplashScreenContract.View {

    @Bind(R.id.layoutProgressBar)
    protected LinearLayout progressBar;

    private SplashScreenContract.Presenter presenter;
    private CallbackManager callbackManager;
    private ArrayList<String> fbPermissions = new ArrayList<>();
    ViewGroup transitionsContainer;

    @Bind(R.id.linearLayoutFacebook)
    LinearLayout facebookButtonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FacebookSdk.sdkInitialize(getApplicationContext());
        init();
    }

    @Override
    public void onLocationFailed(String message) {
        Timber.e(message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestLocation();
    }

    @Override
    public void onLocationSuccess(double latitude, double longitude) {
        Timber.i("Location Success: (".concat(String.valueOf(latitude)).concat(",").concat(String.valueOf(longitude).concat(")")));
        UserPreferencesData.getUserPreferencesData().setLastKnownLatitude(latitude);
        UserPreferencesData.getUserPreferencesData().setLastKnownLongitude(longitude);
        if (UserPreferencesData.getUserPreferencesData().isUserLoggedIn()) {
            goToNextScreen();
        }
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        transitionsContainer = (ViewGroup) findViewById(R.id.transitions_container);
        callbackManager = CallbackManager.Factory.create();
        presenter = new SplashScreenPresenter(this);
        getApiComponent().inject((SplashScreenPresenter) presenter);
        if (!UserPreferencesData.getUserPreferencesData().isUserLoggedIn()) {
            TransitionSet set = new TransitionSet()
                    .addTransition(new Scale(0.7f))
                    .addTransition(new Fade())
                    .setInterpolator(new LinearOutSlowInInterpolator());

            TransitionManager.beginDelayedTransition(transitionsContainer, set);
            facebookButtonLayout.setVisibility(View.VISIBLE);
        } else {
            facebookButtonLayout.setVisibility(View.INVISIBLE);

        }



    }

    @OnClick(R.id.linearLayoutFacebook)
    public void onClickFacebookLogin() {
        goToFacebookLogin();
    }

    @Override
    public void goToFacebookLogin() {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Timber.i("FB login success: " + loginResult.getAccessToken().getToken());
                        //Call Graph Api
                        presenter.onFacebookLoginApiSuccess(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        Timber.e("FB login cancel");
                        presenter.onFacebookLoginApiFailure(getString(R.string.string_error_facebook_login_cancelled));
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Timber.e("FB login error: " + Log.getStackTraceString(error));
                        presenter.onFacebookLoginApiFailure(getString(R.string.string_error_facebook_login));
                    }
                });

        if (fbPermissions.isEmpty()) {
            fbPermissions.addAll(Arrays.asList(getResources().getStringArray(R.array.string_array_fb_permissions)));
        }
        if (Utils.isNetworkAvailable(this)) {
            showProgressBar(true);
            LoginManager.getInstance().logInWithReadPermissions(this, fbPermissions);
        } else {
            showFacebookErrorMessage(getString(R.string.string_error_no_network));
        }
    }

    @Override
    public void goToNextScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    Intent intent = new Intent(SplashActivity.this, HomeScreenActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, AppConstants.SPLASH_SCREEN_DURATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showProgressBar(boolean show) {
        if (!isViewDestroyed()) {
            if (show) {
                if (progressBar.getVisibility() != View.VISIBLE) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showFacebookErrorMessage(String errorMessage) {
        if (!isViewDestroyed()) {
            showProgressBar(false);
            Utils.showToast(this, errorMessage);
        }
    }

    @Override
    public void showHomeScreen() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (!isViewDestroyed()) {
                    Intent intent = new Intent(SplashActivity.this, HomeScreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                }
            }
        });
    }

    @Override
    public boolean isViewDestroyed() {
        return isFinishing();
    }
}
