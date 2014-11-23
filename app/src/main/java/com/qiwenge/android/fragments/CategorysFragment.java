package com.qiwenge.android.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dev1024.utils.GsonUtils;
import com.dev1024.utils.PreferencesUtils;
import com.dev1024.utils.StringUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;
import com.qiwenge.android.R;
import com.qiwenge.android.act.BookActivity;
import com.qiwenge.android.act.SearchActivity;
import com.qiwenge.android.adapters.CategoryAdapter;
import com.qiwenge.android.base.BaseFragment;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.models.Category;
import com.qiwenge.android.models.CategoryList;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.JsonResponseHandler;

/**
 * 分类。 CategorysFragment
 * <p/>
 * Created by John on 2014年5月31日
 */
public class CategorysFragment extends BaseFragment {

    private final String CACHE_CATEGORIES = "cache_categories";

    private PullToRefreshListView gvCategory;

    private CategoryAdapter adapter;

    private List<Category> data = new ArrayList<Category>();

    //EmptyView
    private LinearLayout layoutEmpty;

    private ProgressBar pbLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categorys, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEmptyView();
        initData();
        initViews();
        getCategories();
    }

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

    private void initEmptyView() {
        layoutEmpty = (LinearLayout) getView().findViewById(R.id.layout_empty);
        layoutEmpty.setVisibility(View.GONE);
        TextView tvEmpty = (TextView) getView().findViewById(R.id.tv_empty);
        tvEmpty.setText("网络不给力，请重试");
        Button btnEmpty = (Button) getView().findViewById(R.id.btn_empty);
        btnEmpty.setText("重试");
        btnEmpty.setVisibility(View.VISIBLE);
        btnEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCategories();
            }
        });
    }

    private void initData() {
        String json =
                PreferencesUtils
                        .getString(getActivity(), Constants.PRE_SAVE_NAME, CACHE_CATEGORIES);
        if (StringUtils.isEmptyOrNull(json)) return;

        CategoryList result = GsonUtils.getModel(json, CategoryList.class);
        if (result != null) {
            data.clear();
            data.addAll(result.result);
        }
    }

    private void initViews() {
        pbLoading = (ProgressBar) getView().findViewById(R.id.pb_loading);
        pbLoading.setVisibility(View.GONE);
        adapter = new CategoryAdapter(getActivity(), data);
        gvCategory = (PullToRefreshListView) getView().findViewById(R.id.gv_category);
        gvCategory.setAdapter(adapter);
        gvCategory.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle extras = new Bundle();
                extras.putString(BookActivity.CATEGORY, data.get(position - 1).name);
                startActivity(BookActivity.class, extras);
            }
        });

        gvCategory.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getCategories();
            }
        });
    }

    /**
     * 获取分类。
     */
    private void getCategories() {
        String url = ApiUtils.getCategories();
        RequestParams params = new RequestParams();
        params.put("limit", "100");

        JHttpClient.get(url, params, new JsonResponseHandler<CategoryList>(CategoryList.class) {

            @Override
            public void onOrigin(String json) {
                cacheCategories(json);
            }

            @Override
            public void onSuccess(CategoryList result) {
                if (result != null && adapter != null) {
                    data.clear();
                    adapter.add(result.result);
                }
            }

            @Override
            public void onStart() {
                hideEmptyView();
                if (data.isEmpty()) pbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                gvCategory.onRefreshComplete();
                if (data.isEmpty()) {
                    showEmptyView();
                }
                pbLoading.setVisibility(View.GONE);
            }

        });
    }


    private void cacheCategories(String json) {
        System.out.println("缓存分类");
        PreferencesUtils.putString(getActivity(), Constants.PRE_SAVE_NAME, CACHE_CATEGORIES, json);
    }

}
