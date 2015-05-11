package com.qiwenge.android.mvp.presenter;

import android.content.Context;
import android.view.View;

import com.liuguangqiang.android.mvp.Presenter;
import com.qiwenge.android.mvp.model.SettingsModel;
import com.qiwenge.android.mvp.ui.SettingUiCallback;
import com.qiwenge.android.mvp.ui.SettingsUi;
import com.qiwenge.android.utils.LoginManager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Eric on 15/5/7.
 */
@Singleton
public class SettingsPresenter extends Presenter<SettingsUi, SettingUiCallback> {

    Context mContext;

    @Inject
    SettingsModel settingsModel;

    public SettingsPresenter(Context context) {
        this.mContext = context;
        settingsModel = new SettingsModel();
    }

    @Override
    protected void populateUi(SettingsUi settingsUi) {
        settingsUi.setVersionName(settingsModel.getVersionName(mContext));

        int visibility = LoginManager.isLogin() ? View.VISIBLE : View.GONE;
        settingsUi.setLogoutVisibility(visibility);
    }

    @Override
    protected SettingUiCallback createUiCallback(final SettingsUi settingsUi) {
        return new SettingUiCallback() {
            @Override
            public void logout() {
                LoginManager.logout(mContext);
                settingsUi.setLogoutVisibility(View.GONE);
            }
        };
    }

}
