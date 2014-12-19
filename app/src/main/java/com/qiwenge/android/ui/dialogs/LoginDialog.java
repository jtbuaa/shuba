package com.qiwenge.android.ui.dialogs;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

import com.qiwenge.android.R;
import com.qiwenge.android.login.AuthListener;
import com.qiwenge.android.login.SinaWeiboLogin;
import com.qiwenge.android.login.TencentLogin;

/**
 * Created by Eric on 14/11/12.
 */
public class LoginDialog {

    private MyDialog dialog;


    private Activity act;

    public LoginDialog(Activity context) {
        act = context;
        dialog = new MyDialog(context, R.string.choose_login_type);
        String[] items = context.getResources().getStringArray(R.array.login_types);
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

    private AuthListener listener;

    public void setAuthListener(AuthListener authListener) {
        listener = authListener;
    }

}
