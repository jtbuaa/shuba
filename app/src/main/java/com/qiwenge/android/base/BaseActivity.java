package com.qiwenge.android.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.dev1024.utils.IntentUtils;
import com.qiwenge.android.R;
import com.qiwenge.android.utils.MyFilters;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends FragmentActivity {

    private FinishAppReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        showActionBarBack();
        initReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void startActivity(Class<?> cls, Bundle extra) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(extra);
        startActivity(intent);
    }

    private void initActionBar(){
        if (getActionBar() != null && getActionBar().isShowing()) {
            getActionBar().setDisplayUseLogoEnabled(false);
            getActionBar().setDisplayShowHomeEnabled(false);
        }
    }

    /**
     * 显示ActionBar中的返回按钮。
     */
    public void showActionBarBack() {
        if (getActionBar() != null && getActionBar().isShowing()) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 隐藏ActionBar中的返回按钮。
     */
    public void hideActionBarBack() {
        if (getActionBar() != null && getActionBar().isShowing())
            getActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void setContent(Fragment fragment) {
        addFragment(R.id.layout_content, fragment);
    }

    public void addFragment(int resid, Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().add(resid, fragment).commit();
        }
    }

    public void hideFragment(Fragment fragment) {
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
    }

    public void showFragment(Fragment fragment) {
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
    }

    private void initReceiver() {
        receiver = new FinishAppReceiver();
        IntentFilter filter = new IntentFilter(MyFilters.ACTION_QUIT_APP);
        registerReceiver(receiver, filter);
    }

    /**
     * 退出.
     */
    public void exitApp() {
        IntentUtils.sendBroadcast(getApplicationContext(), MyFilters.ACTION_QUIT_APP);
    }

    public class FinishAppReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyFilters.ACTION_QUIT_APP)) {
                System.out.println("finish");
                finish();
            }
        }
    }

}
