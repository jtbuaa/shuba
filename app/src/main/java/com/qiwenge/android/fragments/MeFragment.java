package com.qiwenge.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuguangqiang.common.utils.DisplayUtils;
import com.liuguangqiang.common.utils.IntentUtils;
import com.liuguangqiang.common.utils.ToastUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiwenge.android.R;
import com.qiwenge.android.act.FeedbackActivity;
import com.qiwenge.android.act.SettingActivity;
import com.qiwenge.android.base.BaseFragment;
import com.qiwenge.android.listeners.LoginListener;
import com.qiwenge.android.entity.User;
import com.qiwenge.android.entity.UserLevel;
import com.qiwenge.android.ui.dialogs.LoginDialog;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.ImageLoaderUtils;
import com.qiwenge.android.utils.LoginManager;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.JsonResponseHandler;

public class MeFragment extends BaseFragment implements View.OnClickListener {

    private final static String LEVEL_FORMAT = "LV.%s (%s/%s)";

    private ImageView ivAvatar;
    private TextView tvUserName;
    private TextView tvLevel;

    private TextView tvSet;
    private TextView tvRating;
    private TextView tvFeedback;
    private LinearLayout layoutUser;
    private LoginDialog loginDialog;

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
        showUser();
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
            case R.id.tv_rating:
                skipToMarket();
                break;
            case R.id.layout_user:
                showLoginDialog();
                break;
        }
    }

    private void skipToMarket() {
        try {
            IntentUtils.skipToMarket(getActivity());
        } catch (Exception ex) {
            ToastUtils.show(getActivity(), getString(R.string.error_not_find_market));
        }
    }

    private void showLoginDialog() {
        if (loginDialog == null) {
            loginDialog = new LoginDialog(getActivity());
            loginDialog.setLoginListener(new LoginListener() {
                @Override
                public void onSuccess() {
                    showUser(LoginManager.getUser());
                }
            });
        }
        loginDialog.show();
    }

    private void initViews() {
        ivAvatar = (ImageView) getView().findViewById(R.id.iv_avatar);
        tvUserName = (TextView) getView().findViewById(R.id.tv_username);
        tvLevel = (TextView) getView().findViewById(R.id.tv_level);

        tvSet = (TextView) getView().findViewById(R.id.tv_set);
        tvSet.setOnClickListener(this);
        tvFeedback = (TextView) getView().findViewById(R.id.tv_feed_back);
        tvFeedback.setOnClickListener(this);
        tvRating = (TextView) getView().findViewById(R.id.tv_rating);
        tvRating.setOnClickListener(this);

        layoutUser = (LinearLayout) getView().findViewById(R.id.layout_user);
        layoutUser.setOnClickListener(this);
    }

    private void showUser() {
        if (LoginManager.isLogin()) {
            showUser(LoginManager.getUser());
        }
    }

    private void showUser(User user) {
        if (user != null) {
            tvUserName.setText(user.username);
            DisplayImageOptions options = ImageLoaderUtils.createOptions(R.drawable.default_avatar,
                    DisplayUtils.dip2px(getActivity(), 70));
            ImageLoader.getInstance().displayImage(user.avatar, ivAvatar, options);
            UserLevel level = user.level;
            tvLevel.setText(String.format(LEVEL_FORMAT, level.rank, level.exp, level.next));
            tvLevel.setVisibility(View.VISIBLE);
        }
    }

    private void clearUser() {
        tvUserName.setText(R.string.choose_login_type);
        tvLevel.setText(R.string.login_desc);
        ivAvatar.setImageResource(R.drawable.default_avatar);
    }

    private void getUser(String userId) {
        System.out.println("getUser:" + userId);
        String url = ApiUtils.getUser(userId);
        JHttpClient.get(getActivity(), url, null, new JsonResponseHandler<User>(User.class, false) {

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
            }
        });
    }

}
