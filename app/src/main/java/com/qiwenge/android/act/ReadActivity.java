package com.qiwenge.android.act;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.dev1024.utils.AnimUtils;
import com.dev1024.utils.listener.AnimListener;
import com.qiwenge.android.R;
import com.qiwenge.android.adapters.ReadMenuAdapter;
import com.qiwenge.android.adapters.ReadThemeAdapter;
import com.qiwenge.android.async.AsyncUtils;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.fragments.ReadFragment;
import com.qiwenge.android.listeners.ReadPageClickListener;
import com.qiwenge.android.models.Book;
import com.qiwenge.android.models.ReadMenu;
import com.qiwenge.android.models.ReadTheme;
import com.qiwenge.android.utils.BookShelfUtils;
import com.qiwenge.android.utils.ReaderUtils;
import com.qiwenge.android.utils.ScreenBrightnessUtils;
import com.qiwenge.android.utils.ThemeUtils;

public class ReadActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ReadActivity";

    /**
     * 是否初始化完毕。
     */
    private boolean inited = false;

    private static final int MESSAGE_SET_BRIGHTNESS = 0x1;

    private final static int ANIM_DURITATION = 200;

    /**
     * 最大屏幕亮度
     */
    private final static int MAX_BRIGHTNESS = 255;

    /**
     * 调节字体大小的SeekBar的最大值
     */
    private final static int MAX_VALUE_FONTSZIE_SEEKBAR = 100;

    /**
     * SeekBar的progrees，缩小5倍为字体偏移量
     */
    private final static int OFFSET_ZOOM_OUT = 5;

    /**
     * 最大字体偏移量
     */
    private final static int MAX_FONTSIZE_OFFSET = MAX_VALUE_FONTSZIE_SEEKBAR / OFFSET_ZOOM_OUT;

    /**
     * 调节屏幕亮度的增量，仅点击亮度按钮时使用
     */
    private final static int INCREMENT_BRIGHTNESS = 5;

    /**
     * 亮度SeekBar的值
     */
    private int mProgress = 0;

    /**
     * 字体大小增量。
     */
    private int mFontSizeOffest = 0;

    /**
     * 原始字体大小。字体大小的调节，在此基础上偏移
     */
    private int mFontSizeOrigin = Constants.MIN_TEXT_SIZE;

    /**
     * 上次修改的字体大小。
     */
    private int lastTextSize = 0;

    /**
     * 章节Id
     */
    public final static String Extra_ChapterId = "chapterId";

    /**
     * 小说标题
     */
    public final static String Extra_BookTitle = "bookTitle";

    /**
     * 小说Id
     */
    public final static String Extra_BookId = "bookId";

    public final static String Extra_Book = "book";

    private ReadFragment fragment;

    private LinearLayout actionBack;

    /**
     * 阅读器最外层容器
     */
    private RelativeLayout layoutContainer;

    /**
     * 顶部导航
     */
    private LinearLayout layoutTop;

    /**
     * 底部菜单
     */
    private LinearLayout layoutBottomMenu;

    private TextView tvBookTitle;

    private TextView tvAddCollect;

    private GridView gvMenu;

    private GridView gvTheme;

    private SeekBar seekBarBrightness;

    private SeekBar seekFontSize;

    private ImageView ivBrightnessMinus;

    private ImageView ivBrightnessPlus;

    private TextView tvFontSizeMinus;

    private TextView tvFontSizePlus;

    private boolean topIsShow = false;

    private boolean bottomIsShow = false;

    /**
     * 菜单动画，是否正在进行。避免重复执行动画。
     */
    private boolean menuAnimActioning = false;

    /**
     * Menu
     */
    private List<ReadMenu> menuData = new ArrayList<ReadMenu>();

    /**
     * Theme
     */
    private List<ReadTheme> themeData = new ArrayList<ReadTheme>();

    private ReadMenuAdapter menuAdapter;

    private ReadThemeAdapter themeAdapter;

    /**
     * 当前选择的主题
     */
    private int currentTheme = 0;

    private Handler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        initData();
        initViews();
        initReadFragment();
        getIntentData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("TAG", "onNewIntent");
        if (intent != null && intent.getExtras() != null) {
            handleExtras(intent.getExtras());
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !inited) {
            initListeners();
            initTheme();
            inited = true;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        ScreenBrightnessUtils.saveScreenBright(getApplicationContext(), mProgress);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && !menuAnimActioning) {
            showOrHideMenu();
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (topIsShow) showOrHideMenu();
            else finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_collect://收藏
                if (book != null) {
                    BookShelfUtils.addBook(getApplicationContext(), book);
                    tvAddCollect.setVisibility(View.GONE);
                }
                break;
            case R.id.layout_bottom_menu:
                break;
            case R.id.layout_back:
                finish();
                break;
            case R.id.iv_brightness_minus:
                minusBrightness();
                break;
            case R.id.iv_brightness_plus:
                plusBirghtness();
                break;
            case R.id.tv_fontsize_minus:
                minusFontSize();
                break;
            case R.id.tv_fontsize_plus:
                plusFontSize();
                break;
            default:
                break;
        }
    }

    private String bookId;
    private String bookTitle;
    private String chapterId;
    private Book book;

    /**
     * 获取意图传递的数据，并获取章节详情。
     */
    private void getIntentData() {
        Bundle extra = getIntent().getExtras();
        handleExtras(extra);
    }

    //TODO SingleTask Bundle Extras
    private void handleExtras(Bundle extra) {
        Log.i(TAG, "handleExtras");

        fragment.clearReader();

        if (extra.containsKey(Extra_BookId)) {
            bookId = extra.getString(Extra_BookId);
            fragment.setBookId(bookId);
            AsyncUtils.postViewTotal(bookId);
        }

        if (extra.containsKey(Extra_BookTitle)) {
            bookTitle = extra.getString(Extra_BookTitle);
            tvBookTitle.setText(bookTitle);
        }

        if (extra.containsKey(Extra_ChapterId)) {
            chapterId = extra.getString(Extra_ChapterId);
            int length = BookShelfUtils.getReadLenght(getApplicationContext(), chapterId);
            fragment.getChapter(chapterId, length);
        }

        if (extra.containsKey(Extra_Book)) {
            book = extra.getParcelable(Extra_Book);
            if (book != null && !BookShelfUtils.contains(getApplicationContext(), book)) {
                tvAddCollect.setVisibility(View.VISIBLE);
            } else {
                tvAddCollect.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 初始化数据，如menu,theme
     */
    private void initData() {
        String[] titles = getResources().getStringArray(R.array.read_menu_titles);
        int[] icons =
                {R.drawable.icon_menu_mode_night,
                        R.drawable.icon_menu_chapters, R.drawable.icon_menu_favour};

        ReadMenu menu;
        for (int i = 0; i < titles.length; i++) {
            menu = new ReadMenu();
            menu.title = titles[i];
            menu.icon = icons[i];
            menuData.add(menu);
        }

        int[] themes = {ThemeUtils.NORMAL, ThemeUtils.YELLOW, ThemeUtils.GREEN, ThemeUtils.LEATHER};

        ReadTheme theme;
        for (int i = 0; i < themes.length; i++) {
            theme = new ReadTheme();
            theme.theme = themes[i];
            themeData.add(theme);
        }

        // 屏幕亮度。
        mProgress = ScreenBrightnessUtils.getBrightness(getApplicationContext());
        ScreenBrightnessUtils.setBrightness(this, mProgress);

    }

    private void initViews() {
        layoutContainer = (RelativeLayout) this.findViewById(R.id.layout_read_container);
        layoutTop = (LinearLayout) this.findViewById(R.id.layout_reader_top);
        tvBookTitle = (TextView) this.findViewById(R.id.tv_book_title);
        tvAddCollect = (TextView) this.findViewById(R.id.tv_add_collect);
        tvAddCollect.setVisibility(View.GONE);
        tvAddCollect.setOnClickListener(this);
        actionBack = (LinearLayout) this.findViewById(R.id.layout_back);
        actionBack.setOnClickListener(this);

        initBottomMenu();

        initBrightnessSeekBar();

        initFontSizeSeekBar();
    }


    private void initBottomMenu() {
        layoutBottomMenu = (LinearLayout) this.findViewById(R.id.layout_bottom_menu);
        layoutBottomMenu.setVisibility(View.GONE);
        layoutBottomMenu.setOnClickListener(this);

        ivBrightnessMinus = (ImageView) this.findViewById(R.id.iv_brightness_minus);
        ivBrightnessPlus = (ImageView) this.findViewById(R.id.iv_brightness_plus);
        tvFontSizeMinus = (TextView) this.findViewById(R.id.tv_fontsize_minus);
        tvFontSizePlus = (TextView) this.findViewById(R.id.tv_fontsize_plus);
        ivBrightnessMinus.setOnClickListener(this);
        ivBrightnessPlus.setOnClickListener(this);
        tvFontSizeMinus.setOnClickListener(this);
        tvFontSizePlus.setOnClickListener(this);

        menuAdapter = new ReadMenuAdapter(getApplicationContext(), menuData);
        gvMenu = (GridView) this.findViewById(R.id.gv_menu);
        gvMenu.setAdapter(menuAdapter);

        themeAdapter = new ReadThemeAdapter(getApplicationContext(), themeData);
        gvTheme = (GridView) this.findViewById(R.id.gv_theme);
        gvTheme.setAdapter(themeAdapter);
    }

    private void initBrightnessSeekBar() {
        seekBarBrightness = (SeekBar) this.findViewById(R.id.seekbar_screen_brightness);
        seekBarBrightness.setMax(MAX_BRIGHTNESS);
        seekBarBrightness.setProgress(mProgress);
        seekBarBrightness.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    setBrightness(progress);
                }
            }
        });
    }

    private void initFontSizeSeekBar() {
        // 字体大小。
        seekFontSize = (SeekBar) this.findViewById(R.id.seekBar_font_size);
        int textSize = ReaderUtils.getTextSize(getApplicationContext());

        //字体大小改变的偏移量x5，就是SeekBar的进度
        mFontSizeOffest = textSize - mFontSizeOrigin;
        int progress = mFontSizeOffest * OFFSET_ZOOM_OUT;
        seekFontSize.setMax(MAX_VALUE_FONTSZIE_SEEKBAR);
        seekFontSize.setProgress(progress);
        seekFontSize.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setReadTextSize();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mFontSizeOffest = progress / OFFSET_ZOOM_OUT;
                }
            }
        });
    }

    private void initListeners() {
        gvMenu.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                switch (arg2) {
                    case 0:// 夜间模式
                        if (ThemeUtils.isNightModel) {
                            showNighitModel(false);
                        } else {
                            showNighitModel(true);
                        }
                        break;
                    case 1:// 目录
                        Bundle extra = new Bundle();
                        extra.putString(ChapterActivity.EXTRA_BOOK_ID, bookId);
                        extra.putString(ChapterActivity.EXTRA_BOOK_TITLE, bookTitle);
                        startActivity(ChapterActivity.class, extra);
                        break;
                    case 2:// 赞
                        AsyncUtils.postVoteup(getApplicationContext(),bookId);
                        break;

                    default:
                        break;
                }

            }
        });

        gvTheme.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectTheme(position, true);
            }
        });
    }

    /**
     * 选择主题。
     *
     * @param i
     */
    private void selectTheme(int i, boolean cacheable) {
        if (currentTheme >= 0) themeData.get(currentTheme).selected = false;
        themeData.get(i).selected = true;
        currentTheme = i;
        themeAdapter.notifyDataSetChanged();

        ThemeUtils.setReaderTheme(themeData.get(i).theme, layoutContainer);
        ThemeUtils.setNightModle(getApplicationContext(), false);
        cancelNightModel();
        if (cacheable) ThemeUtils.setTheme(getApplicationContext(), themeData.get(i).theme);
    }

    private void clearThemeSelected() {
        themeData.get(currentTheme).selected = false;
        currentTheme = -1;
        themeAdapter.notifyDataSetChanged();
    }

    /**
     * 取消夜间模式
     */
    private void cancelNightModel() {
        menuData.get(0).icon = R.drawable.icon_menu_mode_night;
        menuData.get(0).title = getString(R.string.reader_night_model);
        menuAdapter.notifyDataSetChanged();
        fragment.refreshTextColor();
    }

    /**
     * 设置，是否为夜间模式。
     *
     * @param isNight
     */
    private void showNighitModel(boolean isNight) {
        ThemeUtils.setNightModle(getApplicationContext(), isNight);
        ThemeUtils.setThemeBg(layoutContainer);
        if (isNight) {
            menuData.get(0).icon = R.drawable.icon_menu_mode_normal;
            menuData.get(0).title = getString(R.string.reader_normal_model);
            clearThemeSelected();
        } else {
            selectedLastTheme();
            cancelNightModel();
        }
        menuAdapter.notifyDataSetChanged();
        fragment.refreshTextColor();
    }

    /**
     * 初始化主题
     */
    private void initTheme() {
        if (ThemeUtils.isNightModel) {
            showNighitModel(true);
        } else {
            selectedLastTheme();
        }
    }

    /**
     * 选择原来的主题
     */
    private void selectedLastTheme() {
        int theme = ThemeUtils.getCurrentTheme();
        for (int i = 0; i < themeData.size(); i++) {
            if (themeData.get(i).theme == theme) {
                selectTheme(i, false);
                break;
            }
        }
    }

    private void showTop() {
        topIsShow = true;
        animate(layoutTop).setDuration(ANIM_DURITATION).y(0).start();
    }

    private void hideTop() {
        topIsShow = false;
        animate(layoutTop).setDuration(ANIM_DURITATION).y(-layoutTop.getHeight()).start();
    }

    /**
     * 显示底部菜单
     */
    private void showBottomMenu() {

        AnimUtils.showFromBottom(layoutBottomMenu, ANIM_DURITATION, new AnimListener() {
            @Override
            public void onEnd() {
                bottomIsShow = true;
                menuAnimActioning = false;
            }
        });

    }

    /**
     * 隐藏底部菜单
     */
    private void hideBottomMenu() {
        AnimUtils.hideFromBottom(layoutBottomMenu, ANIM_DURITATION, new AnimListener() {
            @Override
            public void onEnd() {
                bottomIsShow = false;
                menuAnimActioning = false;
            }
        });
    }

    /**
     * 显示或者隐藏菜单。
     */
    private void showOrHideMenu() {
        if (menuAnimActioning) return;
        menuAnimActioning = true;
        handleTop();
        handleBottom();
    }

    private void handleTop() {
        if (topIsShow)
            hideTop();
        else
            showTop();
    }

    private void handleBottom() {
        if (bottomIsShow) {
            hideBottomMenu();
        } else {
            showBottomMenu();
        }

    }

    /**
     * 初始化Fragment。
     */
    private void initReadFragment() {
        fragment = new ReadFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_content, fragment).commit();
        fragment.setOnReadPageClickListener(new ReadPageClickListener() {

            @Override
            public void onClick() {
                if (!menuAnimActioning) {
                    showOrHideMenu();
                }
            }
        });
    }

    private void setReadTextSize() {
        int textSize = mFontSizeOrigin + mFontSizeOffest;
        if (textSize != lastTextSize) {
            //如果没有修改字体大小，如，来回滑动，不修改阅读器的字体大小
            lastTextSize = textSize;
            fragment.setTextSize(textSize);
            ReaderUtils.saveTextSize(getApplicationContext(), textSize);
        }
    }

    private void setBrightness(int progress) {
        mProgress = progress;
        Message msg = mHandler.obtainMessage();
        msg.what = MESSAGE_SET_BRIGHTNESS;
        msg.arg1 = progress;
        msg.sendToTarget();
    }

    private void minusBrightness() {
        mProgress = mProgress - INCREMENT_BRIGHTNESS;
        if (mProgress < 0) mProgress = 0;
        seekBarBrightness.setProgress(mProgress);
        setBrightness(mProgress);
    }

    private void plusBirghtness() {
        mProgress = mProgress + INCREMENT_BRIGHTNESS;
        if (mProgress > MAX_BRIGHTNESS) mProgress = MAX_BRIGHTNESS;
        seekBarBrightness.setProgress(mProgress);
        setBrightness(mProgress);
    }

    private void minusFontSize() {
        mFontSizeOffest = mFontSizeOffest - 1;
        if (mFontSizeOffest < 0) mFontSizeOffest = 0;
        seekFontSize.setProgress(mFontSizeOffest * OFFSET_ZOOM_OUT);
        setReadTextSize();
    }

    private void plusFontSize() {
        mFontSizeOffest = mFontSizeOffest + 1;
        if (mFontSizeOffest > MAX_FONTSIZE_OFFSET) mFontSizeOffest = MAX_FONTSIZE_OFFSET;
        seekFontSize.setProgress(mFontSizeOffest * OFFSET_ZOOM_OUT);
        setReadTextSize();
    }


    /**
     * 静态的内部类，继承了Handler，防内存泄露
     */
    private static class MyHandler extends Handler {

        private WeakReference<Activity> mActivity;

        public MyHandler(Activity act) {
            mActivity = new WeakReference<Activity>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_SET_BRIGHTNESS) {
                ScreenBrightnessUtils.setBrightness(mActivity.get(), msg.arg1);
            }
        }

    }

}
