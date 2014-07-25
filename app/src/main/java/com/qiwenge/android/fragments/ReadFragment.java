package com.qiwenge.android.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dev1024.utils.DialogUtils;
import com.dev1024.utils.LogUtils;
import com.dev1024.utils.StringUtils;
import com.qiwenge.android.R;
import com.qiwenge.android.base.BaseFragment;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.listeners.ReadPageClickListener;
import com.qiwenge.android.models.Chapter;
import com.qiwenge.android.models.Page;
import com.qiwenge.android.reader.OnReaderPageListener;
import com.qiwenge.android.reader.ReadPagerView;
import com.qiwenge.android.reader.ReaderAdapter;
import com.qiwenge.android.ui.SlowViewPager;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.BookShelfUtils;
import com.qiwenge.android.utils.ReaderUtils;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.JsonResponseHandler;

public class ReadFragment extends BaseFragment {

    private final static String PAGE_FORMAT = "%s/%s";

    private LinearLayout layoutContainer;

    /**
     * 手机电量。
     */
    private ProgressBar pbBattery;

    /**
     * 小说标题。
     */
    private TextView tvTitle;

    /**
     * 页码。
     */
    private TextView tvPage;

    /**
     * 小说分页器。
     */
    private ReadPagerView readerCurrent;

    private ReadPagerView readerNext;

    private ReadPagerView readerPrev;

    private SlowViewPager viewPager;

    private ReaderAdapter adapter;

    private BatteryReceiver batteryReceiver;

    private ReadPageClickListener clickListener;

    private List<Page> pageList = new ArrayList<Page>();

    /**
     * 当前章节分页数
     */
    private int pageCount = 0;

    private int currentItem = 0;

    /**
     * 当前章节
     */
    private Chapter current;


    /**
     * 下一章
     */
    private Chapter nextChapter;

    /**
     * 上一章
     */
    private Chapter prevChapter;

    private String currentContent;

    private String nextContent;

    private String prevContent;

    /**
     * 小说Id
     */
    private String bookId;

    private float lastX = 0;
    private float currentX = 0;
    private long lastClickTime = 0;
    private long currentClickTime = 0;

    /**
     * 如同两次ACTION_DOWN的时间间隔小于CLICK_INTERVAL，可以判断为是做的点击操作,否则为长按操作。
     */
    private final int CLICK_INTERVAL = 300;

    /**
     * 最小滑动距离，如果大于它，则判断为滑动。
     */
    private int mScaledTouchSlop = 0;

    /**
     * 屏幕三分之一的宽度。
     */
    private int oneThird = Constants.WIDTH / 3;

    /**
     * 章节临时缓存。
     */
    private HashMap<String, Chapter> chapterCache = new HashMap<String, Chapter>();

    /**
     * 在当前章节中的页码。
     */
    private int currentChapterPageIndex = 0;

    private List<String> listCurrent;

    private List<String> listNext;

    private List<String> listPrev;

    public void setOnReadPageClickListener(ReadPageClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initViews();
        initBatteryReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeBatteryReceiver();
    }

    private void initData() {
        mScaledTouchSlop =
                ViewConfiguration.get(getActivity().getApplicationContext()).getScaledTouchSlop();
    }

    /**
     * 初始化字体大小
     */
    private void initTextSize(){
        int textSize=ReaderUtils.getTextSize(getActivity().getApplicationContext());
        readerCurrent.setTextSize(textSize);
        readerNext.setTextSize(textSize);
        readerPrev.setTextSize(textSize);
        adapter.setTextSize(textSize);
    }

