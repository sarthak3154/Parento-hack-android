package com.stelle.stelleapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.stelle.stelleapp.injections.components.ApiComponent;
import com.stelle.stelleapp.injections.modules.ApiModule;


public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ApiComponent getApiComponent() {
        return ((BaseApplication) getApplication()).getApplicationComponent().plusApiComponent(new ApiModule());
    }


    @Override
    protected void onResume() {
        super.onResume();
        ((BaseApplication) getApplication()).updateResumeCount();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((BaseApplication) getApplication()).updatePauseCount();
    }
}

