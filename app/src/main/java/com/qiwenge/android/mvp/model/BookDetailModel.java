package com.qiwenge.android.mvp.model;

import com.liuguangqiang.framework.utils.GsonUtils;
import com.loopj.android.http.RequestParams;
import com.qiwenge.android.constant.BookStatus;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.entity.BookList;
import com.qiwenge.android.mvp.ui.BookDetailUi;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.book.BookManager;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.StringResponseHandler;

import javax.inject.Inject;

/**
 * Created by Eric on 15/5/8.
 */
public class BookDetailModel {

    @Inject
    public BookDetailModel() {
    }

    public void getRelatedBooks(String bookId, final BookDetailUi ui) {
        String url = ApiUtils.getRelated(bookId);
        RequestParams params = new RequestParams();
        params.put("limit", "4");
        params.put("status", "" + BookStatus.APPROVED);
        JHttpClient.get(url, params, new StringResponseHandler() {
            @Override
            public void onSuccess(String result) {
                BookList list = GsonUtils.getModel(result, BookList.class);
                ui.showRelatedBooks(list.result);
            }
        });
    }

    public boolean hasAdded(Book book) {
        return book != null && BookManager.getInstance().contains(book);
    }

}
