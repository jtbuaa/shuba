package com.qiwenge.android.app;

import com.dev1024.utils.AppUtils;
import com.dev1024.utils.StringUtils;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.openudid.OpenUDID_manager;
import com.qiwenge.android.utils.ThemeUtils;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

public class ReadApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ThemeUtils.initTheme(getApplicationContext());
        initJPush();
        initOpenUDID();
    }

    private void initJPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    private void initOpenUDID() {
        OpenUDID_manager.sync(this);
        OpenUDID_manager.isInitialized();
        Constants.OEPN_UD_ID = OpenUDID_manager.getOpenUDID();
        if (StringUtils.isEmptyOrNull(Constants.OEPN_UD_ID)) {
            System.out.println("用IMEI号做OpenUDID");
            Constants.OEPN_UD_ID = AppUtils.getImeiCode(getApplicationContext());
        }
    }

}
