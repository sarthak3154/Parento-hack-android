package com.stelle.stelleapp;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.stelle.stelleapp.injections.components.ApplicationComponent;
import com.stelle.stelleapp.injections.components.DaggerApplicationComponent;
import com.stelle.stelleapp.injections.modules.ApplicationModule;

import timber.log.Timber;




public class BaseApplication extends com.activeandroid.app.Application {

    private int resumeCount;
    private int pauseCount;
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeApplicationComponent();
        initializeTimber();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initializeTimber() {
        Timber.plant(new Timber.DebugTree());
    }

    private void initializeApplicationComponent() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }


    public void updateResumeCount() {
        this.resumeCount++;
    }

    public void updatePauseCount() {
        this.pauseCount++;
    }

    public int getResumeCount() {
        return resumeCount;
    }

    public int getPauseCount() {
        return pauseCount;
    }
}
