package com.qiwenge.android.act;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.qiwenge.android.R;
import com.qiwenge.android.base.BaseActivity;

public class FeedbackActivity extends BaseActivity {

    private static final int ACTION_SEND = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(ACTION_SEND, 0, 0, R.string.action_send)
                .setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_ALWAYS
                                | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
                );
        return super.onCreateOptionsMenu(menu);
    }


}
