package com.stelle.stelleapp.getstarted.interfaces;

import com.stelle.stelleapp.getstarted.parsers.CompleteProfileRequestParser;

/**
 * Created by Sarthak on 22-04-2017
 */

public interface CompleteProfileContract {

    interface View {
        void init();
        void moveToNextScreen();
    }

    interface Presenter {
        void callUpdateProfileApi(CompleteProfileRequestParser requestParser);

        void onUpdateProfileApiSuccess();
    }
}

