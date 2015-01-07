package com.qiwenge.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;
import com.qiwenge.android.R;
import com.qiwenge.android.adapters.BookShelfAdapter;
import com.qiwenge.android.async.AsyncRemoveBook;
import com.qiwenge.android.base.BaseFragment;
import com.qiwenge.android.dao.BookDao;
import com.qiwenge.android.dao.DaoFactory;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.entity.BookUpdateList;
import com.qiwenge.android.ui.dialogs.MyDialog;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.SkipUtils;
import com.qiwenge.android.utils.StyleUtils;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.JsonResponseHandler;

/**
 * 书架Fragment。
 */
public class BookshelfFragment extends BaseFragment {

    private ListView lvBookShelf;

    private List<Book> data = new ArrayList<Book>();

    private BookShelfAdapter adapter;

    private View emptyView;

    private SwipeRefreshLayout mSwipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookshelf, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        mSwipeLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_container);
        StyleUtils.setColorSchemeResources(mSwipeLayout);

        emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty, null);
        ImageView ivEmpty = (ImageView) emptyView.findViewById(R.id.iv_empty);
        ivEmpty.setBackgroundResource(R.drawable.icon_empty_tree);
        adapter = new BookShelfAdapter(getActivity(), data);
        lvBookShelf = (ListView) getView().findViewById(R.id.lv_book_shelf);
        lvBookShelf.setEmptyView(emptyView);
        emptyView.setVisibility(View.GONE);
        lvBookShelf.setAdapter(adapter);
        lvBookShelf.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < data.size()) {
                    Book book = data.get(position);
                    SkipUtils.skipToReader(getActivity(), book);
                }
            }
        });

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                chkBookUpdated();
            }
        });

        lvBookShelf.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showBookDialog(data.get(position));
                return true;
            }
        });
    }

    private void showBookDialog(final Book book) {
        MyDialog myDialog = new MyDialog(getActivity(), book.title);
        String[] items = {"查看详情", "删除"};
        myDialog.setItems(items, new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        SkipUtils.skipToBookDetail(getActivity(), book);
                        break;
                    case 1:
                        deleteBook(book);
                        break;
                }
            }
        });
        myDialog.show();
    }

    /**
     * 检查书架中的书，是否有更新。
     */
    private void chkBookUpdated() {
        StringBuilder bookIds = new StringBuilder();
        StringBuilder chapterTotals = new StringBuilder();
        Book book;
        for (int i = 0; i < data.size(); i++) {
            book = data.get(i);
            if (i > 0) {
                bookIds.append(",");
                chapterTotals.append(",");
            }
            bookIds.append(book.getId());
            chapterTotals.append(book.chapter_total);
        }
        String url = ApiUtils.checkBookUpdate();
        RequestParams params = new RequestParams();
        params.put("books", bookIds.toString());
        params.put("chapter_totals", chapterTotals.toString());
        JHttpClient.get(url, params, new JsonResponseHandler<BookUpdateList>(BookUpdateList.class) {

            @Override
            public void onOrigin(String json) {

            }

            @Override
            public void onFinish() {
                mSwipeLayout.setRefreshing(false);
            }
        });
    }

    private void deleteBook(Book book) {
        new AsyncRemoveBook(getActivity(), null).execute(book);
        data.remove(book);
        adapter.notifyDataSetChanged();
    }

    /**
     * 获取书架内容。
     */
    private void getBooks() {
        new AsyncBookShelfs().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        getBooks();
    }

    /**
     * 异步获取书架内容 AsyncBookShelfs
     * <p/>
     * Created by John on 2014年6月27日
     */
    private class AsyncBookShelfs extends AsyncTask<Void, Integer, List<Book>> {

        @Override
        protected List<Book> doInBackground(Void... params) {
            return DaoFactory.createBookDao(getActivity()).queryAll();
        }

        @Override
        protected void onPostExecute(List<Book> result) {
            if (result != null) {
                data.clear();
                adapter.add(result);
            }
        }
    }

}
