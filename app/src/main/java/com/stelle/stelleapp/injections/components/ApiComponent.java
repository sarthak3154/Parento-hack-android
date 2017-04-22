package com.stelle.stelleapp.injections.components;


import com.stelle.stelleapp.getstarted.presenters.CompleteProfilePresenter;
import com.stelle.stelleapp.getstarted.presenters.SplashScreenPresenter;
import com.stelle.stelleapp.homescreen.presenters.MapScreenPresenter;
import com.stelle.stelleapp.injections.modules.ApiModule;

import javax.inject.Singleton;

import dagger.Subcomponent;

@Singleton
@Subcomponent(modules = {ApiModule.class})
public interface ApiComponent {

    void inject(SplashScreenPresenter splashScreenPresenter);

    void inject(MapScreenPresenter mapScreenPresenter);

    void inject(CompleteProfilePresenter profilePresenter);

}
