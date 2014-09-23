package com.qiwenge.android.base;

import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.models.Book;
import com.qiwenge.android.ui.PagePullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 14-9-20.
 */
public class BaseListFragment<T> extends BaseFragment {

    public PagePullToRefreshListView mListView;

    public List<Book> data = new ArrayList<Book>();

    public int pageindex = 1;

    private boolean enableFooterPage=false;

    public void setEnablePullToRefresh() {
        if (mListView != null) {
            mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

                @Override
                public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                    pageindex = 1;
                    mListView.reset();
                    requestData();
                }
            });
        }
    }

    public void setEnableFooterPage() {
        if (mListView != null) {
            mListView.getRefreshableView().setFooterDividersEnabled(true);
            enableFooterPage=true;
            mListView.addPageFooterView();
            mListView.setOnScrollPageListener(new PagePullToRefreshListView.ScrollPageListener() {
                @Override
                public void onPage() {
                    pageindex++;
                    requestData();
                }
            });
        }
    }

    public void requestData() {
        if (mListView != null && mListView.isLoading()) return;

        if (mListView != null)
            mListView.loadStart();
    }

    public void requestFinished() {
        if (mListView != null) {
            if(data.size()> Constants.DEFAULT_PAGE_SIZE&&enableFooterPage){
                mListView.addPageFooterView();
            }
            mListView.loadFinished(data.size());
        }
    }

}
