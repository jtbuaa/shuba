package com.qiwenge.android.act;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.dev1024.utils.AnimUtils;
import com.dev1024.utils.IntentUtils;
import com.dev1024.utils.LogUtils;
import com.dev1024.utils.listener.AnimListener;
import com.qiwenge.android.R;
import com.qiwenge.android.adapters.ReadMenuAdapter;
import com.qiwenge.android.adapters.ReadThemeAdapter;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.fragments.ReadFragment;
import com.qiwenge.android.listeners.ReadPageClickListener;
import com.qiwenge.android.models.ReadMenu;
import com.qiwenge.android.models.ReadTheme;
import com.qiwenge.android.utils.ReaderUtils;
import com.qiwenge.android.utils.ScreenBrightnessUtils;
import com.qiwenge.android.utils.ThemeUtils;

public class ReadActivity extends FragmentActivity implements View.OnClickListener {

    /**
     * 是否初始化完毕。
     */
    private boolean inited = false;

    private final static int ANIM_DURITATION = 200;

    /**
     * 章节Id
     */
    public final static String ChapterId = "chapterId";

    /**
     * 小说标题
     */
    public final static String BookTitle = "bookTitle";

    /**
     * 小说Id
     */
    public final static String BookId = "bookId";

    private ReadFragment fragment;

    /**
     * 顶部导航
     */
    private LinearLayout layoutTop;

    /**
     * 底部菜单
     */
    private LinearLayout layoutBottomMenu;

    private TextView tvBookTitle;

    private GridView gvMenu;

    private GridView gvTheme;

    /**
     * 亮度调节。
     */
    private SeekBar seekBarBrightness;

    /**
     * 字体大小调节。
     */
    private SeekBar seekFontSize;

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

    /**
     * 亮度SeekBar的值
     */
    private int mProgress = 0;

    /**
     * 字体大小增量。
     */
    private int mFontSizeOffest = 0;

    /**
     * 原始字体大小。
     */
    private int fontSizeOrigin = Constants.MIN_TEXT_SIZE;

