package com.qiwenge.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiwenge.android.R;
import com.qiwenge.android.act.FeedbackActivity;
import com.qiwenge.android.act.SettingActivity;
import com.qiwenge.android.base.BaseFragment;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.login.AuthSuccess;
import com.qiwenge.android.models.Auth;
import com.qiwenge.android.models.User;
import com.qiwenge.android.models.UserLevel;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.ImageLoaderUtils;
import com.qiwenge.android.utils.LoginManager;
import com.qiwenge.android.utils.http.BaseResponseHandler;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.JsonResponseHandler;

import java.util.logging.Level;

public class MeFragment extends BaseFragment implements View.OnClickListener {

    private final static String LEVEL_FORMAT = "LV.%s (%s/%s)";

    private ImageView ivAvatar;
    private TextView tvUserName;
    private TextView tvLevel;

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
        if (LoginManager.isLogin()) {
            getUser(LoginManager.getUser().getId());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LoginManager.isLogout && !LoginManager.isLogin()) {
            LoginManager.isLogout = false;
            clearUser();
        }
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
                showAuthDialog();
                break;
        }
    }

    private void initViews() {
        ivAvatar = (ImageView) getView().findViewById(R.id.iv_avatar);
        tvUserName = (TextView) getView().findViewById(R.id.tv_username);
        tvLevel = (TextView) getView().findViewById(R.id.tv_level);

        tvSet = (TextView) getView().findViewById(R.id.tv_set);
        tvSet.setOnClickListener(this);
        tvFeedback = (TextView) getView().findViewById(R.id.tv_feed_back);
        tvFeedback.setOnClickListener(this);

        layoutUser = (LinearLayout) getView().findViewById(R.id.layout_user);
        layoutUser.setOnClickListener(this);

        showUser();
    }

    private void showUser() {
        if (LoginManager.isLogin()) {
            showUser(LoginManager.getUser());
        }
    }

    private void showUser(User user) {
        if (user != null) {
            tvUserName.setText(user.username);
            DisplayImageOptions options = ImageLoaderUtils.createOptions(R.drawable.default_avatar);
            ImageLoader.getInstance().displayImage(user.avatar, ivAvatar, options);
            UserLevel level = user.level;
            tvLevel.setText(String.format(LEVEL_FORMAT, level.rank, level.exp, level.next));
        }
    }

    private void clearUser() {
        tvUserName.setText("未登陆");
        tvLevel.setText("0");
        ivAvatar.setImageResource(R.drawable.default_avatar);
    }

    @Override
    public void onAuthStart() {
        super.onAuthStart();
        tvUserName.setText("正在登录...");
    }

    @Override
    public void onAuthSuccess(AuthSuccess authSuccess) {
        super.onAuthSuccess(authSuccess);
        login(authSuccess);
    }

    private void login(final AuthSuccess authSuccess) {
        String url = ApiUtils.putAuth();
        System.out.println("login:" + url);
        RequestParams params = new RequestParams();
        params.put("open_id", authSuccess.getOpenId().toLowerCase());
        JHttpClient.put(url, params, new JsonResponseHandler<Auth>(Auth.class, false) {
            @Override
            public void onSuccess(Auth result) {
                if (result != null && isAdded()) {
                    getUser(result.userId);
                    LoginManager.saveAuth(getActivity(), result);
                }
            }

            @Override
            public void onFailure(int statusCode, String msg) {
                System.out.println("login-onFailure-statusCode:" + statusCode);
                System.out.println("login-onFailure-msg:" + msg);
                if (statusCode == Constants.STATUS_CODE_UN_REG) {
                    reg(authSuccess);
                }
            }

            @Override
            public void onFinish() {
                System.out.println("login-onFinish");
            }
        });
    }

    private void reg(AuthSuccess authSuccess) {
        String url = ApiUtils.postUser();
        System.out.println("reg:" + url);
        RequestParams params = new RequestParams();
        params.put("username", authSuccess.getUsername());
        params.put("avatar", authSuccess.getAvatarUrl());
        params.put("open_id", authSuccess.getOpenId().toLowerCase());
        params.put("from", authSuccess.getLoginType().toString());
        System.out.println("reg:" + url + "?" + params.toString());
        JHttpClient.post(url, params, new BaseResponseHandler() {
            @Override
            public void onSuccess(String result) {
                System.out.println("reg-onSuccess:" + result);
            }

            @Override
            public void onFailure(int statusCode, String msg) {
                System.out.println("reg-onFailure-statusCode:" + statusCode);
                System.out.println("reg-onFailure-msg:" + msg);
            }

            @Override
            public void onFinish() {
                System.out.println("reg-onFinish");
            }
        });
    }

    private void getUser(String userId) {
        System.out.println("getUser:" + userId);
        String url = ApiUtils.getUser(userId);
        JHttpClient.get(url, null, new JsonResponseHandler<User>(User.class, false) {

            @Override
            public void onOrigin(String json) {
                System.out.println("getUser-onSuccess:" + json);
            }

            @Override
            public void onSuccess(User result) {
                LoginManager.saveUser(getActivity(), result);
                showUser(result);
            }

            @Override
            public void onFinish() {
                System.out.println("getUser-onFinish");
            }
        });
    }

}
