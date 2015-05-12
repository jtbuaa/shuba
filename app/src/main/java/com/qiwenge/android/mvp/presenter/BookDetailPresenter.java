package com.qiwenge.android.mvp.presenter;

import android.content.Context;

import com.liuguangqiang.android.mvp.Presenter;
import com.qiwenge.android.app.ReadApplication;
import com.qiwenge.android.async.AsyncAddBook;
import com.qiwenge.android.async.AsyncRemoveBook;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.listeners.CommonHandler;
import com.qiwenge.android.mvp.model.BookDetailModel;
import com.qiwenge.android.mvp.ui.BookDetailUi;
import com.qiwenge.android.mvp.ui.BookDetailUiCallback;

import javax.inject.Inject;

/**
 * Created by Eric on 15/5/7.
 */
public class BookDetailPresenter extends Presenter<BookDetailUi, BookDetailUiCallback> {

    @Inject
    BookDetailModel bookDetailModel;

    private Context mContext;

    public BookDetailPresenter(Context context) {
        mContext = context;
        ReadApplication.from(context).inject(this);
    }

    @Override
    protected void populateUi(BookDetailUi bookDetailUi) {
    }

    @Override
    protected BookDetailUiCallback createUiCallback(final BookDetailUi bookDetailUi) {
        return new BookDetailUiCallback() {

            @Override
            public void addOrRemove(Book book) {
                if (bookDetailModel.hasAdded(book)) {
                    remove(book, bookDetailUi);
                } else {
                    add(book, bookDetailUi);
                }
            }

            @Override
            public void checkAdded(Book book) {
                bookDetailUi.showBookStatus(bookDetailModel.hasAdded(book));
            }

            @Override
            public void getRelatedBooks(String bookId) {
                bookDetailModel.getRelatedBooks(bookId, bookDetailUi);
            }
        };
    }

    private void add(Book book, final BookDetailUi ui) {
        ui.setAddBtnEnable(false);
        new AsyncAddBook(mContext, new CommonHandler() {

            @Override
            public void onSuccess() {
                ui.setAddBtnEnable(true);
                ui.showBookStatus(true);
            }
        }).execute(book);
    }

    private void remove(Book book, final BookDetailUi ui) {
        ui.setAddBtnEnable(false);
        new AsyncRemoveBook(mContext, new CommonHandler() {

            @Override
            public void onSuccess() {
                ui.setAddBtnEnable(true);
                ui.showBookStatus(false);
            }
        }).execute(book);
    }

}
