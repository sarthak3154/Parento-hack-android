package com.stelle.stelleapp.homescreen.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.maps.android.ui.IconGenerator;
import com.squareup.picasso.Picasso;
import com.stelle.stelleapp.BaseActivity;
import com.stelle.stelleapp.R;
import com.stelle.stelleapp.dbmodels.UserPreferencesData;
import com.stelle.stelleapp.dbmodels.UserProfileData;
import com.stelle.stelleapp.homescreen.interfaces.MapScreenContract;
import com.stelle.stelleapp.homescreen.models.FeaturedUsersModel;
import com.stelle.stelleapp.homescreen.parsers.UsersMapResponseParser;
import com.stelle.stelleapp.homescreen.presenters.MapScreenPresenter;
import com.stelle.stelleapp.utils.Utils;
import com.stelle.stelleapp.widgets.AppTextView;
import com.stelle.stelleapp.widgets.CircleTransform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by Sarthak on 22-04-2017
 */

public class MapScreenFragment extends Fragment implements MapScreenContract.View, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    @Bind(R.id.mapView)
    MapView mMapView;
    @Bind(R.id.textDispName)
    AppTextView dispName;

    private Boolean isNight;
    private GoogleMap googleMap;
    private ArrayList<FeaturedUsersModel> usersModels;
    private ArrayList<Marker> mapMarkers = new ArrayList<>();
    private MapScreenContract.Presenter mapPresenter;


    public MapScreenFragment() {

    }

    public static MapScreenFragment newInstance() {
        Bundle args = new Bundle();
        MapScreenFragment fragment = new MapScreenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_screen, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void init() {
        mMapView.onResume();
        MapsInitializer.initialize(this.getActivity().getApplicationContext());
        mMapView.getMapAsync(this);

        mapPresenter = new MapScreenPresenter(this);
        ((BaseActivity) getActivity()).getApiComponent().
                inject((MapScreenPresenter) mapPresenter);
        mapPresenter.callGetNearbyUsersApi();
    }


    @Override
    public void updateUsersMapMarkers(List<UsersMapResponseParser> responseParserList) {
        usersModels = mapPresenter.getFeaturesUsersModel(responseParserList);
        for (int i = 0; i < usersModels.size(); i++) {
            //Add markers
            IconGenerator iconGenerator = new IconGenerator(getActivity());
            iconGenerator.setBackground(getResources().getDrawable(R.drawable.drawable_circle_user));
            iconGenerator.setRotation(0);
            LayoutInflater myInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View markerView = myInflater.inflate(R.layout.marker_map, null, false);
            ImageView imageUser = (ImageView) markerView.findViewById(R.id.imageUser);
            AppTextView textView = (AppTextView) markerView.findViewById(R.id.textUserName);
            String firstChar = Character.toString(usersModels.get(i).getName().charAt(0));
            textView.setText(firstChar);
            String cloudinaryImageUrl = "";

            int userIconRadius = Utils.convertDpToPixel(getActivity(),
                    (int) getActivity().getResources().getDimension(R.dimen.dimen_10dp));
            cloudinaryImageUrl = Utils.getCircularCloudinaryImageUrl(
                    usersModels.get(i).getBaseUrl(), usersModels.get(i).getBaseUrlPostFix(),
                    usersModels.get(i).getImagePublicId(), userIconRadius);

            if (!TextUtils.isEmpty(cloudinaryImageUrl)) {
                Timber.i("Url: " + cloudinaryImageUrl);
                Picasso.with(getActivity())
                        .load(cloudinaryImageUrl)
                        .into(imageUser);
                iconGenerator.setContentView(markerView);
            } else {
                imageUser.setImageResource(R.drawable.ic_action_name);
            }

            Marker marker = googleMap.addMarker(new MarkerOptions().
                    icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon())).snippet(String.valueOf(i)).
                    position(new LatLng(usersModels.get(i).getLatitude(), usersModels.get(i).getLongitude())));
            mapMarkers.add(marker);
        }

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Timber.i("OnMarkerClick");
        final String position = marker.getSnippet();
        String name = usersModels.get(Integer.parseInt(position)).getName();
        dispName.setText(name);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Utils.showToast(getActivity().getApplicationContext(), getString(R.string.string_show_location));
            return;
        }
        this.googleMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setMyLocationEnabled(true);
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        isNight = hour < 6 || hour > 18;
        /*
        MapStyleOptions styleOptions = MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_json_day_retro);
        googleMap.setMapStyle(styleOptions);
        */
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setCompassEnabled(false);
        UserProfileData userProfileData = UserProfileData.getUserData();
        if (userProfileData != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(UserPreferencesData.getUserPreferencesData().getLastKnownLatitude(),
                    UserPreferencesData.getUserPreferencesData().getLastKnownLongitude()), 15));
        }

    }

}
