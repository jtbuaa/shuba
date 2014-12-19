package com.qiwenge.android.async;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import com.dev1024.utils.GsonUtils;
import com.dev1024.utils.PreferencesUtils;
import com.dev1024.utils.StringUtils;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.entity.BookList;


/**
 * 获取缓存的书籍。
 * <p>
 * 比如: 推荐,排行
 * </p>
 *
 * Created by John on 2014年7月19日
 */
public class AsyncGetCacheBooks extends AsyncTask<String, Integer, BookList> {

    private Context mContext;

    private CacheBooksHandler mHandler;

    public AsyncGetCacheBooks(Context context, CacheBooksHandler handler) {
        this.mContext = context;
        this.mHandler = handler;
    }

    @Override
    protected BookList doInBackground(String... params) {
        if (params != null && params[0] != null) {
            String json = PreferencesUtils.getString(mContext, Constants.PRE_SAVE_NAME, params[0]);
            if (StringUtils.isEmptyOrNull(json))
                return null;
            else
                return GsonUtils.getModel(json, BookList.class);
        }
        return null;
    }

    @Override
    protected void onPostExecute(BookList result) {
        if (mHandler != null) {
            if (result != null)
                mHandler.onSuccess(result.result);
            else
                mHandler.onEmpty();
        }
    }

    public interface CacheBooksHandler {

        public void onSuccess(List<Book> list);

        public void onEmpty();

    }

}
