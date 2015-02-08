package com.qiwenge.android.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.qiwenge.android.listeners.CommonHandler;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.PushUtils;
import com.qiwenge.android.utils.book.BookManager;
import com.qiwenge.android.utils.http.BaseResponseHandler;
import com.qiwenge.android.utils.http.JHttpClient;

/**
 * 异步从书架移除书
 */
public class AsyncRemoveBook extends AsyncTask<Book, Integer, Boolean> {

    private CommonHandler mHandler;

    private Context mContext;

    private String bookId = "";

    public AsyncRemoveBook(Context context, CommonHandler handler) {
        mContext = context;
        mHandler = handler;
    }

    @Override
    protected Boolean doInBackground(Book... params) {
        if (params != null && params[0] != null) {
            Book book = params[0];
            bookId = book.getId();
            BookManager.getInstance().delete(mContext, book);
            new PushUtils(mContext).setTags(BookManager.getInstance().getAll());
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onPreExecute() {
        if (mHandler != null) mHandler.onStart();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            deleteProgresses(bookId);
            if (mHandler != null) {
                mHandler.onSuccess();
            }
        } else {
            mHandler.onFailure();
        }
    }

    /**
     * 移除小说的时候，删除服务器上的进度
     *
     * @param bookId
     */
    private void deleteProgresses(String bookId) {
        String url = ApiUtils.getProgresses();
        RequestParams params = new RequestParams();
        params.put("book_id", bookId);
        JHttpClient.delete(url, params, new BaseResponseHandler() {
            @Override
            public void onSuccess(String result) {
                Log.i("BookShelf", "deleteProgresses-onSuccess");
            }
        });
    }

}
