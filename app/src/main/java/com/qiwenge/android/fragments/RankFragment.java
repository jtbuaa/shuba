package com.qiwenge.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dev1024.utils.LogUtils;
import com.dev1024.utils.PreferencesUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qiwenge.android.R;
import com.qiwenge.android.act.BookDetailActivity;
import com.qiwenge.android.adapters.BooksAdapter;
import com.qiwenge.android.async.AsyncGetCacheBooks;
import com.qiwenge.android.async.AsyncUtils;
import com.qiwenge.android.async.AsyncGetCacheBooks.CacheBooksHandler;
import com.qiwenge.android.base.BaseFragment;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.models.Book;
import com.qiwenge.android.models.BookList;
import com.qiwenge.android.ui.PagePullToRefreshListView;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.http.JsonResponseHandler;

/**
 * 排行。
 * <p/>
 * Created by John on 2014－5－31
 */
public class RankFragment extends BaseFragment {

    private final String CACHE_RANK = "cache_rank";

    private PagePullToRefreshListView lvMain;

    private List<Book> data = new ArrayList<Book>();

    private BooksAdapter adapter;

    private int pageindex = 1;

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

    private boolean refreshed = false;

    /**
     * 刷新数据。
     */
    public void refresh() {
        if (refreshed) return;
        refreshed = true;
        getRank();
    }

    private void getCacheData() {
        new AsyncGetCacheBooks(getActivity().getApplicationContext(), new CacheBooksHandler() {

            @Override
            public void onSuccess(List<Book> list) {
                adapter.add(list);
                getRank();
            }

            @Override
            public void onEmpty() {
                System.out.println("Rank cache is empty");
                getRank();
            }
        }).execute(CACHE_RANK);
    }

    private void cacheRank(String json) {
        System.out.println("缓存排行");
        PreferencesUtils.putString(getActivity(), Constants.PRE_SAVE_NAME, CACHE_RANK, json);
    }

    private void initViews() {

        adapter = new BooksAdapter(getActivity(), data);
        lvMain = (PagePullToRefreshListView) getView().findViewById(R.id.listview_pull_to_refresh);

        lvMain.setAdapter(adapter);
        lvMain.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 < data.size()) {
                    Bundle extra = new Bundle();
                    extra.putParcelable(BookDetailActivity.EXTRA_BOOK, data.get(position - 1));
                    startActivity(BookDetailActivity.class, extra);
                }
            }
        });

        lvMain.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                pageindex = 1;
                getRank();
                lvMain.reset();
            }
        });

        lvMain.setOnScrollPageListener(new PagePullToRefreshListView.ScrollPageListener() {
            @Override
            public void onPage() {
                pageindex++;
                getRank();
            }
        });

    }


    /**
     * 获取排行。
     */
    private void getRank() {
        String url = ApiUtils.getBooksByTop();
        AsyncUtils.getBooks(url, pageindex, new JsonResponseHandler<BookList>(BookList.class) {

            @Override
            public void onStart() {
                lvMain.loadStart();
            }

            @Override
            public void onOrigin(String json) {
                if (pageindex == 1)
                    cacheRank(json);
            }

            @Override
            public void onSuccess(BookList result) {
                if (result != null) {
                    lvMain.setTotal(result.total);
                    if (pageindex == 1)
                        data.clear();
                    adapter.add(result.result);
                }
            }

            @Override
            public void onFinish() {
                lvMain.loadFinished(adapter.getCount());
            }
        });

    }

}
