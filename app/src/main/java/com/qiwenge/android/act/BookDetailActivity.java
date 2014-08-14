package com.qiwenge.android.act;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
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
import com.dev1024.utils.LogUtils;
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
import com.qiwenge.android.utils.ThemeUtils;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.StringResponseHandler;

/**
 * 小说详情。
 * <p/>
 * Created by John on 2014-5-5
 */
public class BookDetailActivity extends BaseActivity implements OnClickListener {

    public static final String EXTRA_BOOK="book";

    private TextView tvTitle;
    private TextView tvIntro;
    private TextView tvAuthor;
    private TextView tvCategory;
    private TextView tvStatus;
    private Button btnChapter;
    private Button btnAdd;
    private ListView lvRecommend;
    private ScrollView scrollView;
    private ImageView ivCover;
    private LinearLayout layoutRelated;
    private LinearLayout layoutIntroTop;
    private LinearLayout layoutIntroCenter;
    private LinearLayout layoutIntroButtons;
    private ImageView ivShadow1;
    private ImageView ivShadow2;
    private ImageView ivShadow3;

    private Book book;
    private List<Book> dataRecommend = new ArrayList<Book>();
    private AboutRmdAdapter adapter;

    /**
     * 是否已经添加到书架。
     */
    private boolean isAdded = false;

    /**
     * 是否需要滑动到顶部。
     * <p/>
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

    @Override
    protected void onResume() {
        super.onResume();
        ThemeUtils.setThemeBg(layoutIntroCenter);
        ThemeUtils.setThemeBg(layoutIntroTop);
        ThemeUtils.setThemeBg(layoutRelated);
        ThemeUtils.setThemeBg(layoutIntroButtons);
        ThemeUtils.setThemeSecondBg(scrollView);

        //Shadow
        ThemeUtils.setThemeLine(ivShadow1);
        ThemeUtils.setThemeLine(ivShadow2);
        ThemeUtils.setThemeLine(ivShadow3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chapter:
                Bundle extra = new Bundle();
                extra.putParcelable(ChapterActivity.EXTRA_BOOK, book);
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
     * <p/>
     * 如果以前阅读过该小说，那继续阅读。
     */
    private void continueRead() {
        String lastReadId =
                BookShelfUtils.getRecordChapterId(getApplicationContext(), book.getId());
        if (StringUtils.isEmptyOrNull(lastReadId)) {
            Bundle extra = new Bundle();
            extra.putParcelable(ChapterActivity.EXTRA_BOOK, book);
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
        if (extra.containsKey(EXTRA_BOOK)) {
            book = extra.getParcelable(EXTRA_BOOK);
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

        layoutIntroCenter = (LinearLayout) this.findViewById(R.id.layout_intro_center);
        layoutIntroTop = (LinearLayout) this.findViewById(R.id.layout_intro_top);
        layoutIntroButtons = (LinearLayout) this.findViewById(R.id.layout_intro_buttons);

        layoutRelated = (LinearLayout) this.findViewById(R.id.layout_related);
        layoutRelated.setVisibility(View.GONE);

        ivShadow1 = (ImageView) this.findViewById(R.id.iv_shadow1);
        ivShadow2 = (ImageView) this.findViewById(R.id.iv_shadow2);
        ivShadow3 = (ImageView) this.findViewById(R.id.iv_shadow3);

        showBookInfo();

        // 相关推荐
        lvRecommend = (ListView) this.findViewById(R.id.gv_recommend);
        adapter = new AboutRmdAdapter(getApplicationContext(), dataRecommend);
        lvRecommend.setAdapter(adapter);
        lvRecommend.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < dataRecommend.size()) {
                    Bundle extra = new Bundle();
                    extra.putParcelable(BookDetailActivity.EXTRA_BOOK, dataRecommend.get(position));
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
     * <p>
     * 计算第一行的高度，得出总给的高度。
     * 这样的做法是因为动态计算ListView的高度的时候，如果用户修改了手机字体的大小，
     * 那么Item的高度，可能就和以前设定的高度不一致了，
     * 所以需要重新计算Item的高度
     * </p>
     */
    private void setRelatedHeight() {

        if(dataRecommend==null) return;

        if (dataRecommend.isEmpty()) return;

        final ViewTreeObserver myTree = lvRecommend.getViewTreeObserver();
        if (myTree.isAlive()) {
            myTree.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int itemHeight = lvRecommend.getMeasuredHeight();
                    int height = itemHeight * dataRecommend.size();
                    LinearLayout.LayoutParams params =
                            new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
                    lvRecommend.setLayoutParams(params);
                    myTree.removeOnGlobalLayoutListener(this);
                }
            });
        }

    }


}
