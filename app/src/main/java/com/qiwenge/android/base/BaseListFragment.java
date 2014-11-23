package com.qiwenge.android.base;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.qiwenge.android.R;
import com.qiwenge.android.adapters.base.MyBaseAdapter;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.ui.PagePullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 14-9-20.
 */
public class BaseListFragment<T> extends BaseFragment {

    public PagePullToRefreshListView mListView;

    public List<T> data = new ArrayList<T>();

    public MyBaseAdapter<T> adapter;

    public int pageindex = 1;

    private boolean enableFooterPage = false;

    //EmptyView
    private LinearLayout layoutEmpty;

    public ProgressBar pbLoading;

    private TextView tvEmpty;
    private Button btnEmpty;
    private ImageView ivEmpty;

    private void showEmptyView() {
        if (layoutEmpty == null) return;
        if (data.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
        }
    }

    private void hideEmptyView() {
        if (layoutEmpty != null)
            layoutEmpty.setVisibility(View.GONE);
    }

    public void setEnableEmptyView() {
        layoutEmpty = (LinearLayout) getView().findViewById(R.id.layout_empty);
        layoutEmpty.setVisibility(View.GONE);
        tvEmpty = (TextView) getView().findViewById(R.id.tv_empty);
        tvEmpty.setText("网络不给力，请重试");
        tvEmpty.setVisibility(View.GONE);
        btnEmpty = (Button) getView().findViewById(R.id.btn_empty);
        btnEmpty.setText("重试");
        btnEmpty.setVisibility(View.GONE);
        btnEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData();
            }
        });
        ivEmpty = (ImageView) getView().findViewById(R.id.iv_empty);
        ivEmpty.setVisibility(View.GONE);
    }

    public void setEmptyIcon(int resId) {
        if(ivEmpty!=null){
            ivEmpty.setBackgroundResource(resId);
            ivEmpty.setVisibility(View.VISIBLE);
        }
    }

    public void setRetryTxt(int resId) {

    }

    public void setEmptyMessage(int resId) {
        if (tvEmpty != null) {
            tvEmpty.setText(resId);
            tvEmpty.setVisibility(View.VISIBLE);
        }
    }


    public void setDisablePullToRefresh() {
        if (mListView != null) {
            mListView.setMode(PullToRefreshBase.Mode.DISABLED);
        }
    }

    public void setAdapter() {
        if (mListView != null && adapter != null) {
            mListView.setAdapter(adapter);
            mListView.removePageFooterView();
        }
    }

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

    public void setEnableProgressBar() {
        pbLoading = (ProgressBar) getView().findViewById(R.id.pb_loading);
        pbLoading.setVisibility(View.GONE);
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

        if (pbLoading != null && data.isEmpty() && pageindex == 1)
            pbLoading.setVisibility(View.VISIBLE);

        hideEmptyView();
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

    public void requestFailure() {
        if (mListView != null) {
            mListView.removePageFooterView();
        }

    }

    public void requestFinished() {
        if (mListView != null) {
            mListView.loadFinished();
        }
        if (data.isEmpty()) showEmptyView();

        if (pbLoading != null) pbLoading.setVisibility(View.GONE);
    }

}
