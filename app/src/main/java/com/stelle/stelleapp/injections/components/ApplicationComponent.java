package com.stelle.stelleapp.injections.components;

import android.content.Context;

import com.stelle.stelleapp.BaseApplication;
import com.stelle.stelleapp.injections.modules.ApiModule;
import com.stelle.stelleapp.injections.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    Context getApplicationContext();

    ApiComponent plusApiComponent(ApiModule apiModule);

    void inject(BaseApplication application);
}
