package com.qiwenge.android.fragments.bookcity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.liuguangqiang.common.utils.GsonUtils;
import com.liuguangqiang.common.utils.PreferencesUtils;
import com.liuguangqiang.common.utils.StringUtils;
import com.loopj.android.http.RequestParams;
import com.qiwenge.android.act.BookActivity;
import com.qiwenge.android.adapters.CategoryAdapter;
import com.qiwenge.android.base.BaseListFragment;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.entity.Category;
import com.qiwenge.android.entity.CategoryList;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.JsonResponseHandler;

/**
 * 分类。 CategorysFragment
 * <p/>
 * Created by John on 2014年5月31日
 */
public class CategorysFragment extends BaseListFragment<Category> {

    private final String CACHE_CATEGORIES = "cache_categories";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initViews();
        requestData();
    }

    @Override
    public void requestData() {
        super.requestData();
        getCategories();
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

    @Override
    public void initViews() {
        super.initViews();
        adapter = new CategoryAdapter(getActivity(), data);
        setPageable(false);
        setRefreshable(false);
        setAdapter();
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < data.size()) {
                    Bundle extras = new Bundle();
                    extras.putString(BookActivity.CATEGORY, data.get(position).name);
                    startActivity(BookActivity.class, extras);
                }
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
        JHttpClient.get(getActivity(), url, params, new JsonResponseHandler<CategoryList>(CategoryList.class) {

            @Override
            public void onOrigin(String json) {
                if (pageindex == 1)
                    cacheCategories(json);
            }

            @Override
            public void onSuccess(CategoryList result) {
                if (result != null) {
                    requestSuccess(result.result);
                }
            }

            @Override
            public void onStart() {
            }

            @Override
            public void onFinish() {
                requestFinished();
            }

        });
    }


    private void cacheCategories(String json) {
        if (isAdded())
            PreferencesUtils.putString(getActivity(), Constants.PRE_SAVE_NAME, CACHE_CATEGORIES, json);
    }

}
