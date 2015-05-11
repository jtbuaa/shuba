package com.qiwenge.android.mvp.model;

import android.content.Context;

import com.liuguangqiang.common.utils.AppUtils;

import javax.inject.Inject;

/**
 * Created by Eric on 15/5/7.
 */
public class SettingsModel {

    @Inject
    public SettingsModel() {
    }

    public String getVersionName(Context context) {
        return AppUtils.getVersionName(context);
    }

}
