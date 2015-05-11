package com.qiwenge.android.act;

import android.os.Bundle;

import com.qiwenge.android.R;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.fragments.SettingFragment;

/**
 * 设置。
 * <p/>
 * Created by Eric on 2014-7-9
 */
public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.set_title);
        replaceFragment(new SettingFragment());
    }
}
