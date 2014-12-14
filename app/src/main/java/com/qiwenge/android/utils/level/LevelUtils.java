package com.qiwenge.android.utils.level;

import android.content.Context;

import com.dev1024.utils.PreferencesUtils;
import com.dev1024.utils.TimeUtils;
import com.loopj.android.http.RequestParams;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.http.BaseResponseHandler;
import com.qiwenge.android.utils.http.JHttpClient;

/**
 * 经验管理
 * <p/>
 * Created by Eric on 14/11/26.
 */
public class LevelUtils {

    private final static String NAME = "LEVEL_MANAGE";

    private final static String DAILY_LOGIN = "DAILY_LOGIN";

    private static void addExp(int type, BaseResponseHandler responseHandler) {
        String url = ApiUtils.postLevel();
        RequestParams param = new RequestParams();
        param.put("type", "" + type);
        JHttpClient.setAuthToken();
        JHttpClient.post(url, param, responseHandler);
    }

    public static void dailyLogin(final Context context) {
        System.out.println("dailyLogin");
        int daily = PreferencesUtils.getInt(context, NAME, DAILY_LOGIN);
        final int date = Integer.parseInt(TimeUtils.getDateTimeByFormat("yyyyMMdd"));
        if (date > daily) {
            addExp(ExpType.DAILY_LOGIN, new BaseResponseHandler() {
                @Override
                public void onSuccess(String result) {
                    System.out.println("dailyLogin success");
                    PreferencesUtils.putInt(context, NAME, DAILY_LOGIN, date);
                }
            });
        }
    }

}
