package com.qiwenge.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.dev1024.utils.StringUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qiwenge.android.R;
import com.qiwenge.android.act.BookDetailActivity;
import com.qiwenge.android.adapters.BooksAdapter;
import com.qiwenge.android.base.BaseFragment;
import com.qiwenge.android.listeners.OnFragmentClickListener;
import com.qiwenge.android.models.Book;
import com.qiwenge.android.models.BookList;
import com.qiwenge.android.utils.BookShelfUtils;
import com.qiwenge.android.utils.SkipUtils;

public class BookshelfFragment extends BaseFragment {

    private PullToRefreshListView lvBookShelf;

    private List<Book> data = new ArrayList<Book>();

    private TextView tvEmpty;

    private BooksAdapter adapter;

    private OnFragmentClickListener clickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookshelf, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    public void setOnFragmentClickListener(OnFragmentClickListener listener) {
        this.clickListener = listener;
    }

    private void initViews() {
        tvEmpty = (TextView) getView().findViewById(R.id.tv_empty);
        tvEmpty.setVisibility(View.GONE);
        adapter = new BooksAdapter(getActivity(), data);
        lvBookShelf = (PullToRefreshListView) getView().findViewById(R.id.lv_book_shelf);
        lvBookShelf.setAdapter(adapter);
        lvBookShelf.setVisibility(View.GONE);
        lvBookShelf.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 < data.size()) {
                    Book book = data.get(position - 1);
                    String lastReadId =
                            BookShelfUtils.getRecordChapterId(getActivity(), book.getId());
                    if (StringUtils.isEmptyOrNull(lastReadId)) {
                        Bundle extra = new Bundle();
                        extra.putParcelable("book", data.get(position - 1));
                        startActivity(BookDetailActivity.class, extra);
                    } else {
                        book.lastReadId = lastReadId;
                        SkipUtils.skipToReader(getActivity(), book.getId(), book.title,
                                book.lastReadId);
                    }
                }
            }
        });

        lvBookShelf.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {}
        });

        lvBookShelf.getRefreshableView().setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (clickListener != null) clickListener.onClick();
                return true;
            }
        });
    }

    /**
     * 获取书架内容。
     */
    private void getBooks() {
        new AsyncBookShelfs().execute("");
    }

    @Override
    public void onResume() {
        super.onResume();
        getBooks();
    }

    /**
     * 异步获取书架内容 AsyncBookShelfs
     * 
     * Created by John on 2014年6月27日
     */
    private class AsyncBookShelfs extends AsyncTask<String, Integer, BookList> {

        @Override
        protected BookList doInBackground(String... params) {
            return BookShelfUtils.getBooks(getActivity().getApplicationContext());
        }

        @Override
        protected void onPostExecute(BookList result) {
            if (result != null) {
                data.clear();
                data.addAll(result.result);
                adapter.notifyDataSetChanged();
                if (data.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE);
                    lvBookShelf.setVisibility(View.GONE);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                    lvBookShelf.setVisibility(View.VISIBLE);
                }
            }
        }
    }


}
