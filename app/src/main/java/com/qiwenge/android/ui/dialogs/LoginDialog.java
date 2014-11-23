package com.qiwenge.android.ui.dialogs;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

import com.qiwenge.android.login.FsAuthListener;
import com.qiwenge.android.login.LoginType;
import com.qiwenge.android.login.SinaWeiboLogin;
import com.qiwenge.android.login.TencentLogin;
import com.sina.weibo.sdk.api.IWeiboAPI;

/**
 * Created by Eric on 14/11/12.
 */
public class LoginDialog {

    private MyDialog dialog;

    private String[] items = new String[]{"新浪微博登陆", "QQ登陆"};

    private Activity act;

    public LoginDialog(Activity context) {
        act = context;
        dialog = new MyDialog(context, "请选择登陆方式");
        dialog.setItems(items, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        loginSina();
                        break;
                    case 1:
                        loginByTencent();
                        break;
                }
            }
        });
    }

    public void show() {
        dialog.show();
    }

    private void loginSina() {
        SinaWeiboLogin.login(act, listener);
    }

    private void loginByTencent() {
        TencentLogin.login(act, listener);
    }

    private FsAuthListener listener;

    public void setAuthListener(FsAuthListener authListener) {
        listener = authListener;
    }

}
