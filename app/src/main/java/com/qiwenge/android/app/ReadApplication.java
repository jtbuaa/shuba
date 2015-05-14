package com.qiwenge.android.app;

import android.app.Application;
import android.content.Context;

import com.liuguangqiang.framework.utils.DeviceId;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.module.AppModule;
import com.qiwenge.android.module.PresenterModule;
import com.qiwenge.android.utils.ImageLoaderUtils;
import com.qiwenge.android.utils.LoginManager;
import com.qiwenge.android.utils.OfflineUtils;
import com.qiwenge.android.utils.ThemeUtils;

import cn.jpush.android.api.JPushInterface;
import dagger.ObjectGraph;

public class ReadApplication extends Application {

    private ObjectGraph mObjectGraph;

    public static ReadApplication from(Context context) {
        return (ReadApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initJPush();
        ImageLoaderUtils.init(getApplicationContext());
        ThemeUtils.initTheme(getApplicationContext());
        LoginManager.init(getApplicationContext());
        initOpenUDID();
        createFolder();

        createObjectsGraph();
    }

    private void createObjectsGraph() {
        mObjectGraph = ObjectGraph.create(
                AppModule.class,
                PresenterModule.class
        );
        mObjectGraph.inject(this);
    }

    private void initJPush() {
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
    }

    private void initOpenUDID() {
        Constants.OEPN_UD_ID = DeviceId.getInstance(this).getDeviceId();
    }

    private void createFolder() {
        OfflineUtils.createOfflineFolder();
    }

    public void inject(Object object) {
        mObjectGraph.inject(object);
    }
}
