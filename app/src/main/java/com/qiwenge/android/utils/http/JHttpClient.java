package com.qiwenge.android.utils.http;

import android.content.Context;
import android.util.Log;

import com.liuguangqiang.common.utils.LogUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.qiwenge.android.entity.Auth;
import com.qiwenge.android.utils.FailureUtils;
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
        setAuthToken();
    }

    private static void setAuthToken() {
        if (LoginManager.isLogin()) {
            Auth auth = LoginManager.getAuth();
            httpClient.removeHeader(HEADER_AUTH);
            httpClient.addHeader(HEADER_AUTH, auth.authToken);
            Log.i(TAG, "AUTH_TOKEN:" + auth.authToken);
        } else {
            httpClient.removeHeader(HEADER_AUTH);
        }
    }

    /**
     * HTTP request with GET.
     *
     * @param url     地址
     * @param params  参数
     * @param handler 回调
     */
    public static void get(final Context context, String url, RequestParams params, final BaseResponseHandler handler) {
        if (params != null) LogUtils.i(TAG, "get:" + url + "?" + params.toString());
        else LogUtils.i(TAG, "get:" + url);
        createHttpCilent();
        httpClient.get(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (handler != null) {
                    handler.onFailure(responseString);
                    handler.onFailure(statusCode, responseString);
                    if (context != null) {
                        FailureUtils.handleHttpRequest(context, responseString, statusCode, throwable);
                    }
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

    public static void delete(String url, RequestParams params, final BaseResponseHandler handler) {
        if (url.contains("?")) {
            url = url + "&" + params.toString();
        } else {
            url = url + "?" + params.toString();
        }
        createHttpCilent();
        httpClient.delete(url, new TextHttpResponseHandler() {
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
