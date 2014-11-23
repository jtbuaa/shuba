package com.qiwenge.android.fragments;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.loopj.android.http.RequestParams;
import com.qiwenge.android.R;
import com.qiwenge.android.act.BookDetailActivity;
import com.qiwenge.android.adapters.BooksAdapter;
import com.qiwenge.android.async.AsyncUtils;
import com.qiwenge.android.base.BaseFragment;
import com.qiwenge.android.base.BaseListFragment;
import com.qiwenge.android.models.Book;
import com.qiwenge.android.models.BookList;
import com.qiwenge.android.ui.PagePullToRefreshListView;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.ThemeUtils;
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

    private void initViews() {
        adapter = new BooksAdapter(getActivity().getApplicationContext(), data);
        mListView = (PagePullToRefreshListView) getView().findViewById(R.id.listview_pull_to_refresh);
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
                pbLoading.setVisibility(View.GONE);
            }

        });
    }

}
