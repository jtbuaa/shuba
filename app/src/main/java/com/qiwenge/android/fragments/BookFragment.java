package com.qiwenge.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.loopj.android.http.RequestParams;
import com.qiwenge.android.R;
import com.qiwenge.android.act.BookDetailActivity;
import com.qiwenge.android.adapters.BooksAdapter;
import com.qiwenge.android.async.AsyncUtils;
import com.qiwenge.android.base.BaseListFragment;
import com.qiwenge.android.constant.BookStatus;
import com.qiwenge.android.models.Book;
import com.qiwenge.android.models.BookList;
import com.qiwenge.android.ui.PagePullToRefreshListView;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.http.JsonResponseHandler;


public class BookFragment extends BaseListFragment<Book> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        pbLoading = (ProgressBar) getView().findViewById(R.id.pb_loading);
        pbLoading.setVisibility(View.GONE);
        adapter = new BooksAdapter(getActivity(), data);
        mListView = (PagePullToRefreshListView) getView().findViewById(R.id.listview_pull_to_refresh);
        setEnableFooterPage();
        setEnablePullToRefresh();
        setEnableEmptyView();
        setAdapter();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int p = position - 1;
                if (p >= 0 && p < data.size()) {
                    Bundle extra = new Bundle();
                    extra.putParcelable(BookDetailActivity.EXTRA_BOOK, data.get(p));
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
        AsyncUtils.getBooks(url, params, new JsonResponseHandler<BookList>(BookList.class) {

            @Override
            public void onSuccess(BookList result) {
                if (result != null && result.result != null) {
                    requestSuccess(result.result);
                }
            }

            @Override
            public void onFinish() {
                requestFinished();
                pbLoading.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
            }

        });
    }

}


