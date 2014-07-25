package com.qiwenge.android.utils.http;

import android.content.Context;

import com.dev1024.utils.LogUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

public class JHttpClient {

    private static final String TAG = "JHttpClient";

    private static AsyncHttpClient httpClient;

    private static AsyncHttpClient createHttpCilent() {
        return new AsyncHttpClient();
    }

    public static void cancelRequests(Context context) {
        if (httpClient != null) httpClient.cancelRequests(context, true);
    }

    /**
     * HTTP request with GET.
     *
     * @param url     地址
     * @param params  参数
     * @param handler
     */
    public static void get(String url, RequestParams params, final BaseResponseHandler handler) {
        if (params != null) LogUtils.i(TAG, "get-latest:" + url + "?" + params.toString());
        else LogUtils.i(TAG, "get-latest:" + url);
        if (httpClient == null) httpClient = createHttpCilent();
        httpClient.get(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (handler != null) handler.onFailure(responseString);
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
        if (httpClient == null) httpClient = createHttpCilent();
        httpClient.post(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (handler != null) handler.onFailure(responseString);
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
        if (httpClient == null) httpClient = createHttpCilent();
        httpClient.put(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (handler != null) handler.onFailure(responseString);
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
