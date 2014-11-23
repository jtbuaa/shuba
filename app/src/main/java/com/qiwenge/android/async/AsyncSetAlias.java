package com.qiwenge.android.async;

import java.util.Set;

import android.content.Context;
import android.os.AsyncTask;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 初始化极光。
 * <p/>
 * Created by John on 2014年7月1日
 */
public class AsyncSetAlias extends AsyncTask<String, Integer, Boolean> {

    private Context mContext;

    public AsyncSetAlias(Context context) {
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        System.out.println("设置极光别名");
        if (params != null) {
            JPushInterface.setAlias(mContext, params[0], new TagAliasCallback() {
                @Override
                public void gotResult(int arg0, String arg1, Set<String> arg2) {
                    if (arg0 == 0) {
                        System.out.println("极光别名设置成功:" + arg1);
                    } else {
                        System.out.println("极光别名设置失败:" + arg0);
                    }

                }
            });
        }

        return true;
    }

}
