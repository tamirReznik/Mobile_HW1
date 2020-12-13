package com.college.mobile_hw1;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        MediaManager.init(this);
        SharedPreferencesManager.init(this);
    }


}
