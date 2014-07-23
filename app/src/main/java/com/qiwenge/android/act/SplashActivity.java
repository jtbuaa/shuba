package com.qiwenge.android.act;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;

import com.qiwenge.android.R;
import com.qiwenge.android.async.AsyncSetAlias;
import com.qiwenge.android.constant.Constants;

public class SplashActivity extends Activity {

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
            skipToMain();
        }
    }

    /**
     * 初始化极光推送
     */
    private void initJPush() {
        new AsyncSetAlias(getApplicationContext()).execute("test");
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

}
