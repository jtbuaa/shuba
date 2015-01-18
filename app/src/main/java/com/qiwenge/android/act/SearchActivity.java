package com.qiwenge.android.act;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.qiwenge.android.R;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.fragments.SearchFragment;

import android.app.ActionBar.LayoutParams;

/**
 * 搜索。
 * <p/>
 * Created by Eric on 2014-7-6
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private EditText etSearch;
    private ImageView btnSearch;

    private SearchFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initActionBar();
        fragment = new SearchFragment();
        setContent(fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                search();
                break;
        }
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null && actionBar.isShowing()) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            View view = LayoutInflater.from(this).inflate(R.layout.action_bar_search, null);
            etSearch = (EditText) view.findViewById(R.id.et_search);
            etSearch.requestFocus();
            btnSearch = (ImageView) view.findViewById(R.id.iv_search);
            btnSearch.setOnClickListener(this);
            etSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            etSearch.setOnKeyListener(new View.OnKeyListener() {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (KeyEvent.KEYCODE_ENTER == keyCode
                            && event.getAction() == KeyEvent.ACTION_DOWN) {
                        search();
                        return true;
                    }
                    return false;
                }
            });
            LayoutParams params =
                    new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            actionBar.setCustomView(view, params);
        }
    }

    private void search() {
        if (etSearch.getText().toString().trim().length() == 0) return;
        hideKeyborad();
        fragment.search(etSearch.getText().toString().trim());
    }

    private void hideKeyborad() {
        InputMethodManager imm =
                (InputMethodManager) getApplicationContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
    }

}
