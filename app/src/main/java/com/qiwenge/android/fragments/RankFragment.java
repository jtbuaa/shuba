package com.qiwenge.android.fragments;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;

import com.dev1024.utils.PreferencesUtils;
import com.qiwenge.android.R;
import com.qiwenge.android.act.BookDetailActivity;
import com.qiwenge.android.adapters.BooksAdapter;
import com.qiwenge.android.async.AsyncGetCacheBooks;
import com.qiwenge.android.async.AsyncUtils;
import com.qiwenge.android.async.AsyncGetCacheBooks.CacheBooksHandler;
import com.qiwenge.android.base.BaseListFragment;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.entity.BookList;
import com.qiwenge.android.ui.PagePullToRefreshListView;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.http.JsonResponseHandler;

/**
 * 排行。
 * <p/>
 * Created by John on 2014－5－31
 */
public class RankFragment extends BaseListFragment<Book> {

    private final String CACHE_RANK = "cache_rank";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_recommend, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEmptyView();
        initViews();
        getCacheData();
    }

    private void initEmptyView() {

    }

    private void getCacheData() {
        new AsyncGetCacheBooks(getActivity().getApplicationContext(), new CacheBooksHandler() {

            @Override
            public void onSuccess(List<Book> list) {
                adapter.add(list);
                requestData();
            }

            @Override
            public void onEmpty() {
                System.out.println("Rank cache is empty");
                requestData();
            }
        }).execute(CACHE_RANK);
    }

    private void cacheRank(String json) {
        if (isAdded())
            PreferencesUtils.putString(getActivity().getApplicationContext(), Constants.PRE_SAVE_NAME, CACHE_RANK, json);
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
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 < data.size()) {
                    Bundle extra = new Bundle();
                    extra.putParcelable(BookDetailActivity.EXTRA_BOOK, data.get(position - 1));
                    startActivity(BookDetailActivity.class, extra);
                }
            }
        });

    }

    @Override
    public void requestData() {
        super.requestData();
        getRank();
    }

    /**
     * 获取排行。
     */
    private void getRank() {
        String url = ApiUtils.getBooksByTop();
        AsyncUtils.getBooks(url, pageindex, new JsonResponseHandler<BookList>(BookList.class) {

            @Override
            public void onOrigin(String json) {
                if (pageindex == 1)
                    cacheRank(json);
            }

            @Override
            public void onSuccess(BookList result) {
                if (result != null) {
                    requestSuccess(result.result);
                }
            }

            @Override
            public void onFailure(String msg) {
                requestFailure();
            }

            @Override
            public void onStart() {
                super.onStart();
                if (data.isEmpty()) pbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                requestFinished();
                pbLoading.setVisibility(View.GONE);
            }
        });

    }

}
