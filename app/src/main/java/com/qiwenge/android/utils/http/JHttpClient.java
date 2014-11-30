package com.qiwenge.android.utils.http;

import com.dev1024.utils.LogUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.qiwenge.android.models.Auth;
import com.qiwenge.android.utils.LoginManager;

import org.apache.http.Header;

public class JHttpClient {

    private static final String TAG = "JHttpClient";

    private static AsyncHttpClient httpClient;

    private static final String HEADER_AUTH = "Authorization";

    private static void createHttpCilent() {
        if (httpClient == null) {
            httpClient = new AsyncHttpClient();
        }
    }

    public static void setAuthToken() {
        createHttpCilent();
        if (LoginManager.isLogin()) {
            Auth auth = LoginManager.getAuth();
            httpClient.removeHeader(HEADER_AUTH);
            httpClient.addHeader(HEADER_AUTH, auth.authToken);
            System.out.println(HEADER_AUTH + auth.authToken);
        }
    }

    /**
     * HTTP request with GET.
     *
     * @param url     地址
     * @param params  参数
     * @param handler 回调
     */
    public static void get(String url, RequestParams params, final BaseResponseHandler handler) {
        if (params != null) LogUtils.i(TAG, "get:" + url + "?" + params.toString());
        else LogUtils.i(TAG, "get:" + url);
        createHttpCilent();
        httpClient.get(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (handler != null) {
                    handler.onFailure(responseString);
                    handler.onFailure(statusCode, responseString);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (handler != null) handler.onSuccess(responseString);
            }

            @Override
            public void onStart() {
                if (handler != null) handler.onStart();
            }

            @Override
            public void onFinish() {
                if (handler != null) handler.onFinish();
            }
        });

    }

    public static void post(String url, RequestParams params, final BaseResponseHandler handler) {
        if (params != null) LogUtils.i(TAG, "post:" + url + "?" + params.toString());
        else LogUtils.i(TAG, "post:" + url);
        createHttpCilent();
        httpClient.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (handler != null) {
                    handler.onFailure(responseString);
                    handler.onFailure(statusCode, responseString);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (handler != null) handler.onSuccess(responseString);
            }

            @Override
            public void onStart() {
                if (handler != null) handler.onStart();
            }

            @Override
            public void onFinish() {
                if (handler != null) handler.onFinish();
            }
        });
    }

    public static void put(String url, RequestParams params, final BaseResponseHandler handler) {
        createHttpCilent();
        httpClient.put(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (handler != null) {
                    handler.onFailure(responseString);
                    handler.onFailure(statusCode, responseString);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (handler != null) handler.onSuccess(responseString);
            }

            @Override
            public void onStart() {
                if (handler != null) handler.onStart();
            }

            @Override
            public void onFinish() {
                if (handler != null) handler.onFinish();
            }
        });
    }
}
