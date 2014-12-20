package com.qiwenge.android.utils.http;


import com.liuguangqiang.common.utils.LogUtils;

/**
 * BaseResponseHandler
 * <p/>
 * Created by John
 */
public abstract class BaseResponseHandler {

    public abstract void onSuccess(String result);

    public void onFailure(String msg) {
        if (msg != null)
            LogUtils.i("BaseResponseHandler", msg);
    }

    public void onFailure(int statusCode,String msg){

    }

    public void onFinish() {
    }

    public void onStart() {
    }

}
