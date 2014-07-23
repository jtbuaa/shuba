package com.qiwenge.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dev1024.utils.DialogUtils;
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
    private ReadPagerView readerView;

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
    private Chapter next;

    /**
     * 上一章
     */
    private Chapter prev;

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

    private String content = "";

    private void initData() {
        mScaledTouchSlop =
                ViewConfiguration.get(getActivity().getApplicationContext()).getScaledTouchSlop();
        System.out.println("mScaledTouchSlop:" + mScaledTouchSlop);
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
        readerView = (ReadPagerView) getView().findViewById(R.id.tv_test);

        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                Page p = pageList.get(arg0);

                if (next != null && next.getId().equals(p.chapterId)) {
                    System.out.println("已经进入下一章");
                    current = next;
                    saveRecord(current.getId());
                    if (next.next != null) getNext(next.next.ref);
                }

                if (prev != null && prev.getId().equals(p.chapterId)) {
                    System.out.println("已经进入上一章");
                    current = prev;
                    saveRecord(current.getId());
                    if (prev.prev != null) getPrev(prev.prev.ref);
                }

                currentItem = arg0;
                if (isAdded()) {
                    tvTitle.setText(String.format(getString(R.string.str_chapter_title),
                            pageList.get(arg0).chapterNumber, pageList.get(arg0).chapterTitle));
                    tvPage.setText(String.format(PAGE_FORMAT, pageList.get(arg0).pageIndex,
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
     * @param link
     */
    public void getPrev(String link) {
        System.out.println("getPrev:" + link);
        JHttpClient.get(link, null, new JsonResponseHandler<Chapter>(Chapter.class, false) {

            @Override
            public void onSuccess(final Chapter result) {
                if (result != null && result.content != null && result.content.length() > 100) {
                    prev = result;
                    content = result.content.trim();
                    content = ReaderUtils.formatContent(content);
                    readerView.setText(content);
                    readerView.onPage(new OnReaderPageListener() {

                        @Override
                        public void onSuccess(List<String> pages) {
                            pageList.addAll(0, covertPageList(pages, result));
                            adapter.notifyDataSetChanged();
                            currentItem = currentItem + pages.size();
                            viewPager.setCurrentItem(currentItem, false);
                        }
                    });
                }
            }

            @Override
            public void onFailure(String msg) {
                System.out.println(msg);
            }

        });
    }

    /**
     * 获取下一页
     * 
     * @param link
     */
    public void getNext(String link) {
        System.out.println("getNext:" + link);
        JHttpClient.get(link, null, new JsonResponseHandler<Chapter>(Chapter.class, false) {

            @Override
            public void onSuccess(final Chapter result) {

                if (result != null && result.content != null && result.content.length() > 100) {
                    next = result;
                    content = result.content.trim();
                    content = ReaderUtils.formatContent(content);
                    readerView.setText(content);
                    readerView.onPage(new OnReaderPageListener() {

                        @Override
                        public void onSuccess(List<String> pages) {
                            pageList.addAll(covertPageList(pages, result));
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onFailure(String msg) {
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
        String url = ApiUtils.getChapter(chapterId);
        JHttpClient.get(url, null, new JsonResponseHandler<Chapter>(Chapter.class, false) {

            @Override
            public void onSuccess(final Chapter result) {
                if (result != null && result.content != null && result.content.length() > 100) {
                    saveRecord(chapterId);
                    tvTitle.setText(String.format(getString(R.string.str_chapter_title),
                            result.number, result.title));
                    if (result.content != null && result.content.length() > 100) {
                        current = result;
                        content = result.content.trim();
                        content = ReaderUtils.formatContent(content);
                        readerView.setText(content);
                        readerView.onPage(new OnReaderPageListener() {
                            @Override
                            public void onSuccess(List<String> pages) {
                                pageList.addAll(covertPageList(pages, result));
                                pageCount = pages.size();
                                tvPage.setText(String.format(PAGE_FORMAT, 1, pageCount));
                                adapter.notifyDataSetChanged();
                            }
                        });

                        if (result.prev != null) getPrev(result.prev.ref);

                        if (result.next != null) getNext(result.next.ref);
                    }
                }
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
     * 手机电量改变，广播接收。
     * 
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
}
