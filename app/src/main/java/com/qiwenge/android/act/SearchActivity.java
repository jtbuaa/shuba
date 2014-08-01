package com.qiwenge.android.act;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.loopj.android.http.RequestParams;
import com.qiwenge.android.R;
import com.qiwenge.android.adapters.BooksAdapter;
import com.qiwenge.android.async.AsyncUtils;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.constant.BookStatus;
import com.qiwenge.android.models.Book;
import com.qiwenge.android.models.BookList;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.LoadAnim;
import com.qiwenge.android.utils.ThemeUtils;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.JsonResponseHandler;

/**
 * 搜索。
 * <p/>
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
    private RelativeLayout layoutContainer;
    private RelativeLayout layoutTop;

    private List<Book> data = new ArrayList<Book>();
    private BooksAdapter adapter;


    private ImageView ivLoading;

    private LoadAnim mLoadAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
        getIntentExtras();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ThemeUtils.setThemeBg(layoutContainer);
        ThemeUtils.setThemeSecondBg(layoutTop);
    }

    private void initViews() {
        layoutTop = (RelativeLayout) this.findViewById(R.id.layout_top);
        layoutContainer = (RelativeLayout) this.findViewById(R.id.layout_container);
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

        ivLoading = (ImageView) this.findViewById(R.id.iv_loading);

        mLoadAnim = new LoadAnim(ivLoading);
        mLoadAnim.start();

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
            public void onStart() {
            }

            @Override
            public void onSuccess(BookList result) {
                if (result != null && result.result != null) {
                    data.clear();
                    adapter.add(result.result);
                }
            }

            @Override
            public void onFinish() {
                if (data.isEmpty())
                    getRecommend();
                else {
                    mLoadAnim.cancel();
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
            public void onFinish() {
                mLoadAnim.cancel();
            }
        });
    }

}
