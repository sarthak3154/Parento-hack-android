package com.stelle.stelleapp.getstarted.interfaces;

/**
 * Created by Sarthak on 22-04-2017
 */
public interface UserLocationListener {

    void requestLocation();

    boolean isLocationPermissionGranted();

    void checkLocationSettings();

    void startLocationUpdates();

    void stopLocationUpdates();

    void onLocationSuccess(double latitude, double longitude);

    void onLocationFailed(String message);

}
