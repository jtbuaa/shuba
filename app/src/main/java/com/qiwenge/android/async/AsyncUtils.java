package com.qiwenge.android.async;

import android.content.Context;

import com.liuguangqiang.common.utils.LogUtils;
import com.liuguangqiang.common.utils.StringUtils;
import com.liuguangqiang.common.utils.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.qiwenge.android.constant.BookStatus;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.entity.BookList;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.JsonResponseHandler;
import com.qiwenge.android.utils.http.StringResponseHandler;

/**
 * 常用的一些异步处理工具类 。
 * <p/>
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
        if (params != null) {
            params.put("limit", "" + Constants.DEFAULT_PAGE_SIZE);
            params.put("status", "" + BookStatus.APPROVED);
        }
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
                LogUtils.i("postViewTotal", "onStart");
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("postViewTotal", "onSuccess");
            }

            @Override
            public void onFailure(String msg) {
                LogUtils.e("postViewTotal", "onFailure");
            }
        });
    }

    /**
     * 赞
     *
     * @param bookId
     */
    public static void postVoteup(final Context context, String bookId) {
        String url = ApiUtils.postBookVoteUp(bookId);
        JHttpClient.post(url, null, new StringResponseHandler() {

            @Override
            public void onStart() {
                LogUtils.i("postViewVoteup-onStart", "onStart");
            }

            @Override
            public void onSuccess(String result) {
                if (StringUtils.isEmptyOrNull(result)) result = "success with empty result.";
                LogUtils.i("postViewVoteup-onSuccess", result);
                ToastUtils.show(context, "点赞成功");
            }

            @Override
            public void onFailure(String msg) {
                if (StringUtils.isEmptyOrNull(msg)) msg = "Failure with unknow message.";
                LogUtils.e("postViewVoteup-onFailure", msg);
                ToastUtils.show(context, "点赞失败");
            }
        });
    }

}
