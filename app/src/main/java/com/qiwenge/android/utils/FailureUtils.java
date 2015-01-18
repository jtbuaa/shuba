package com.qiwenge.android.utils;

import android.content.Context;

import com.liuguangqiang.common.utils.NetworkUtils;
import com.liuguangqiang.common.utils.ToastUtils;
import com.qiwenge.android.R;

/**
 * Created by Eric on 15/1/10.
 */
public class FailureUtils {

    private static final String TAG = "FailureUtils";
    private static final String ERROR_DETAIL = "error:%s,statusCode:%s,throwable:%s";

    private static long lastToastOnNetwork = 0;

    public static void handleHttpRequest(Context context, String error, int statusCode, Throwable throwable) {
//        Log.i(TAG, String.format(ERROR_DETAIL, error, statusCode, throwable));
        if (NetworkUtils.isAvailable(context)) {

        } else {
            long now = TimeUtils.getTimestampSecond();
            if (now - lastToastOnNetwork > 1) {
                String username = LoginManager.isLogin() ? LoginManager.getUser().username : "";
                ToastUtils.show(context, String.format(context.getString(R.string.error_network_unavailable_format), username));
            }
            lastToastOnNetwork = now;
        }
    }

}
