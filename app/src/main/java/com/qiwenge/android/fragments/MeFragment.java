package com.qiwenge.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.qiwenge.android.R;
import com.qiwenge.android.act.FeedbackActivity;
import com.qiwenge.android.act.SettingActivity;
import com.qiwenge.android.base.BaseFragment;
import com.qiwenge.android.login.FsAuthListener;
import com.qiwenge.android.login.LoginType;
import com.qiwenge.android.login.SinaWeiboLogin;
import com.qiwenge.android.ui.dialogs.LoginDialog;
import com.qiwenge.android.utils.ImageLoaderUtils;

import javax.security.auth.callback.Callback;

public class MeFragment extends BaseFragment implements View.OnClickListener {

    private ImageView ivAvatar;
    private TextView tvUserName;

    private TextView tvSet;
    private TextView tvFeedback;
    private LinearLayout layoutUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_set:
                startActivity(SettingActivity.class);
                break;
            case R.id.tv_feed_back:
                startActivity(FeedbackActivity.class);
                break;
            case R.id.layout_user:
                showLogin();
                break;
        }
    }

    private void initViews() {
        ivAvatar = (ImageView) getView().findViewById(R.id.iv_avatar);
        tvUserName = (TextView) getView().findViewById(R.id.tv_username);

        tvSet = (TextView) getView().findViewById(R.id.tv_set);
        tvSet.setOnClickListener(this);
        tvFeedback = (TextView) getView().findViewById(R.id.tv_feed_back);
        tvFeedback.setOnClickListener(this);

        layoutUser = (LinearLayout) getView().findViewById(R.id.layout_user);
        layoutUser.setOnClickListener(this);
    }

    private LoginDialog loginDialog;
    private String mUserName;
    private String mAvatarUrl;

    private void showLogin() {
        if (loginDialog == null) {
            loginDialog = new LoginDialog(getActivity());
            loginDialog.setAuthListener(new FsAuthListener() {
                @Override
                public void authSuccess(String uid, String username, String avatarUrl, LoginType loginType) {
                    System.out.println("授权成功");
                    System.out.println("username:" + username);
                    System.out.println("avatar:" + avatarUrl);
                    mAvatarUrl = avatarUrl;
                    mUserName = username;

                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }
            });
        }
        loginDialog.show();
    }

    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == 1) {
                DisplayImageOptions options = ImageLoaderUtils.createOptions(R.drawable.default_avatar);
                ImageLoader.getInstance().displayImage(mAvatarUrl+"?time=2014-11-23", ivAvatar, options);
                tvUserName.setText(mUserName);
            }
            return false;
        }
    });

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (SinaWeiboLogin.mSsoHandler != null && data != null) {
            SinaWeiboLogin.mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void login(){

    }

}
