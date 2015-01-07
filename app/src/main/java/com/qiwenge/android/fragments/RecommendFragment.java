package com.qiwenge.android.fragments;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;

import com.liuguangqiang.common.utils.PreferencesUtils;
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
 * 推荐。 CategorysFragment
 * <p/>
 * Created by John on 2014年5月31日
 */
public class RecommendFragment extends BaseListFragment<Book> {

    private static final String CACHE_RECOMMEND = "cache_recommend";

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
        if (isAdded())
            PreferencesUtils.putString(getActivity().getApplicationContext(), Constants.PRE_SAVE_NAME, CACHE_RECOMMEND, json);
    }

    @Override
    public void initViews() {
        super.initViews();
        adapter = new BooksAdapter(getActivity(), data);
        setEnableFooterPage();
        setEnablePullToRefresh();
        setEnableEmptyView();
        setAdapter();
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < data.size()) {
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
            }

            @Override
            public void onFinish() {
                requestFinished();
            }
        });
    }

}
