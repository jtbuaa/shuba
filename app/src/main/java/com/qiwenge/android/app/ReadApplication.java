package com.qiwenge.android.app;

import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.openudid.OpenUDIDUtils;
import com.qiwenge.android.utils.ImageLoaderUtils;
import com.qiwenge.android.utils.LoginManager;
import com.qiwenge.android.utils.ThemeUtils;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

public class ReadApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderUtils.init(getApplicationContext());
        ThemeUtils.initTheme(getApplicationContext());
        LoginManager.init(getApplicationContext());
        initJPush();
        initOpenUDID();
    }

    private void initJPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    private void initOpenUDID() {
        Constants.OEPN_UD_ID = OpenUDIDUtils.getOpenUDID(getApplicationContext());
    }

}
