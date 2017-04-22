package com.stelle.stelleapp.getstarted.presenters;

import android.content.Context;

import com.stelle.stelleapp.R;
import com.stelle.stelleapp.dbmodels.UserProfileData;
import com.stelle.stelleapp.getstarted.interfaces.CompleteProfileContract;
import com.stelle.stelleapp.getstarted.interfaces.SplashWebServiceInterface;
import com.stelle.stelleapp.getstarted.parsers.CompleteProfileRequestParser;
import com.stelle.stelleapp.getstarted.parsers.CompleteProfileResponseParser;
import com.stelle.stelleapp.getstarted.parsers.FacebookLoginResponseParser;
import com.stelle.stelleapp.utils.AppConstants;
import com.stelle.stelleapp.utils.Utils;

import javax.inject.Inject;

import okhttp3.internal.Util;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Sarthak on 22-04-2017
 */

public class CompleteProfilePresenter implements CompleteProfileContract.Presenter {

    @Inject
    Context context;

    @Inject
    SplashWebServiceInterface webServiceInterface;

    private CompleteProfileContract.View view;

    public CompleteProfilePresenter() {

    }

    public CompleteProfilePresenter(CompleteProfileContract.View view) {
        this.view = view;
    }


    @Override
    public void callUpdateProfileApi(CompleteProfileRequestParser requestParser) {
        if (Utils.isNetworkAvailable(context)) {
            webServiceInterface.updateProfile(UserProfileData.getUserData().getEmail(), requestParser).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(new Func1<Throwable, Observable<? extends CompleteProfileResponseParser>>() {
                        @Override
                        public Observable<? extends CompleteProfileResponseParser> call(Throwable throwable) {
                            return null;
                        }
                    }).subscribe(new Subscriber<CompleteProfileResponseParser>() {
                @Override
                public void onCompleted() {
                    Timber.i("Completed create profile api call");
                }

                @Override
                public void onError(Throwable e) {
                    Timber.e(e.getMessage());
                    onUpdateProfileApiSuccess();
                }

                @Override
                public void onNext(CompleteProfileResponseParser responseParser) {
                    if (responseParser.getState().equals(AppConstants.API_RESPONSE_SUCCESS)) {
                        onUpdateProfileApiSuccess();
                    } else {
                        Timber.e("Update Profile Not Success!");
                    }
                }
            });

        } else {
            Utils.showToast(context, context.getString(R.string.string_error_no_network));
        }
    }

    @Override
    public void onUpdateProfileApiSuccess() {
        view.moveToNextScreen();
    }
}