    /**
     * 上次修改的字体大小。
     */
    private int lastTextSize=0;

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
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_bottom_menu:
                break;
            default:
                break;
        }
    }

    /**
     * 获取意图传递的数据，并获取章节详情。
     */
    private void getIntentData() {
        Bundle extra = getIntent().getExtras();

        if (extra.containsKey(BookId)) fragment.setBookId(extra.getString(BookId));

        if (extra.containsKey(BookTitle)) tvBookTitle.setText(extra.getString(BookTitle));

        if (extra.containsKey(ChapterId)) fragment.getChapter(extra.getString(ChapterId));

    }

    /**
     * 初始化数据，如menu,theme
     */
    private void initData() {
        String[] titles = getResources().getStringArray(R.array.read_menu_titles);
        int[] icons =
                {R.drawable.icon_menu_mode_night, R.drawable.icon_menu_aa,
                        R.drawable.icon_menu_chapters, R.drawable.icon_menu_share};

        ReadMenu menu;
        for (int i = 0; i < titles.length; i++) {
            menu = new ReadMenu();
            menu.title = titles[i];
            menu.icon = icons[i];
            menuData.add(menu);
        }

        int[] themeColor = getResources().getIntArray(R.array.read_theme_color);
        int[] themes = {ThemeUtils.NORMAL, ThemeUtils.YELLOW, ThemeUtils.GREEN, ThemeUtils.LEATHER};

        ReadTheme theme;
        for (int i = 0; i < themes.length; i++) {
            theme = new ReadTheme();
            theme.color = themeColor[i];
            theme.theme = themes[i];
            themeData.add(theme);
        }

        // 屏幕亮度。
        mProgress = ScreenBrightnessUtils.getBrightness(getApplicationContext());
        ScreenBrightnessUtils.setBrightness(this, mProgress);

    }

    private void initViews() {
        tvBookTitle = (TextView) this.findViewById(R.id.tv_book_title);

        layoutBottomMenu = (LinearLayout) this.findViewById(R.id.layout_bottom_menu);
        layoutBottomMenu.setVisibility(View.GONE);
        layoutBottomMenu.setOnClickListener(this);

        menuAdapter = new ReadMenuAdapter(getApplicationContext(), menuData);
        gvMenu = (GridView) this.findViewById(R.id.gv_menu);
        gvMenu.setAdapter(menuAdapter);

        themeAdapter = new ReadThemeAdapter(getApplicationContext(), themeData);
        gvTheme = (GridView) this.findViewById(R.id.gv_theme);
        gvTheme.setAdapter(themeAdapter);
        layoutTop = (LinearLayout) this.findViewById(R.id.layout_top);
        seekBarBrightness = (SeekBar) this.findViewById(R.id.seekbar_screen_brightness);
        seekBarBrightness.setMax(255);
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
                    mProgress = progress;
                    Message msg = new Message();
                    msg.what = 1;
                    msg.arg1 = progress;
                    mHandler.sendMessage(msg);
                }
            }
        });

        // 字体大小。
        seekFontSize = (SeekBar) this.findViewById(R.id.seekBar_font_size);
        int textSize= ReaderUtils.getTextSize(getApplicationContext());
        int progress=(textSize-fontSizeOrigin)*5;
        seekFontSize.setMax(100);
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
                    mFontSizeOffest = progress/5;
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
                        if (ThemeUtils.getCurrentTheme() == ThemeUtils.NIGHT) {
                            showNighitModel(false);
                        } else {
                            showNighitModel(true);
                        }
                        break;
                    case 1:// 字体设置
                        break;
                    case 2:// 目录

                        break;
                    case 3:// 分享
                        IntentUtils.openShare(ReadActivity.this, fragment.getShareContent(), "分享");
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

        if (fragment != null) fragment.setThemeBgColor(themeData.get(i).color);

        if (cacheable) ThemeUtils.setTheme(getApplicationContext(), themeData.get(i).theme);
    }

    private void clearThemeSelected() {
        themeData.get(currentTheme).selected = false;
        currentTheme = -1;
        themeAdapter.notifyDataSetChanged();
    }

    /**
     * 设置，是否为夜间模式。
     *
     * @param isNight
     */
    private void showNighitModel(boolean isNight) {
        if (!isNight) {
            menuData.get(0).icon = R.drawable.icon_menu_mode_normal;
            selectTheme(0, true);
        } else {
            clearThemeSelected();
            menuData.get(0).icon = R.drawable.icon_menu_mode_night;
            fragment.setThemeBgColor(getResources().getColor(R.color.read_night_bg));
            ThemeUtils.setTheme(getApplicationContext(), ThemeUtils.NIGHT);
        }
        menuAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化主题
     */
    private void initTheme() {
        int theme = ThemeUtils.getCurrentTheme();
        if (theme == ThemeUtils.NIGHT) {
            showNighitModel(true);
        } else {
            for (int i = 0; i < themeData.size(); i++) {
                if (themeData.get(i).theme == theme) {
                    selectTheme(i, false);
                    break;
                }
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
                showOrHideMenu();
            }
        });
    }

    /**
     * TODO 设置字体大小。
     */
    private void setReadTextSize() {
        int textSize=fontSizeOrigin+mFontSizeOffest;
        LogUtils.i("textSize",""+textSize);
        if(textSize!=lastTextSize) {
            //如果没有修改字体大小，如，来回滑动，不修改阅读器的字体大小
            lastTextSize=textSize;
            fragment.setTextSize(textSize);
            ReaderUtils.saveTextSize(getApplicationContext(),textSize);
        }
    }

    /**
     * 静态的内部类，继承了Handler，防内存泄露
     */
    static class MyHandler extends Handler {

        private WeakReference<Activity> mActivity;

        private int progress = 0;

        public MyHandler(Activity act) {
            mActivity = new WeakReference<Activity>(act);
            ;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                progress = msg.arg1;
                ScreenBrightnessUtils.setBrightness(mActivity.get(), progress);
            }
        }

    }

}
