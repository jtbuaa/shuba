package com.qiwenge.android.act;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dev1024.utils.DialogUtils;
import com.dev1024.utils.DisplayUtils;
import com.dev1024.utils.GsonUtils;
import com.dev1024.utils.StringUtils;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiwenge.android.R;
import com.qiwenge.android.adapters.AboutRmdAdapter;
import com.qiwenge.android.async.AsyncAddBook;
import com.qiwenge.android.async.AsyncUtils;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.constant.BookStatus;
import com.qiwenge.android.listeners.CommonHandler;
import com.qiwenge.android.models.Book;
import com.qiwenge.android.models.BookList;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.BookShelfUtils;
import com.qiwenge.android.utils.ImageLoaderUtils;
import com.qiwenge.android.utils.ReaderUtils;
import com.qiwenge.android.utils.SkipUtils;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.StringResponseHandler;

/**
 * 小说详情。
 * 
 * Created by John on 2014-5-5
 */
public class BookDetailActivity extends BaseActivity implements OnClickListener {

    private TextView tvTitle;
    private TextView tvIntro;
    private TextView tvAuthor;
    private TextView tvCategory;
    private TextView tvStatus;
    private Button btnChapter;
    private Button btnAdd;
    private ListView gvRecommend;
    private ScrollView scrollView;
    private ImageView ivCover;
    private LinearLayout layoutRelated;

    private Book book;
    private List<Book> dataRecommend = new ArrayList<Book>();
    private AboutRmdAdapter adapter;

    /**
     * 是否已经添加到书架。
     */
    private boolean isAdded = false;

    /**
     * 是否需要滑动到顶部。
     * <p>
     * 初始化时，ScrollView滑动到顶部。
     */
    private boolean scrollToTop = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_intro);
        setTitle(R.string.book_intro_detail);
        getIntentData();
        initViews();
        getRelated();
    }


    // private HomeAwayShareProvider mShareActionProvider;

    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {
    // getMenuInflater().inflate(R.menu.action_book_detail, menu);
    // MenuItem item = menu.findItem(R.id.menu_item_share);
    // mShareActionProvider = (HomeAwayShareProvider) item.getActionProvider();
    //
    // setShareIntent();
    // return super.onCreateOptionsMenu(menu);
    // }

    // private void setShareIntent() {
    // if (mShareActionProvider != null) {
    // Intent shareIntent = new Intent(Intent.ACTION_SEND);
    // shareIntent.putExtra(Intent.EXTRA_TEXT, "testest");
    // shareIntent.setType("text/plain");
    // mShareActionProvider.setShareIntent(shareIntent);
    // }
    // }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chapter:
                Bundle extra = new Bundle();
                extra.putParcelable("book", book);
                startActivity(ChapterActivity.class, extra);
                break;
            case R.id.btn_add:
                if (isAdded) {
                    continueRead();
                } else {
                    addBook();
                    btnAdd.setText(R.string.book_intro_read_continue);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 继续阅读
     * <p>
     * 如果以前阅读过该小说，那继续阅读。
     */
    private void continueRead() {
        String lastReadId =
                BookShelfUtils.getRecordChapterId(getApplicationContext(), book.getId());
        if (StringUtils.isEmptyOrNull(lastReadId)) {
            Bundle extra = new Bundle();
            extra.putParcelable("book", book);
            startActivity(ChapterActivity.class, extra);
        } else {
            book.lastReadId = lastReadId;
            SkipUtils.skipToReader(getApplicationContext(), book.getId(), book.title,
                    book.lastReadId);
        }
    }

    /**
     * 添加小说到书架
     */
    private void addBook() {
        new AsyncAddBook(getApplicationContext(), new CommonHandler() {
            @Override
            public void onStart() {
                DialogUtils.showLoading(BookDetailActivity.this);
            }

            @Override
            public void onSuccess() {
                isAdded = true;
                DialogUtils.hideLoading();
            }

        }).execute(book);
    }

    private void getIntentData() {
        Bundle extra = getIntent().getExtras();
        if (extra.containsKey("book")) {
            book = extra.getParcelable("book");
            AsyncUtils.postViewTotal(book.getId());
        }
    }

    private void initViews() {
        scrollView = (ScrollView) this.findViewById(R.id.scrollView_container);
        ivCover = (ImageView) this.findViewById(R.id.iv_cover);
        tvTitle = (TextView) this.findViewById(R.id.tv_title);
        tvIntro = (TextView) this.findViewById(R.id.tv_intro);
        tvAuthor = (TextView) this.findViewById(R.id.tv_author);
        tvCategory = (TextView) this.findViewById(R.id.tv_category);
        tvStatus = (TextView) this.findViewById(R.id.tv_status);
        btnChapter = (Button) this.findViewById(R.id.btn_chapter);
        btnChapter.setOnClickListener(this);
        btnAdd = (Button) this.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        layoutRelated = (LinearLayout) this.findViewById(R.id.layout_related);
        layoutRelated.setVisibility(View.GONE);
        showBookInfo();

        // 相关推荐
        gvRecommend = (ListView) this.findViewById(R.id.gv_recommend);
        adapter = new AboutRmdAdapter(getApplicationContext(), dataRecommend);
        gvRecommend.setAdapter(adapter);
        gvRecommend.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < dataRecommend.size()) {
                    Bundle extra = new Bundle();
                    extra.putParcelable("book", dataRecommend.get(position));
                    startActivity(BookDetailActivity.class, extra);
                    finish();
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && scrollToTop) {
            scrollToTop = false;
            scrollView.scrollTo(0, 0);
        }
    }

    private void showBookInfo() {
        if (book != null) {
            tvTitle.setText(book.title);

            if (BookShelfUtils.contains(getApplicationContext(), book)) {
                btnAdd.setText(R.string.book_intro_read_continue);
                isAdded = true;
            }

            tvIntro.setText(ReaderUtils.formatContent(book.description));
            tvAuthor.setText(String.format(getString(R.string.book_intro_author_format),
                    book.author));

            if (book.categories != null)
                tvCategory.setText(String.format(getString(R.string.book_intro_category_format),
                        book.categories.get(0)));
            else
                tvCategory.setText(String.format(getString(R.string.book_intro_category_format),
                        getString(R.string.str_unknow)));

            if (book.finish == 1) {
                tvStatus.setText(String.format(getString(R.string.book_intro_status_format),
                        getString(R.string.str_book_finish)));
            } else {
                tvStatus.setText(String.format(getString(R.string.book_intro_status_format),
                        getString(R.string.str_book_publishing)));
            }

            ImageLoaderUtils.display(book.cover, ivCover, null);
        }
    }

    /**
     * 获取相关推荐。
     */
    private void getRelated() {
        System.out.println("getRelated");
        if (book != null && !StringUtils.isEmptyOrNull(book.getId())) {
            String url = ApiUtils.getRelated(book.getId());
            RequestParams params = new RequestParams();
            params.put("limit", "4");
            params.put("status", "" + BookStatus.APPROVED);
            JHttpClient.get(url, params, new StringResponseHandler() {
                @Override
                public void onSuccess(String result) {
                    BookList list = GsonUtils.getModel(result, BookList.class);
                    if (adapter != null) adapter.add(list.result);
                    setRelatedHeight();
                }

                @Override
                public void onFinish() {
                    if (!dataRecommend.isEmpty()) {
                        layoutRelated.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    /**
     * 设置相关推荐的高度。
     */
    private void setRelatedHeight() {
        int height = DisplayUtils.dip2px(getApplicationContext(), 90) * dataRecommend.size();
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
        gvRecommend.setLayoutParams(params);
    }


}
