package com.qiwenge.android.act;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liuguangqiang.framework.utils.GsonUtils;
import com.loopj.android.http.RequestParams;
import com.qiwenge.android.R;
import com.qiwenge.android.adapters.ChapterAdapter;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.entity.Chapter;
import com.qiwenge.android.entity.ChapterList;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.BookShelfUtils;
import com.qiwenge.android.utils.SkipUtils;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.StringResponseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 目录。
 * <p/>
 * Created by Eric on 2014-5-5
 */
public class ChapterActivity extends BaseActivity {

    public static final String EXTRA_BOOK = "book";

    private ListView lvChapters;
    private View emptyView;
    private ProgressBar ivLoading;

    private ChapterAdapter adapter;

    private List<Chapter> data = new ArrayList<>();

    private Book book;

    private int pageindex = 1;

    private boolean isInited = false;

    private int lastNumber = -1;

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
        selectedReadNumber();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !isInited) {
            getBookChpaters();
            isInited = true;
        }
    }

    private void getIntentData() {
        Bundle extra = getIntent().getExtras();
        if (extra.containsKey(EXTRA_BOOK)) {
            book = extra.getParcelable(EXTRA_BOOK);
            Log.i("book", "id:" + book.getId());
            setTitle(book.title);
        }
    }

    private void initViews() {
        ivLoading = (ProgressBar) this.findViewById(R.id.pb_loading);
        lvChapters = (ListView) this.findViewById(R.id.listview_common);

        adapter = new ChapterAdapter(this, data);
        lvChapters.setAdapter(adapter);
        lvChapters.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < data.size()) {
                    SkipUtils.skipToReader(getApplicationContext(), book, data.get(position).getId());
                }
            }
        });
    }

    private void showEmptyView() {
        emptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty, null);
        TextView tvEmpty = (TextView) emptyView.findViewById(R.id.tv_empty);
        ImageView ivEmpty = (ImageView) emptyView.findViewById(R.id.iv_empty);
        ivEmpty.setVisibility(View.GONE);
        tvEmpty.setText(R.string.empty_chapter);

        ViewGroup viewGroup = (ViewGroup) lvChapters.getParent();
        viewGroup.addView(emptyView, lvChapters.getLayoutParams());
        lvChapters.setEmptyView(emptyView);
    }

    /**
     * 获取一本书下的，所有章节。
     */
    private void getBookChpaters() {
        String url = ApiUtils.getBookChpaters();
        RequestParams params = new RequestParams();
        params.put("book_id", book.getId());
        params.put("limit", "9999");
        params.put("page", "" + pageindex);
        JHttpClient.get(getApplicationContext(), url, params, new StringResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                ivLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                if (data.isEmpty()) {
                    showEmptyView();
                }
                ivLoading.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(String result) {
                ChapterList list = GsonUtils.getModel(result, ChapterList.class);
                adapter.add(list.result);
                selectedReadNumber();
            }
        });
    }

    private ViewTreeObserver viewTreeObserver;

    private void selectedReadNumber() {
        final int number = BookShelfUtils.getReadNumber(book.getId()) - 1;
        if (number < 0) return;
        if (number > adapter.getCount()) return;
        //改变颜色
        if (lastNumber >= 0 && lastNumber < adapter.getCount() && adapter.get(lastNumber) != null) {
            adapter.get(lastNumber).isSelected = false;
        }
        lastNumber = number;
        if (number >= 0 && number < adapter.getCount() && adapter.get(number) != null) {
            adapter.get(number).isSelected = true;
            adapter.notifyDataSetChanged();
        }

        //定位到阅读到的number，并滚动到中间
        viewTreeObserver = lvChapters.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (viewTreeObserver != null && viewTreeObserver.isAlive()) {

                        try {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                viewTreeObserver.removeGlobalOnLayoutListener(this);
                            } else {
                                viewTreeObserver.removeOnGlobalLayoutListener(this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    int offset = Math.abs(lvChapters.getLastVisiblePosition() - lvChapters.getFirstVisiblePosition());
                    int selectedPostion = number - offset / 2;
                    if (selectedPostion < 0) selectedPostion = 0;

                    lvChapters.setSelection(selectedPostion);
                }
            });
        }

    }
}
