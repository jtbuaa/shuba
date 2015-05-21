/*
 * Copyright 2015 qiwenge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qiwenge.android.act;

import android.os.Bundle;

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
        fragment = new BookFragment();
        replaceFragment(fragment);
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
