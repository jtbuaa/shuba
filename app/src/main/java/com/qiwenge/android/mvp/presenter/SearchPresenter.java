/*
 * Copyright 2015 qiwenge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qiwenge.android.mvp.presenter;

import android.content.Context;

import com.liuguangqiang.android.mvp.Presenter;
import com.qiwenge.android.app.ReadApplication;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.mvp.model.BookModel;
import com.qiwenge.android.mvp.ui.SearchUi;
import com.qiwenge.android.mvp.ui.SearchUiCallback;
import com.qiwenge.android.utils.SkipUtils;

import javax.inject.Inject;

/**
 * Created by Eric on 15/5/21.
 */
public class SearchPresenter extends Presenter<SearchUi, SearchUiCallback> {

    @Inject
    BookModel mBookModel;

    Context mContext;

    public SearchPresenter(Context context, SearchUi ui) {
        super(ui);
        ReadApplication.from(context).inject(this);
        mContext = context;
    }

    @Override
    protected void populateUi(SearchUi searchUi) {

    }

    @Override
    protected SearchUiCallback createUiCallback(final SearchUi searchUi) {
        return new SearchUiCallback() {
            @Override
            public void getBooks(int pageindex, String keyword) {
                mBookModel.getBooks(mContext, pageindex, keyword, searchUi);
            }

            @Override
            public void onItemClick(Book book) {
                SkipUtils.skipToBookDetail(mContext, book);
            }
        };
    }
}
