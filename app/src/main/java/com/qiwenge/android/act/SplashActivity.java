package com.qiwenge.android.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.liuguangqiang.framework.utils.AppUtils;
import com.loopj.android.http.RequestParams;
import com.qiwenge.android.R;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.entity.Configures;
import com.qiwenge.android.entity.Mirror;
import com.qiwenge.android.entity.Progresses;
import com.qiwenge.android.entity.ProgressesList;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.book.BookManager;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.JsonResponseHandler;

import java.util.List;

public class SplashActivity extends BaseActivity {

    private static final String TAG = "splash";

    private static final int mDuration = 500;

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
            getScreenSize(this);

//            if (NetworkUtils.isAvailable(getApplicationContext())) {
//                getProgresses();
//            } else {
//                skipToMain();
//            }
            skipToMain();
        }
    }

    private void getProgresses() {
        String url = ApiUtils.getProgresses();
        JHttpClient.get(getApplicationContext(), url, null, new JsonResponseHandler<ProgressesList>(ProgressesList.class) {
            @Override
            public void onSuccess(ProgressesList result) {
                if (result != null) {
                    filterBooks(result.result);
                    skipToMain();
                }
            }

            @Override
            public void onOrigin(String json) {
            }

            @Override
            public void onFailure(String msg) {
                skipToMain();
            }
        });
    }

    private void filterBooks(List<Progresses> list) {
        Book book;
        Mirror mirror;
        Progresses realProgresses;
        Progresses p;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            p = list.get(i);
            book = p.book;

            realProgresses = new Progresses();
            realProgresses.chapter_id = p.chapter_id;
            realProgresses.book_id = p.book_id;
            realProgresses.chapters = p.chapters;
            realProgresses.chars = p.chars;

            mirror = new Mirror();
            mirror.setId(p.mirror_id);
            mirror.progress = realProgresses;
            mirror.book_id = book.getId();
            book.mirrorList.add(mirror);

            if (!BookManager.getInstance().contains(book)) {
                BookManager.getInstance().add(getApplicationContext(), book);
            }
        }

        BookManager.getInstance().updateProgresses(getApplicationContext(), list);
    }

    public static void getScreenSize(Activity activity) {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        Constants.WIDTH = dm.widthPixels * dm.densityDpi;
        Constants.HEIGHT = dm.heightPixels * dm.densityDpi;
    }

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
        JHttpClient.get(getApplicationContext(), url, params, new JsonResponseHandler<Configures>(Configures.class, false) {
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
