package com.qiwenge.android.utils.http;

import com.dev1024.utils.LogUtils;

/**
 * BaseResponseHandler
 * 
 * Created by John
 */
public abstract class BaseResponseHandler {

    public abstract void onSuccess(String result);

    public void onFailure(String msg) {
        LogUtils.i("BaseResponseHandler", msg);
    };

    public void onFinish() {};

    public void onStart() {};

}
