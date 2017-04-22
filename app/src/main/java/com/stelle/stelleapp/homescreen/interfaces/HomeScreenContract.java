package com.stelle.stelleapp.homescreen.interfaces;

/**
 * Created by Sarthak on 22-04-2017
 */

public interface HomeScreenContract {

    interface View {
        void init();

        void setupViewPager();

        void showProgressBar(boolean show);

        void disableHomeNavigationSelected();

        boolean isViewDestroyed();
    }
}
