package com.qiwenge.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.liuguangqiang.android.mvp.BaseUi;
import com.liuguangqiang.android.mvp.Presenter;
import com.qiwenge.android.R;
import com.qiwenge.android.adapters.BooksAdapter;
import com.qiwenge.android.base.BaseListFragment;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.mvp.presenter.SearchPresenter;
import com.qiwenge.android.mvp.ui.SearchUi;
import com.qiwenge.android.mvp.ui.SearchUiCallback;

public class SearchFragment extends BaseListFragment<Book> implements SearchUi {

    private String keyword;

    private SearchUiCallback mCallback;

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
    public void setUiCallback(SearchUiCallback searchUiCallback) {
        mCallback = searchUiCallback;
    }

    @Override
    public Presenter setPresenter() {
        return new SearchPresenter(getActivity());
    }

    @Override
    public BaseUi setUi() {
        return this;
    }

    @Override
    public void initViews() {
        super.initViews();
        adapter = new BooksAdapter(getActivity().getApplicationContext(), data);
        setEnableProgressBar();
        setEmptyIcon(R.drawable.ic_empty_search);
        setEmptyMessage(R.string.empty_search);
        setAdapter();
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < data.size()) {
                    mCallback.onItemClick(data.get(position));
                }
            }
        });
    }

    @Override
    public void requestData() {
        super.requestData();
        mCallback.getBooks(pageindex, keyword);
    }

    public void search(String title) {
        keyword = title;
        pageindex = 1;
        data.clear();
        adapter.notifyDataSetChanged();
        requestData();
    }
}
