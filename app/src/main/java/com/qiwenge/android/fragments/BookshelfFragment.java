package com.qiwenge.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
import com.loopj.android.http.RequestParams;
import com.qiwenge.android.R;
import com.qiwenge.android.act.BookDetailActivity;
import com.qiwenge.android.adapters.BookShelfAdapter;
import com.qiwenge.android.adapters.BooksAdapter;
import com.qiwenge.android.base.BaseFragment;
import com.qiwenge.android.listeners.OnFragmentClickListener;
import com.qiwenge.android.models.Book;
import com.qiwenge.android.models.BookList;
import com.qiwenge.android.models.BookUpdateList;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.BookShelfUtils;
import com.qiwenge.android.utils.SkipUtils;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.JsonResponseHandler;

/**
 * 书架Fragment。
 */
public class BookshelfFragment extends BaseFragment {

    /**
     * 是否为编辑模式，如果为true的话，单击书架列表，为选择操作。
     */
    private boolean isEditModel = false;

    private PullToRefreshListView lvBookShelf;

    private List<Book> data = new ArrayList<Book>();

    private TextView tvEmpty;

    private BookShelfAdapter adapter;

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
        adapter = new BookShelfAdapter(getActivity(), data);
        lvBookShelf = (PullToRefreshListView) getView().findViewById(R.id.lv_book_shelf);
        lvBookShelf.setAdapter(adapter);
        lvBookShelf.setVisibility(View.GONE);
        lvBookShelf.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 < data.size()) {

                    if (isEditModel) {
                        //编辑模式
                        selectBook(position - 1);
                        return;
                    }

                    Book book = data.get(position - 1);
                    String lastReadId =
                            BookShelfUtils.getRecordChapterId(getActivity(), book.getId());
                    if (StringUtils.isEmptyOrNull(lastReadId)) {
                        Bundle extra = new Bundle();
                        extra.putParcelable(BookDetailActivity.EXTRA_BOOK, data.get(position - 1));
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
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                chkBookUpdated();
            }
        });

        lvBookShelf.getRefreshableView().setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isEditModel)
                    if (clickListener != null) clickListener.onClick();
                isEditModel = true;
                selectBook(position - 1);
                return true;
            }
        });
    }

    /**
     * 检查书架中的书，是否有更新。
     */
    private void chkBookUpdated() {
        if (data.isEmpty()) return;
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
                lvBookShelf.onRefreshComplete();
            }
        });
    }

    /**
     * 选中一本书
     *
     * @param position
     */
    public void selectBook(int position) {
        if (data.isEmpty()) return;

        if (position > data.size() - 1 || position < 0) return;

        data.get(position).selected = !data.get(position).selected;

        data.get(position).showAnim = true;

        adapter.notifyDataSetChanged();
    }

    /**
     * 清楚所有选中
     */
    public void clearAllSelect() {
        isEditModel = false;
        if (data.isEmpty()) return;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).selected) {
                data.get(i).selected = false;
                data.get(i).showAnim = true;
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 删除选中的Book。
     */
    public void deleteSelected() {
        Context context = getActivity().getApplicationContext();
        List<Book> listRemove = new ArrayList<Book>();
        int total = data.size();
        for (int i = 0; i < total; i++) {
            if (data.get(i).selected) {
                BookShelfUtils.removeBook(context, data.get(i));
                listRemove.add(data.get(i));
            }
        }
        if (!listRemove.isEmpty())
            adapter.remove(listRemove);
        checkIsEmpty();
        clearAllSelect();
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

    private void checkIsEmpty() {
        if (data.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            lvBookShelf.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            lvBookShelf.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 异步获取书架内容 AsyncBookShelfs
     * <p/>
     * Created by John on 2014年6月27日
     */
    private class AsyncBookShelfs extends AsyncTask<Void, Integer, BookList> {

        @Override
        protected BookList doInBackground(Void... params) {
            return BookShelfUtils.getBooks(getActivity().getApplicationContext());
        }

        @Override
        protected void onPostExecute(BookList result) {
            if (result != null) {
                data.clear();
                adapter.add(result.result);
            }
            checkIsEmpty();
        }
    }

}
