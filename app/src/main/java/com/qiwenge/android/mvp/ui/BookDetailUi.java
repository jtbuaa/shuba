package com.qiwenge.android.mvp.ui;

import com.liuguangqiang.android.mvp.BaseUi;
import com.qiwenge.android.entity.Book;

import java.util.List;

/**
 * Created by Eric on 15/5/7.
 */
public interface BookDetailUi extends BaseUi<BookDetailUiCallback> {

    void showRelatedBooks(List<Book> books);

    void setAddBtnEnable(boolean enable);

    void showBookStatus(boolean isAdded);

}
