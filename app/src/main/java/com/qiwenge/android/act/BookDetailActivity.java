package com.qiwenge.android.act;

import android.os.Build;
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
import android.widget.TextView;

import com.liuguangqiang.android.mvp.BaseUi;
import com.liuguangqiang.android.mvp.Presenter;
import com.liuguangqiang.common.utils.DisplayUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiwenge.android.R;
import com.qiwenge.android.adapters.AboutRmdAdapter;
import com.qiwenge.android.async.AsyncUtils;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.mvp.presenter.BookDetailPresenter;
import com.qiwenge.android.mvp.ui.BookDetailUi;
import com.qiwenge.android.mvp.ui.BookDetailUiCallback;
import com.qiwenge.android.utils.ImageLoaderUtils;
import com.qiwenge.android.utils.ReaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 小说详情。
 * <p/>
 * Created by Eric on 2014-5-5
 */
public class BookDetailActivity extends BaseActivity implements OnClickListener, BookDetailUi {

    public static final String EXTRA_BOOK = "book";

    private TextView tvTitle;
    private TextView tvIntro;
    private TextView tvAuthor;
    private TextView tvCategory;
    private TextView tvStatus;
    private Button btnChapter;
    private Button btnAdd;
    private ListView lvRecommend;
    private ImageView ivCover;
    private LinearLayout layoutRelated;

    private Book book;
    private List<Book> dataRecommend = new ArrayList<Book>();
    private AboutRmdAdapter adapter;

    private boolean hasInited = false;

    private BookDetailUiCallback mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_intro);
        setTitle(R.string.book_intro_detail);
        initViews();
        getIntentData();
    }

    @Override
    public Presenter setPresenter() {
        return new BookDetailPresenter(this);
    }

    @Override
    public BaseUi setUi() {
        return this;
    }

    @Override
    public void setUiCallback(BookDetailUiCallback bookDetailUiCallback) {
        mCallback = bookDetailUiCallback;
    }

    private void initViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvIntro = (TextView) findViewById(R.id.tv_intro);
        tvAuthor = (TextView) findViewById(R.id.tv_author);
        tvCategory = (TextView) findViewById(R.id.tv_category);
        tvStatus = (TextView) findViewById(R.id.tv_status);

        btnAdd = (Button) findViewById(R.id.btn_add);
        ivCover = (ImageView) findViewById(R.id.iv_cover);

        btnChapter = (Button) findViewById(R.id.btn_chapter);
        btnChapter.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        layoutRelated = (LinearLayout) findViewById(R.id.layout_related);
        layoutRelated.setVisibility(View.GONE);

        // 相关推荐
        lvRecommend = (ListView) findViewById(R.id.lv_recommend);
        adapter = new AboutRmdAdapter(getApplicationContext(), dataRecommend);
        lvRecommend.setAdapter(adapter);
        lvRecommend.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < dataRecommend.size()) {
                    Bundle extra = new Bundle();
                    extra.putParcelable(BookDetailActivity.EXTRA_BOOK, dataRecommend.get(position));
                    startActivity(BookDetailActivity.class, extra);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCallback.checkAdded(book);
    }

    @Override
    public void showBookStatus(boolean isAdded) {
        if (isAdded) {
            showRemoveBtn();
        } else {
            showAddBtn();
        }
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
                mCallback.addOrRemove(book);
                break;
            default:
                break;
        }
    }

    private void getIntentData() {
        Bundle extra = getIntent().getExtras();
        if (extra.containsKey(EXTRA_BOOK)) {
            book = extra.getParcelable(EXTRA_BOOK);
            AsyncUtils.postViewTotal(book.getId());
            showBookInfo();
        }
    }

    @Override
    public void setAddBtnEnable(boolean enable) {
        btnAdd.setEnabled(enable);
    }

    private void showAddBtn() {
        btnAdd.setText(R.string.book_intro_add);
        btnAdd.setBackgroundResource(R.drawable.btn_hollow_yellow);
        btnAdd.setTextColor(getResources().getColorStateList(R.color.btn_yellow_text_color));
    }

    private void showRemoveBtn() {
        btnAdd.setText(R.string.book_intro_remove);
        btnAdd.setBackgroundResource(R.drawable.btn_remove_book);
        btnAdd.setTextColor(getResources().getColorStateList(R.color.btn_remove_text_color));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !hasInited) {
            hasInited = true;
            mCallback.getRelatedBooks(book.id);
        }
    }

    private void showBookInfo() {
        if (book != null) {
            tvTitle.setText(book.title.trim());

            tvIntro.setText(ReaderUtils.formatDesc(book.description));
            tvAuthor.setText(String.format(getString(R.string.book_intro_author_format),
                    book.author.trim()));

            if (book.categories != null)
                tvCategory.setText(String.format(getString(R.string.book_intro_category_format),
                        book.categories.get(0).trim()));
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

            if (ImageLoader.getInstance().isInited()) {
                DisplayImageOptions options = ImageLoaderUtils.createOptions(R.drawable.icon_place_holder);
                ImageLoaderUtils.display(book.cover, ivCover, options);
            }
        }
    }


    @Override
    public void showRelatedBooks(List<Book> books) {
        layoutRelated.setVisibility(View.VISIBLE);
        adapter.add(books);
        adapter.notifyDataSetChanged();
        setRelatedHeight();
    }

    private ViewTreeObserver myTree;

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

        if (dataRecommend == null) return;

        if (dataRecommend.isEmpty()) return;

        myTree = lvRecommend.getViewTreeObserver();
        if (myTree.isAlive()) {
            myTree.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int lineHeight = DisplayUtils.dip2px(getApplicationContext(), 1);
                    int itemHeight = lvRecommend.getMeasuredHeight() + lineHeight;
                    int height = itemHeight * dataRecommend.size() - lineHeight;
                    LinearLayout.LayoutParams params =
                            new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
                    lvRecommend.setLayoutParams(params);
                    if (myTree.isAlive()) {

                        try {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                myTree.removeGlobalOnLayoutListener(this);
                            } else {
                                myTree.removeOnGlobalLayoutListener(this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }
    }

}
