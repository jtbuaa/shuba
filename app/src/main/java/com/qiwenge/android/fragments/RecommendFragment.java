package com.qiwenge.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dev1024.utils.PreferencesUtils;
import com.qiwenge.android.R;
import com.qiwenge.android.act.BookDetailActivity;
import com.qiwenge.android.adapters.BooksAdapter;
import com.qiwenge.android.async.AsyncGetCacheBooks;
import com.qiwenge.android.async.AsyncUtils;
import com.qiwenge.android.async.AsyncGetCacheBooks.CacheBooksHandler;
import com.qiwenge.android.base.BaseListFragment;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.models.Book;
import com.qiwenge.android.models.BookList;
import com.qiwenge.android.ui.PagePullToRefreshListView;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.http.JsonResponseHandler;

/**
 * 推荐。 CategorysFragment
 * <p/>
 * Created by John on 2014年5月31日
 */
public class RecommendFragment extends BaseListFragment<Book> {

    private static final String CACHE_RECOMMEND = "cache_recommend";

    private ProgressBar pbLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_recommend, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        getCacheData();
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
                System.out.println("Recommend cache is empty");
                requestData();
            }
        }).execute(CACHE_RECOMMEND);
    }

    private void cacheRecommend(String json) {
        System.out.println("缓存推荐");
        PreferencesUtils.putString(getActivity(), Constants.PRE_SAVE_NAME, CACHE_RECOMMEND, json);
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
        getRecommend();
    }

    /**
     * 获取推荐的书籍。
     */
    private void getRecommend() {
        String url = ApiUtils.getRecommend();
        AsyncUtils.getBooks(url, pageindex, new JsonResponseHandler<BookList>(BookList.class) {
            @Override
            public void onOrigin(String json) {
                if (pageindex == 1)
                    cacheRecommend(json);
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
                if (data.isEmpty()) {
                    pbLoading.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFinish() {
                requestFinished();
                pbLoading.setVisibility(View.GONE);
            }
        });
    }

}
