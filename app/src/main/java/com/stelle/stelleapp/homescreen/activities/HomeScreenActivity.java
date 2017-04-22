package com.stelle.stelleapp.homescreen.activities;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.stelle.stelleapp.BaseActivity;
import com.stelle.stelleapp.R;
import com.stelle.stelleapp.homescreen.adapters.ShowHomeScreenAdapter;
import com.stelle.stelleapp.homescreen.fragments.MapScreenFragment;
import com.stelle.stelleapp.homescreen.interfaces.HomeScreenContract;
import com.stelle.stelleapp.widgets.CustomViewPager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeScreenActivity extends BaseActivity implements HomeScreenContract.View {

    @Bind(R.id.layoutProgressBar)
    protected LinearLayout progressBar;
    @Bind(R.id.viewPager)
    protected CustomViewPager viewPager;
    @Bind(R.id.imageViewMap)
    ImageView mapBtn;
    @Bind(R.id.imageViewEvent)
    ImageView eventBtn;
    @Bind(R.id.imageViewProfile)
    ImageView profileBtn;

    private ShowHomeScreenAdapter homeScreenAdapter;
    private MapScreenFragment mapScreenFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        showProgressBar(true);
        setupViewPager();
    }

    @Override
    public void setupViewPager() {
        homeScreenAdapter = new ShowHomeScreenAdapter(getSupportFragmentManager());
        mapScreenFragment = MapScreenFragment.newInstance();
        homeScreenAdapter.addFragment(mapScreenFragment);
        viewPager.setAdapter(homeScreenAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPagingEnabled(false);
        mapBtn.setImageResource(R.drawable.location_menu_img_selected);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                disableHomeNavigationSelected();
                if (position == 0) {
                    mapBtn.setImageResource(R.drawable.location_menu_img_selected);
                } else if (position == 1) {
                    eventBtn.setImageResource(R.drawable.recent_fixed_menu_img_selected);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
    public void disableHomeNavigationSelected() {
        mapBtn.setImageResource(R.drawable.location_menu_img);
        eventBtn.setImageResource(R.drawable.recent_fixed_menu_img);
        profileBtn.setImageResource(R.drawable.profile_menu_img);
    }

    @Override
    public boolean isViewDestroyed() {
        return isFinishing();
    }

    @OnClick(R.id.imageViewMap)
    public void onClickMap() {
        viewPager.setCurrentItem(0);
    }

    @OnClick(R.id.imageViewEvent)
    public void onClickEvent() {
        viewPager.setCurrentItem(1);
    }
}
