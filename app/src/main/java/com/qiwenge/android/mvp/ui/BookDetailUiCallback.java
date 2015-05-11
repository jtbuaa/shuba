package com.qiwenge.android.mvp.ui;

import com.qiwenge.android.entity.Book;

/**
 * Created by Eric on 15/5/7.
 */
public interface BookDetailUiCallback {

    void getRelatedBooks(String bookId);

    void checkAdded(Book book);

    void addOrRemove(Book book);

}
