package com.qiwenge.android.act;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.loopj.android.http.RequestParams;
import com.qiwenge.android.R;
import com.qiwenge.android.adapters.BooksAdapter;
import com.qiwenge.android.async.AsyncUtils;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.constant.BookStatus;
import com.qiwenge.android.models.Book;
import com.qiwenge.android.models.BookList;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.JsonResponseHandler;

/**
 * 搜索。
 * 
 * Created by John on 2014-7-6
 */
public class SearchActivity extends BaseActivity {

    /**
     * 分类查询
     */
    public static final String CATEGORY = "category";

    /**
     * 关键字查询
     */
    public static final String KEYWORD = "keyword";

    private String searchKeyword;
    private String searchCategory;

    private ListView lv;
    private LinearLayout layoutNoResult;

    private List<Book> data = new ArrayList<Book>();
    private BooksAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
        getIntentExtras();
    }

    private void initViews() {
        layoutNoResult = (LinearLayout) this.findViewById(R.id.layout_no_result);
        layoutNoResult.setVisibility(View.GONE);
        adapter = new BooksAdapter(getApplicationContext(), data);
        lv = (ListView) this.findViewById(R.id.listview_common);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle extra = new Bundle();
                extra.putParcelable("book", data.get(position));
                startActivity(BookDetailActivity.class, extra);
            }
        });

    }


    private void getIntentExtras() {
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            if (extra.containsKey(KEYWORD)) {
                searchKeyword = extra.getString(KEYWORD);
                setTitle(searchKeyword);
            }

            if (extra.containsKey(CATEGORY)) {
                searchCategory = extra.getString(CATEGORY);
                setTitle(searchCategory);
            }

            getBooks();
        }
    }

    private void getBooks() {
        String url = ApiUtils.getBooks();
        RequestParams params = new RequestParams();
        params.put("status", "" + BookStatus.APPROVED);
        if (searchKeyword != null) {
            params.put("title", searchKeyword);
        }
        if (searchCategory != null) {
            params.put("categories", searchCategory);
        }
        AsyncUtils.getBooks(url, params, new JsonResponseHandler<BookList>(BookList.class) {

            @Override
            public void onStart() {}

            @Override
            public void onSuccess(BookList result) {
                if (result != null && result.result != null) {
                    data.clear();
                    adapter.add(result.result);
                }
            }

            @Override
            public void onFinish() {
                if (data.isEmpty()) {
                    getRecommend();
                }
            }

        });
    }

    private void getRecommend() {
        String url = ApiUtils.getRecommend();
        System.out.println("getRecommend:" + url);
        JHttpClient.get(url, null, new JsonResponseHandler<BookList>(BookList.class) {

            @Override
            public void onSuccess(BookList result) {
                if (result != null) {
                    layoutNoResult.setVisibility(View.VISIBLE);
                    data.clear();
                    adapter.add(result.result);
                }
            }

            @Override
            public void onFinish() {}
        });
    }

}
