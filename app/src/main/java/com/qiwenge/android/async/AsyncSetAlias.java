package com.qiwenge.android.async;

import java.util.Set;

import android.content.Context;
import android.os.AsyncTask;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 初始化极光。
 * 
 * Created by John on 2014年7月1日
 */
public class AsyncSetAlias extends AsyncTask<String, Integer, Boolean> {

    private Context mContext;

    public AsyncSetAlias(Context context) {
        this.mContext = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if (params != null) {
            JPushInterface.setDebugMode(true);
            JPushInterface.init(mContext);
            JPushInterface.setAlias(mContext, params[0], new TagAliasCallback() {
                @Override
                public void gotResult(int arg0, String arg1, Set<String> arg2) {
                    System.out.println("code:" + arg0);
                    System.out.println("p1:" + arg1);
                }
            });
        }

        return true;
    }

}
