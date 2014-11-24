package com.qiwenge.android.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.qiwenge.android.login.AuthListener;
import com.qiwenge.android.login.LoginType;
import com.qiwenge.android.ui.dialogs.LoginDialog;

/**
 * BaseFragment
 * <p/>
 * Created by John on 2014-4-26
 */
public class BaseFragment extends Fragment {


    private LoginDialog loginDialog;
    private String mUserName;
    private String mAvatarUrl;

    public void showAuthDialog() {
        if (loginDialog == null) {
            loginDialog = new LoginDialog(getActivity());
            loginDialog.setAuthListener(new AuthListener() {

                @Override
                public void onStart() {
                    onAuthStart();
                }

                @Override
                public void authSuccess(String uid, String username, String avatarUrl, LoginType loginType) {
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
                onAuthSuccess(mUserName, mAvatarUrl);
            }
            return false;
        }
    });

    public void onAuthStart() {
    }

    public void onAuthSuccess(String username, String avatarUrl) {
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    public void startActivity(Class<?> cls, Bundle extra) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtras(extra);
        startActivity(intent);
    }

}