    /**
     * 初始化Views
     */
    private void initViews() {
        layoutContainer = (LinearLayout) getView().findViewById(R.id.layout_container);
        pbBattery = (ProgressBar) getView().findViewById(R.id.progreebar_battery);
        tvTitle = (TextView) getView().findViewById(R.id.tv_title);
        tvPage = (TextView) getView().findViewById(R.id.tv_pages);
        adapter = new ReaderAdapter(getActivity(), pageList);
        viewPager = (SlowViewPager) getView().findViewById(R.id.viewpager_reader);
        viewPager.setAdapter(adapter);
        readerCurrent = (ReadPagerView) getView().findViewById(R.id.reader_current);
        readerNext = (ReadPagerView) getView().findViewById(R.id.reader_next);
        readerPrev = (ReadPagerView) getView().findViewById(R.id.reader_prev);
        initTextSize();
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                Page p = pageList.get(arg0);

                if (nextChapter != null && nextChapter.getId().equals(p.chapterId)) {
                    System.out.println("已经进入下一章");
                    current = nextChapter;
                    listCurrent = listNext;
                    saveRecord(current.getId());
                    if (nextChapter.next != null) getNext(nextChapter.next.getId());
                }

                if (prevChapter != null && prevChapter.getId().equals(p.chapterId)) {
                    System.out.println("已经进入上一章");
                    current = prevChapter;
                    listCurrent = listPrev;
                    saveRecord(current.getId());
                    if (prevChapter.prev != null) getPrev(prevChapter.prev.getId());
                }

                currentItem = arg0;
                if (isAdded()) {
                    currentChapterPageIndex = pageList.get(arg0).pageIndex;
                    setTitle(String.format(getString(R.string.str_chapter_title),
                            pageList.get(arg0).chapterNumber, pageList.get(arg0).chapterTitle));
                    setPager(String.format(PAGE_FORMAT, pageList.get(arg0).pageIndex,
                            pageList.get(arg0).pageSize));
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        // View onTouch
        viewPager.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = event.getRawX();
                        lastClickTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        currentClickTime = System.currentTimeMillis();
                        currentX = event.getRawX();
                        float offsetX = Math.abs(currentX - lastX);
                        if ((currentClickTime - lastClickTime) <= CLICK_INTERVAL
                                && offsetX < mScaledTouchSlop) {
                            handleOnClick();
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 处理点击事件。
     */
    private void handleOnClick() {
        if (lastX < oneThird) {
            if (currentItem > 0) {
                viewPager.setCurrentItem(currentItem - 1);
            }

        } else if (lastX > oneThird * 2) {
            if (currentItem < adapter.getCount()) {
                viewPager.setCurrentItem(currentItem + 1);
            }
        } else if (clickListener != null) {
            clickListener.onClick();
        }
    }

    /**
     * 获取上一页
     *
     * @param chapterId
     */
    public void getPrev(final String chapterId) {
        if (chapterCache.containsKey(chapterId)) {
            handlePrev(chapterCache.get(chapterId));
            LogUtils.i("Reader", "get prev chapter from cache");
            return;
        }

        String url = ApiUtils.getChapter(chapterId);
        JHttpClient.get(url, null, new JsonResponseHandler<Chapter>(Chapter.class, false) {

            @Override
            public void onSuccess(final Chapter result) {
                if (result != null) {
                    chapterCache.put(chapterId, result);
                }
                handlePrev(result);
            }

            @Override
            public void onFailure(String msg) {
                if (msg == null) msg = "unknow msg";
                System.out.println(msg);
            }

        });
    }

    /**
     * 获取下一页
     *
     * @param chapterId
     */
    public void getNext(final String chapterId) {
        if (chapterCache.containsKey(chapterId)) {
            handleNext(chapterCache.get(chapterId));
            LogUtils.i("Reader", "get next chapter from cache");
            return;
        }

        String url = ApiUtils.getChapter(chapterId);
        JHttpClient.get(url, null, new JsonResponseHandler<Chapter>(Chapter.class, false) {

            @Override
            public void onSuccess(final Chapter result) {
                if (result != null) {
                    chapterCache.put(chapterId, result);
                }
                handleNext(result);
            }

            @Override
            public void onFailure(String msg) {
                if (msg == null) msg = "unknow msg";
                System.out.println(msg);
            }

        });

    }

    /**
     * 获取章节内容
     *
     * @param chapterId
     */
    public void getChapter(final String chapterId) {
        if (chapterCache.containsKey(chapterId)) {
            handleCurrent(chapterId, chapterCache.get(chapterId));
            LogUtils.i("Reader", "get current chapter from cache");
            return;
        }

        String url = ApiUtils.getChapter(chapterId);
        JHttpClient.get(url, null, new JsonResponseHandler<Chapter>(Chapter.class, false) {

            @Override
            public void onSuccess(final Chapter result) {
                if (result != null) {
                    chapterCache.put(chapterId, result);
                }
                handleCurrent(chapterId, result);
            }

            @Override
            public void onStart() {
                DialogUtils.showLoading(getActivity());
            }

            @Override
            public void onFinish() {
                DialogUtils.hideLoading();
            }

            @Override
            public void onFailure(String msg) {
                if (msg == null) msg = "unknow msg";
                System.out.println(msg);
            }

        });
    }

    private List<Page> covertPageList(List<String> list, Chapter result) {
        Page p = null;
        List<Page> pages = new ArrayList<Page>();
        for (int i = 0; i < list.size(); i++) {
            p = new Page();
            p.pageIndex = i + 1;
            p.pageSize = list.size();
            p.chapterId = result.getId();
            p.chapterNumber = result.number;
            p.chapterTitle = result.title;
            p.content = list.get(i);
            pages.add(p);
        }
        return pages;
    }

    private void handleCurrent(String chapterId, final Chapter result) {
        if (isAdded() && result != null && result.content != null && result.content.length() > 100) {
            saveRecord(chapterId);
            tvTitle.setText(String.format(getString(R.string.str_chapter_title),
                    result.number, result.title));
            if (result.content != null && result.content.length() > 100) {
                currentContent = result.content.toString();
                current = result;
                currentContent = result.content.trim();
                currentContent = ReaderUtils.formatContent(currentContent);
                readerCurrent.setText(currentContent);
                readerCurrent.onPage(new OnReaderPageListener() {
                    @Override
                    public void onSuccess(List<String> pages) {
                        listCurrent = pages;
                        pageList.addAll(covertPageList(pages, result));
                        pageCount = pages.size();
                        tvPage.setText(String.format(PAGE_FORMAT, 1, pageCount));
                        adapter.notifyDataSetChanged();
                    }
                });

                if (result.prev != null) getPrev(result.prev.getId());

                if (result.next != null) getNext(result.next.getId());
            }
        }
    }

    private void handleNext(final Chapter result) {
        if (result != null && result.content != null && result.content.length() > 100) {
            nextChapter = result;
            nextContent = result.content.trim();
            nextContent = ReaderUtils.formatContent(nextContent);
            readerNext.setText(nextContent);
            readerNext.onPage(new OnReaderPageListener() {

                @Override
                public void onSuccess(List<String> pages) {
                    listNext = pages;
                    pageList.addAll(covertPageList(pages, result));
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void handlePrev(final Chapter result) {
        if (result != null && result.content != null && result.content.length() > 100) {
            prevChapter = result;
            prevContent = result.content.trim();
            prevContent = ReaderUtils.formatContent(prevContent);
            readerPrev.setText(prevContent);
            readerPrev.onPage(new OnReaderPageListener() {

                @Override
                public void onSuccess(List<String> pages) {
                    listPrev = pages;
                    pageList.addAll(0, covertPageList(pages, result));
                    adapter.notifyDataSetChanged();
                    currentItem = currentItem + pages.size();
                    viewPager.setCurrentItem(currentItem, false);
                }
            });
        }
    }

    /**
     * 注册电量改变广播接收
     */
    private void initBatteryReceiver() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryReceiver = new BatteryReceiver();
        getActivity().registerReceiver(batteryReceiver, filter);
    }

    /**
     * 移除手机电量监听
     */
    private void removeBatteryReceiver() {
        if (batteryReceiver != null) getActivity().unregisterReceiver(batteryReceiver);
    }

    /**
     * 改变主题背景颜色
     *
     * @param bgColor
     */
    public void setThemeBgColor(int bgColor) {
        if (layoutContainer != null) layoutContainer.setBackgroundColor(bgColor);
    }

    /**
     * 获取分享内容
     *
     * @return
     */
    public String getShareContent() {
        if (current != null)
            return String.format(getString(R.string.str_share_format), current.content);
        return "";
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    /**
     * 保存阅读记录。
     *
     * @param chapterId
     */
    private void saveRecord(String chapterId) {
        if (bookId != null) BookShelfUtils.saveRecord(getActivity(), bookId, chapterId);
    }

    /**
     * TODO 设置字体大小。
     *
     * @param textSize
     */
    public void setTextSize(final int textSize) {
        readerNext.setTextSize(textSize);
        readerPrev.setTextSize(textSize);
        readerCurrent.setTextSize(textSize);
        refreshText(textSize);
    }

    private int mTextSize = 20;

    private void refreshText(int textSize) {
        this.mTextSize = textSize;
        setCurrentText();
    }

    /**
     * TODO 设置当前章节内容。
     */
    private void setCurrentText() {
        if (StringUtils.isEmptyOrNull(currentContent)) return;
        setText(readerCurrent, currentContent, new OnReaderPageListener() {
            @Override
            public void onSuccess(List<String> pages) {
                int readCount = getReadTextCount();
                listCurrent = pages;
                pageList.clear();
                pageList.addAll(covertPageList(pages, current));
                pageCount = pages.size();
                tvPage.setText(String.format(PAGE_FORMAT, 1, pageCount));
                adapter.setTextSize(mTextSize);

                int pageindex = getNewPageIndex(readCount, pages);

                if (pageindex > pageCount) {
                    pageindex = 0;
                }

                LogUtils.i("pageindex", "" + pageindex);
                adapter.notifyDataSetChanged();
                viewPager.setCurrentItem(pageindex, false);
                currentChapterPageIndex = pageindex;
                if (current.prev != null) getPrev(current.prev.getId());

                if (current.next != null) getNext(current.next.getId());
            }
        });
    }


    /**
     * 获取上次阅读到的内容的字数。
     *
     * @return
     */
    private int getReadTextCount() {
        LogUtils.i("currentChapterPageIndex", "" + currentChapterPageIndex);
        int size = 0;
        if (listCurrent != null && !listCurrent.isEmpty()) {
            for (int i = 0; i < currentChapterPageIndex; i++) {
                if (currentChapterPageIndex == i - 1)
                    size = size + (listCurrent.get(i).length()/2);
                else
                    size = size + listCurrent.get(i).length();
            }
        }
        return size;
    }

    private int getNewPageIndex(int readCount, List<String> pages) {
        int total = 0;
        int pageindex = 0;
        for (int i = 0; i < pages.size(); i++) {
            total = total + pages.get(i).length();
            if (total >= readCount) {
                pageindex = i - 1;
                break;
            }
        }

        if (pageindex < 0) pageindex = 0;

        return pageindex;
    }

    //TODO
    private void setText(ReadPagerView reader, String content, final OnReaderPageListener listener) {
        reader.setTextSize(mTextSize);
        reader.setText(content);
        final ViewTreeObserver treeObserver = reader.getViewTreeObserver();
        treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                treeObserver.removeOnGlobalLayoutListener(this);
                readerCurrent.onPage(new OnReaderPageListener() {
                    @Override
                    public void onSuccess(List<String> pages) {
                        listener.onSuccess(pages);
                    }
                });

            }
        });
    }


    /**
     * 手机电量改变，广播接收。
     * <p/>
     * Created by John on 2014年5月14日
     */
    private class BatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                int level = intent.getIntExtra("level", 0);
                // int scale = intent.getIntExtra("scale", 100);
                pbBattery.setProgress(100 - level);
            }
        }
    }


    private final int HANDLER_SET_TITLE = 1;

    private final int HANDLER_SET_PAGER = 2;

    private String mTitle;

    private String mPager;

    /**
     * 设置标题
     *
     * @param title
     */
    private void setTitle(String title) {
        mTitle = title;
        Message msg = new Message();
        msg.what = HANDLER_SET_TITLE;
        mHandle.sendMessage(msg);
    }

    /**
     * 设置页码
     *
     * @param pager
     */
    private void setPager(String pager) {
        mPager = pager;
        Message msg = new Message();
        msg.what = HANDLER_SET_PAGER;
        mHandle.sendMessage(msg);
    }

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_SET_TITLE:
                    tvTitle.setText(mTitle);
                    break;
                case HANDLER_SET_PAGER:
                    tvPage.setText(mPager);
                    break;
            }
        }
    };


}
