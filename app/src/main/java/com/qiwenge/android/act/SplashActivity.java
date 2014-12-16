package com.qiwenge.android.act;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;

import com.dev1024.utils.AppUtils;
import com.loopj.android.http.RequestParams;
import com.qiwenge.android.R;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.constant.Platform;
import com.qiwenge.android.models.Configures;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.PushUtils;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.JsonResponseHandler;

public class SplashActivity extends BaseActivity {

    private final int mDuration = 500;

    /**
     * 是否初始化完毕
     */
    private boolean inited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !inited) {
            inited = true;
            getScreenSize();
            initJPush();
            start();
        }
    }

    private void start() {
        if (Constants.PLATFORM.equals(Platform.COMMON)) {
            Constants.openAutoReading = true;
            skipToMain();
        } else {
            connect();
        }
    }

    /**
     * 初始化极光推送
     */
    private void initJPush() {
//        new AsyncSetAlias(getApplicationContext()).execute(Constants.OEPN_UD_ID);
        new PushUtils().setAlias(getApplicationContext());
    }

    public void getScreenSize() {
        Display dis = getWindowManager().getDefaultDisplay();
        Point outSize = new Point(0, 0);
        dis.getSize(outSize);
        if (outSize != null) {
            Constants.WIDTH = outSize.x;
            Constants.HEIGHT = outSize.y;
        }
    }

    /**
     * 跳转到主页
     */
    private void skipToMain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, mDuration);
    }

    private void connect() {
        String url = ApiUtils.getConfigures();
        RequestParams params = new RequestParams();
        params.put("version", AppUtils.getVersionName(this));
        params.put("platform", Constants.PLATFORM);
        JHttpClient.get(url, params, new JsonResponseHandler<Configures>(Configures.class, false) {
            @Override
            public void onSuccess(Configures result) {
                if (result != null && result.functions != null) {
                    Constants.openAutoReading = result.functions.autoReading();
                }
            }

            @Override
            public void onFinish() {
                skipToMain();
            }
        });
    }

}
