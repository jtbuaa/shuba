package com.qiwenge.android.act;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev1024.utils.GsonUtils;
import com.loopj.android.http.RequestParams;
import com.qiwenge.android.R;
import com.qiwenge.android.adapters.ChapterAdapter;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.models.Book;
import com.qiwenge.android.models.Chapter;
import com.qiwenge.android.models.ChapterList;
import com.qiwenge.android.ui.ScrollPageListView;
import com.qiwenge.android.ui.ScrollPageListView.ScrollPageListener;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.ThemeUtils;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.StringResponseHandler;

/**
 * 目录。
 * <p/>
 * Created by John on 2014-5-5
 */
public class ChapterActivity extends BaseActivity {

    private ScrollPageListView lv;
    private TextView tvEmpty;
    private RelativeLayout layoutContainer;

    private ChapterAdapter adapter;

    private List<Chapter> list = new ArrayList<Chapter>();

    private Book book;

    private String bookId;

    private String bookTitle;

    private int pageindex = 1;

    public static final String EXTRA_BOOK = "book";

    public static final String EXTRA_BOOK_ID = "book_id";

    public static final String EXTRA_BOOK_TITLE = "book_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        initViews();
        getIntentData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ThemeUtils.setThemeBg(layoutContainer);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) getBookChpaters();
    }

    private void getIntentData() {
        Bundle extra = getIntent().getExtras();
        if (extra.containsKey(EXTRA_BOOK)) {
            book = extra.getParcelable(EXTRA_BOOK);
            bookId = book.getId();
            setTitle(book.title);
        } else if (extra.containsKey(EXTRA_BOOK_ID) && extra.containsKey(EXTRA_BOOK_TITLE)) {
            bookId = extra.getString(EXTRA_BOOK_ID);
            bookTitle = extra.getString(EXTRA_BOOK_TITLE);
            setTitle(bookTitle);
        }
    }

    private void initViews() {
        layoutContainer = (RelativeLayout) this.findViewById(R.id.layout_container);
        tvEmpty = (TextView) this.findViewById(R.id.tv_empty);
        tvEmpty.setVisibility(View.GONE);
        adapter = new ChapterAdapter(this, list);
        lv = (ScrollPageListView) this.findViewById(R.id.listview_common);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < list.size()) {
                    Bundle extra = new Bundle();
                    extra.putString(ReadActivity.Extra_BookId, bookId);
                    extra.putString(ReadActivity.Extra_ChapterId, list.get(position).getId());
                    extra.putString(ReadActivity.Extra_BookTitle, list.get(position).book_title);
                    startActivity(ReadActivity.class, extra);
                }
            }
        });

        lv.setOnScrollPageListener(new ScrollPageListener() {

            @Override
            public void onPage() {
                pageindex++;
                getBookChpaters();
            }
        });

    }

    /**
     * 获取一本书下的，所有章节。
     */
    private void getBookChpaters() {
        if (bookId == null) return;
        String url = ApiUtils.getBookChpaters();
        RequestParams params = new RequestParams();
        params.put("book_id", bookId);
        params.put("limit", "30");
        params.put("page", "" + pageindex);
        JHttpClient.get(url, params, new StringResponseHandler() {

            @Override
            public void onStart() {
                lv.loadStart();
            }

            @Override
            public void onFinish() {
                lv.loadFinished(adapter.getCount());
                if (list.isEmpty()) {
                    lv.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSuccess(String result) {
                ChapterList list = GsonUtils.getModel(result, ChapterList.class);
                lv.setTotal(list.total);
                adapter.add(list.result);
            }

        });

    }

}
