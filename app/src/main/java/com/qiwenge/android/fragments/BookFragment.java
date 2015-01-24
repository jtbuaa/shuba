package com.qiwenge.android.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.loopj.android.http.RequestParams;
import com.qiwenge.android.act.BookDetailActivity;
import com.qiwenge.android.adapters.BooksAdapter;
import com.qiwenge.android.async.AsyncUtils;
import com.qiwenge.android.base.BaseListFragment;
import com.qiwenge.android.constant.BookStatus;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.entity.BookList;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.http.JsonResponseHandler;

public class BookFragment extends BaseListFragment<Book> {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    @Override
    public void initViews() {
        super.initViews();
        adapter = new BooksAdapter(getActivity(), data);
        setEnableProgressBar();
        setEnableFooterPage();
        setEnablePullToRefresh();
        setEnableEmptyView();
        setAdapter();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < data.size()) {
                    Bundle extra = new Bundle();
                    extra.putParcelable(BookDetailActivity.EXTRA_BOOK, data.get(position));
                    startActivity(BookDetailActivity.class, extra);
                }
            }
        });
    }

    @Override
    public void requestData() {
        super.requestData();
        getBooks();
    }

    private String searchCategory;

    private String searchKeyword;

    public void search(String category) {
        searchCategory = category;
        requestData();
    }

    private void getBooks() {
        String url = ApiUtils.getBooks();
        RequestParams params = new RequestParams();
        params.put("status", "" + BookStatus.APPROVED);
        params.put("page", "" + pageindex);
        if (searchKeyword != null) {
            params.put("title", searchKeyword);
        }
        if (searchCategory != null) {
            params.put("categories", searchCategory);
        }
        AsyncUtils.getBooks(getActivity(), url, params, new JsonResponseHandler<BookList>(BookList.class) {

            @Override
            public void onSuccess(BookList result) {
                if (result != null && result.result != null) {
                    requestSuccess(result.result);
                }
            }

            @Override
            public void onFinish() {
                requestFinished();
            }

        });
    }

}


