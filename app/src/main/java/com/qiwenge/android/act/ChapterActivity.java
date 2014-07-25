package com.qiwenge.android.act;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.StringResponseHandler;

/**
 * 目录。
 * 
 * Created by John on 2014-5-5
 */
public class ChapterActivity extends BaseActivity {

    private ScrollPageListView lv;
    private View pagerFooter;
    private TextView tvEmpty;

    private ChapterAdapter adapter;

    private List<Chapter> list = new ArrayList<Chapter>();

    private Book book;

    private int pageindex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        initViews();
        getIntentData();
        getBookChpaters();
    }

    private void getIntentData() {
        Bundle extra = getIntent().getExtras();
        if (extra.containsKey("book")) {
            book = extra.getParcelable("book");
            setTitle(book.title);
        }
    }

    private void initViews() {
        tvEmpty = (TextView) this.findViewById(R.id.tv_empty);
        tvEmpty.setVisibility(View.GONE);
        pagerFooter =
                LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_pager_footer,
                        null);
        adapter = new ChapterAdapter(this, list);
        lv = (ScrollPageListView) this.findViewById(R.id.listview_common);
        lv.addFooterView(pagerFooter);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < list.size()) {
                    Bundle extra = new Bundle();
                    extra.putString(ReadActivity.BookId, book.getId());
                    extra.putString(ReadActivity.ChapterId, list.get(position).getId());
                    extra.putString(ReadActivity.BookTitle, list.get(position).book_title);
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
        if (book == null) return;
        String url = ApiUtils.getBookChpaters();
        RequestParams params = new RequestParams();
        params.put("book_id", book.getId());
        params.put("limit", "20");
        params.put("page", "" + pageindex);
        JHttpClient.get(url, params, new StringResponseHandler() {

            @Override
            public void onStart() {
                lv.setIsLoading(true);
            }

            @Override
            public void onFinish() {
                lv.setIsLoading(false);
                if (list.isEmpty()) {
                    lv.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSuccess(String result) {
                ChapterList list = GsonUtils.getModel(result, ChapterList.class);
                adapter.add(list.result);
            }

        });

    }

}
