package com.qiwenge.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.loopj.android.http.RequestParams;
import com.qiwenge.android.R;
import com.qiwenge.android.act.BookDetailActivity;
import com.qiwenge.android.adapters.BooksAdapter;
import com.qiwenge.android.async.AsyncUtils;
import com.qiwenge.android.base.BaseListFragment;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.entity.BookList;
import com.qiwenge.android.ui.PagePullToRefreshListView;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.http.JsonResponseHandler;

/**
 * 搜索。
 * <p/>
 * Created by John on 2014-7-6
 */
public class SearchFragment extends BaseListFragment<Book> {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base, container, false);
        return rootView;
    }

    @Override
    public void initViews() {
        super.initViews();
        adapter = new BooksAdapter(getActivity().getApplicationContext(), data);
        setDisablePullToRefresh();
        setEnableFooterPage();
        setEnableProgressBar();
        setEnableEmptyView();
        setEmptyIcon(R.drawable.icon_empty_search);
        setEmptyMessage(R.string.empty_search);
        setAdapter();
        mListView.setOnItemClickListener(new OnItemClickListener() {

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
        searchBook();
    }

    private String keyword = "";

    public void search(String title) {
        keyword = title;
        pageindex = 1;
        data.clear();
        adapter.notifyDataSetChanged();
        requestData();
    }

    private void searchBook() {
        System.out.println("search book");
        String url = ApiUtils.getBooks();
        RequestParams params = new RequestParams();
        params.put("page", "" + pageindex);
        params.put("title", keyword);
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
            }

        });
    }

}
