package com.qiwenge.android.act;

import java.util.ArrayList;
import java.util.List;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

import com.liuguangqiang.common.utils.DisplayUtils;
import com.liuguangqiang.common.utils.GsonUtils;
import com.liuguangqiang.common.utils.StringUtils;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qiwenge.android.R;
import com.qiwenge.android.adapters.AboutRmdAdapter;
import com.qiwenge.android.async.AsyncAddBook;
import com.qiwenge.android.async.AsyncRemoveBook;
import com.qiwenge.android.async.AsyncUtils;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.constant.BookStatus;
import com.qiwenge.android.listeners.CommonHandler;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.entity.BookList;
import com.qiwenge.android.listeners.LoginListener;
import com.qiwenge.android.ui.dialogs.LoginDialog;
import com.qiwenge.android.ui.dialogs.SourceDialog;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.ImageLoaderUtils;
import com.qiwenge.android.utils.LoginManager;
import com.qiwenge.android.utils.ReaderUtils;
import com.qiwenge.android.utils.book.BookManager;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.StringResponseHandler;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * 小说详情。
 * <p/>
 * Created by Eric on 2014-5-5
 */
@ContentView(R.layout.activity_book_intro)
public class BookDetailActivity extends BaseActivity implements OnClickListener {

    public static final String EXTRA_BOOK = "book";

    private static final int ACTION_ITEM_SOURCE = 1;

    @InjectView(R.id.tv_title)
    private TextView tvTitle;

    @InjectView(R.id.tv_intro)
    private TextView tvIntro;

    @InjectView(R.id.tv_author)
    private TextView tvAuthor;

    @InjectView(R.id.tv_category)
    private TextView tvCategory;

    @InjectView(R.id.tv_status)
    private TextView tvStatus;

    @InjectView(R.id.btn_chapter)
    private Button btnChapter;

    @InjectView(R.id.btn_add)
    private Button btnAdd;

    @InjectView(R.id.lv_recommend)
    private ListView lvRecommend;

    @InjectView(R.id.iv_cover)
    private ImageView ivCover;

    @InjectView(R.id.layout_related)
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
     * <p/>
     * 初始化时，ScrollView滑动到顶部。
     */
//    private boolean scrollToTop = true;

    private boolean hasInited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.book_intro_detail);
        getIntentData();
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, ACTION_ITEM_SOURCE, 0, "换源").setIcon(R.drawable.ic_action_source).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == ACTION_ITEM_SOURCE) {
            new SourceDialog(this, book).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showBookStatus();
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
                addOrRemoveBook();
                break;
            default:
                break;
        }
    }

    private void addOrRemoveBook() {
        if (LoginManager.isLogin()) {
            if (isAdded) {
                removeBook();
            } else {
                addBook();
            }
        } else {
            login();
        }
    }

    private LoginDialog loginDialog;

    private void login() {
        if (loginDialog == null) {
            loginDialog = new LoginDialog(this);
            loginDialog.setLoginListener(new LoginListener() {
                @Override
                public void onSuccess() {
                    showBookStatus();
                }
            });
        }
        loginDialog.show();
    }

    /**
     * 添加小说到书架
     */
    private void addBook() {
        new AsyncAddBook(this, new CommonHandler() {
            @Override
            public void onStart() {
                btnAdd.setEnabled(false);
            }

            @Override
            public void onSuccess() {
                btnAdd.setEnabled(true);
                showRemoveBtn();
            }

        }).execute(book);
    }

    private void removeBook() {
        new AsyncRemoveBook(this, new CommonHandler() {
            @Override
            public void onStart() {
                btnAdd.setEnabled(false);
            }

            @Override
            public void onSuccess() {
                btnAdd.setEnabled(true);
                showAddBtn();
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
        btnChapter.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        layoutRelated.setVisibility(View.GONE);

        // 相关推荐
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

        showBookInfo();
    }

    private void showAddBtn() {
        btnAdd.setText(R.string.book_intro_add);
        btnAdd.setBackgroundResource(R.drawable.btn_hollow_yellow);
        btnAdd.setTextColor(getResources().getColorStateList(R.color.btn_yellow_text_color));
        isAdded = false;
    }

    private void showRemoveBtn() {
        btnAdd.setText(R.string.book_intro_remove);
        btnAdd.setBackgroundResource(R.drawable.btn_hollow_gray);
        btnAdd.setTextColor(getResources().getColorStateList(R.color.btn_remove_text_color));
        isAdded = true;
    }

    private void showBookStatus() {
        if (book != null && BookManager.getInstance().contains(book)) {
            showRemoveBtn();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !hasInited) {
            hasInited = true;
            getRelated();
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
            JHttpClient.get(getApplicationContext(), url, params, new StringResponseHandler() {
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
