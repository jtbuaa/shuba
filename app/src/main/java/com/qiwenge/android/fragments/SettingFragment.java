package com.qiwenge.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuguangqiang.android.mvp.BaseUi;
import com.liuguangqiang.android.mvp.Presenter;
import com.liuguangqiang.common.utils.IntentUtils;
import com.liuguangqiang.common.utils.ToastUtils;
import com.qiwenge.android.R;
import com.qiwenge.android.act.LegalActivity;
import com.qiwenge.android.async.AsyncCheckUpdate;
import com.qiwenge.android.base.BaseFragment;
import com.qiwenge.android.listeners.OnPositiveClickListener;
import com.qiwenge.android.mvp.presenter.SettingsPresenter;
import com.qiwenge.android.mvp.ui.SettingUiCallback;
import com.qiwenge.android.mvp.ui.SettingsUi;
import com.qiwenge.android.ui.dialogs.MyDialog;
import com.qiwenge.android.utils.ImageLoaderUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SettingFragment extends BaseFragment implements SettingsUi {

    @InjectView(R.id.tv_version_name)
    TextView tvVersionName;

    @InjectView(R.id.layout_set_user)
    LinearLayout layoutUser;

    @InjectView(R.id.iv_save_model)
    ImageView ivSaveModel;

    private MyDialog logoutDialog;
    private boolean selectSaveModel = false;

    SettingUiCallback mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_setting, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    @Override
    public Presenter setPresenter() {
        return new SettingsPresenter(getActivity());
    }

    @Override
    public BaseUi setUi() {
        return this;
    }

    @Override
    public void setUiCallback(SettingUiCallback settingsUiCallback) {
        mCallback = settingsUiCallback;
    }

    public void initViews() {
        if (!ImageLoaderUtils.isOpen()) {
            setCheckbox(ivSaveModel, true);
            selectSaveModel = true;
        }
    }

    /**
     * 切换流量模式
     */
    @OnClick(R.id.iv_save_model)
    public void switchSaveModel() {
        if (selectSaveModel) {
            setCheckbox(ivSaveModel, false);
            ImageLoaderUtils.openLoader(getActivity());
        } else {
            setCheckbox(ivSaveModel, true);
            ImageLoaderUtils.closeLoader(getActivity());
        }
        selectSaveModel = !selectSaveModel;
    }

    @OnClick(R.id.set_tv_rating)
    public void skipToMarket() {
        try {
            IntentUtils.skipToMarket(getActivity());
        } catch (Exception ex) {
            ToastUtils.show(getActivity(), getString(R.string.error_not_find_market));
        }
    }

    @OnClick(R.id.set_tv_legal)
    public void skipToLegal() {
        startActivity(LegalActivity.class);
    }

    @OnClick(R.id.set_tv_update)
    public void chkVersionUpdate() {
        new AsyncCheckUpdate(getActivity()).checkUpdate();
    }

    @OnClick(R.id.set_tv_logout)
    public void showLogoutDialog() {
        logoutDialog = new MyDialog(getActivity(), R.string.set_logout_title);
        logoutDialog.setMessage(R.string.set_logout_msg);
        logoutDialog.setPositiveButton(R.string.str_sure, new OnPositiveClickListener() {
            @Override
            public void onClick() {
                mCallback.logout();
            }
        });
        logoutDialog.show();
    }

    private void setCheckbox(ImageView iv, boolean ischecked) {
        if (ischecked) {
            iv.setBackgroundResource(R.drawable.icon_switch_on);
        } else {
            iv.setBackgroundResource(R.drawable.icon_switch_off);
        }
    }

    @Override
    public void setVersionName(String versionName) {
        tvVersionName.setText(versionName);
    }

    @Override
    public void setLogoutVisibility(int visibility) {
        layoutUser.setVisibility(visibility);
    }
}


