package com.qiwenge.android.act;


import android.os.Bundle;

import com.qiwenge.android.R;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.fragments.BookFragment;

public class BookActivity extends BaseActivity {

    public static final String CATEGORY = "category";

    private String searchCategory;

    private BookFragment fragment;

    private boolean hasInited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        fragment = (BookFragment) getSupportFragmentManager().findFragmentById(R.id.bookFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!hasInited) {
            hasInited = true;
            getIntentExtras();
        }
    }

    private void getIntentExtras() {
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            if (extra.containsKey(CATEGORY)) {
                searchCategory = extra.getString(CATEGORY);
                setTitle(searchCategory);
                fragment.search(searchCategory);
            }
        }
    }

}
