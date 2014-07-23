package com.qiwenge.android.app;

import com.qiwenge.android.utils.ThemeUtils;

import android.app.Application;

public class ReadApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ThemeUtils.initTheme(getApplicationContext());
    }
}
