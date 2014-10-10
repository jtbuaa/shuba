package com.qiwenge.android.base;

import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.qiwenge.android.adapters.base.MyBaseAdapter;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.ui.PagePullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 14-9-20.
 */
public class BaseListActivity<T> extends BaseActivity {

    public PagePullToRefreshListView mListView;

    public List<T> data = new ArrayList<T>();

    public MyBaseAdapter<T> adapter;

    public int pageindex = 1;

    private boolean enableFooterPage = false;

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

    public void setDisablePullToRefresh() {
        if (mListView != null) {
            mListView.setMode(PullToRefreshBase.Mode.DISABLED);
        }
    }

    public void setEnableFooterPage() {
        if (mListView != null) {
            mListView.getRefreshableView().setFooterDividersEnabled(true);
            enableFooterPage = true;
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

    public void requestSuccess(List<T> newData) {

        if (newData.size() < Constants.DEFAULT_PAGE_SIZE) {
            mListView.removePageFooterView();
            mListView.setPageEnable(false);
        } else {
            mListView.addPageFooterView();
            mListView.setPageEnable(true);
        }

        if (pageindex == 1)
            data.clear();
        if (adapter != null)
            adapter.add(newData);
    }

    public void requestFinished() {
        if (mListView != null) {
            mListView.loadFinished();
        }
    }

}
