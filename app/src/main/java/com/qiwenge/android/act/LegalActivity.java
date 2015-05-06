package com.qiwenge.android.act;

import android.os.Bundle;

import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.fragments.LegalFragment;


public class LegalActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        replaceFragment(new LegalFragment());
    }

}
