package com.qiwenge.android.utils.level;

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

    private static void addExp(int type) {
        String url = ApiUtils.postLevel();
        RequestParams param = new RequestParams();
        param.put("type", "" + type);
        JHttpClient.setAuthToken();
        JHttpClient.post(url, param, new BaseResponseHandler() {
            @Override
            public void onSuccess(String result) {
                System.out.println("addExp-onSuccess");
            }
        });
    }

    public static void dailyLogin() {
        addExp(ExpType.DAILY_LOGIN);
    }

}
