package com.qiwenge.android.async;

import android.app.Activity;

import com.dev1024.utils.LogUtils;
import com.dev1024.utils.StringUtils;
import com.loopj.android.http.RequestParams;
import com.qiwenge.android.constant.BookStatus;
import com.qiwenge.android.models.BookList;
import com.qiwenge.android.models.Configures;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.JsonResponseHandler;
import com.qiwenge.android.utils.http.StringResponseHandler;

/**
 * 常用的一些异步处理工具类 。
 * 
 * Created by John on 2014-6-30
 */
public class AsyncUtils {

    /**
     * 获取小说
     * 
     * @param url
     * @param page
     */
    public static void getBooks(String url, int page, JsonResponseHandler<BookList> handler) {
        RequestParams params = new RequestParams();
        params.put("page", "" + page);
        getBooks(url, params, handler);
    }

    public static void getBooks(String url, RequestParams params,
            JsonResponseHandler<BookList> handler) {
        params.put("limit", "20");
        params.put("status", "" + BookStatus.APPROVED);
        JHttpClient.get(url, params, handler);
    }

    /**
     * 书籍点击次数自增。
     * 
     * @param bookId
     */
    public static void postViewTotal(String bookId) {
        String url = ApiUtils.postViewTotal(bookId);
        JHttpClient.post(url, null, new StringResponseHandler() {

            @Override
            public void onStart() {
                LogUtils.i("postViewTotal-onStart", "onStart");
            }

            @Override
            public void onSuccess(String result) {
                if (StringUtils.isEmptyOrNull(result)) result = "success with empty result.";
                LogUtils.i("postViewTotal-onSuccess", result);
            }

            @Override
            public void onFailure(String msg) {
                if (StringUtils.isEmptyOrNull(msg)) msg = "Failure with unknow message.";
                LogUtils.e("postViewTotal-onFailure", msg);
            }
        });
    }

}
