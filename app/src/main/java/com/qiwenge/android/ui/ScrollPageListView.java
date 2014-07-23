package com.qiwenge.android.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * ListView-滚动分页加载。
 *
 * Created by John on 2014-7-18
 */
public class ScrollPageListView extends ListView {

    private boolean isLoading = false;
    private boolean mLastItemVisible = false;
    private ScrollPageListener pageListener;

    public ScrollPageListView(Context context) {
        super(context);
        init();
    }

    public ScrollPageListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setIsLoading(boolean b) {
        this.isLoading = b;
    }

    public void setOnScrollPageListener(ScrollPageListener listener) {
        pageListener = listener;
    }

    /**
     * 初始化。
     */
    public void init() {
        this.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (!isLoading && mLastItemVisible
                        && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                    if (pageListener != null) {
                        pageListener.onPage();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
                if (!isLoading && (totalItemCount > 0)
                        && (firstVisibleItem + visibleItemCount >= totalItemCount))
                    mLastItemVisible = true;
                else
                    mLastItemVisible = false;
            }
        });
    }

    /**
     * 
     * 分页监听器。
     *
     * Created by John on 2014-7-18
     */
    public interface ScrollPageListener {
        public void onPage();
    }

}
