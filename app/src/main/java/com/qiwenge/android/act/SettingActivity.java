package com.qiwenge.android.act;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuguangqiang.common.utils.AppUtils;
import com.liuguangqiang.common.utils.IntentUtils;
import com.liuguangqiang.common.utils.ToastUtils;
import com.qiwenge.android.R;
import com.qiwenge.android.async.AsyncCheckUpdate;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.listeners.OnPositiveClickListener;
import com.qiwenge.android.ui.dialogs.MyDialog;
import com.qiwenge.android.utils.ImageLoaderUtils;
import com.qiwenge.android.utils.LoginManager;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 设置。
 * <p/>
 * Created by Eric on 2014-7-9
 */
public class SettingActivity extends BaseActivity {

    @InjectView(R.id.tv_version_name)
    TextView tvVersionName;

    @InjectView(R.id.layout_set_user)
    LinearLayout layoutUser;

    @InjectView(R.id.iv_save_model)
    ImageView ivSaveModel;

    private MyDialog logoutDialog;

    private boolean selectSaveModel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle(R.string.set_title);
        ButterKnife.inject(this);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        layoutUser.setVisibility(LoginManager.isLogin() ? View.VISIBLE : View.GONE);
    }

    private void initViews() {
        tvVersionName.setText(AppUtils.getVersionName(getApplicationContext()));
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
            ImageLoaderUtils.openLoader(getApplicationContext());
        } else {
            setCheckbox(ivSaveModel, true);
            ImageLoaderUtils.closeLoader(getApplicationContext());
        }
        selectSaveModel = !selectSaveModel;
    }

    @OnClick(R.id.set_tv_rating)
    public void skipToMarket() {
        try {
            IntentUtils.skipToMarket(SettingActivity.this);
        } catch (Exception ex) {
            ToastUtils.show(getApplicationContext(), getString(R.string.error_not_find_market));
        }
    }

    @OnClick(R.id.set_tv_legal)
    public void skipToLegal() {
        startActivity(LegalActivity.class);
    }

    @OnClick(R.id.set_tv_update)
    public void chkVersionUpdate() {
        new AsyncCheckUpdate(this).checkUpdate();
    }

    @OnClick(R.id.set_tv_logout)
    public void showLogoutDialog() {
        logoutDialog = new MyDialog(this, R.string.set_logout_title);
        logoutDialog.setMessage(R.string.set_logout_msg);
        logoutDialog.setPositiveButton(R.string.str_sure, new OnPositiveClickListener() {
            @Override
            public void onClick() {
                LoginManager.logout(getApplicationContext());
                layoutUser.setVisibility(View.GONE);
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

}
