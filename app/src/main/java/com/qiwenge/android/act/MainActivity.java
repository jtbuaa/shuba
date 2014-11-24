package com.qiwenge.android.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev1024.utils.LogUtils;
import com.dev1024.utils.NetworkUtils;
import com.qiwenge.android.R;
import com.qiwenge.android.adapters.MainMenuAdapter;
import com.qiwenge.android.adapters.MainPagerAdapter;
import com.qiwenge.android.async.AsyncCheckUpdate;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.listeners.OnFragmentClickListener;
import com.qiwenge.android.login.SinaWeiboLogin;
import com.qiwenge.android.models.MainMenuItem;
import com.qiwenge.android.openudid.OpenUDID_manager;
import com.qiwenge.android.ui.transforamers.AlphaTransformer;
import com.qiwenge.android.ui.transforamers.MyTransformer;
import com.qiwenge.android.ui.SlowViewPager;
import com.qiwenge.android.ui.dialogs.LoginDialog;
import com.qiwenge.android.utils.ImageLoaderUtils;
import com.qiwenge.android.utils.ThemeUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements OnClickListener {

    private static final int ACTION_ITEM_DELETE = 1;

    private RelativeLayout layoutMainContainer;

    private ImageView ivLayer;

    private SlowViewPager viewPager;

    private MainPagerAdapter adapter;

    private boolean doubleBackToExitPressedOnce = false;

    private ActionMode actionMode;

    private GridView gvMenu;
    private MainMenuAdapter menuAdapter;
    private List<MainMenuItem> menuData = new ArrayList<MainMenuItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!NetworkUtils.isWifiEnabled(getApplicationContext())) {
            ImageLoaderUtils.setWifiEnable(false);
        }
        ImageLoaderUtils.init(getApplicationContext());
        initViews();
        initFragment();
        getOpenUDID(getApplicationContext());
        initActionBar();
        chkUpdate();
    }

    private void chkUpdate() {
        AsyncCheckUpdate update = new AsyncCheckUpdate(this);
        update.setOnlyCheck();
        update.checkUpdate();
    }

    private void initActionBar() {
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setIcon(R.drawable.transparent_bg);
        getActionBar().setCustomView(R.layout.layout_main_action);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        LogUtils.i("main", "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        LogUtils.i("main", "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        System.out.println("onBackPressed");
        if (doubleBackToExitPressedOnce) {
            exitApp();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.double_click_to_quit), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_layer:// 点击遮罩层
                invalidateOptionsMenu();
                ivLayer.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ThemeUtils.setThemeBg(layoutMainContainer);
    }

    private void initFragment() {
        adapter.setOnFragmentClickListener(new OnFragmentClickListener() {
            @Override
            public void onClick() {
                //长按Fragment中的item，回调到MainActivity.
                startActionMode(new MainActionMode());
            }
        });
    }


    /**
     * 获取设备唯一标识
     *
     * @param context
     * @return
     */
    private String getOpenUDID(Context context) {
        OpenUDID_manager.sync(context);
        OpenUDID_manager.isInitialized();
        return OpenUDID_manager.getOpenUDID();
    }

    private void initMenu() {
        String[] titles = {"收藏", "书城", "我"};
        int[] iconNormal = {R.drawable.ic_main_menu_fav_n,
                R.drawable.ic_main_menu_bookcity_n, R.drawable.ic_main_menu_me_n};
        int[] iconSelected = {R.drawable.ic_main_menu_fav_s,
                R.drawable.ic_main_menu_bookcity_s, R.drawable.ic_main_menu_me_s};
        MainMenuItem item;
        for (int i = 0; i < titles.length; i++) {
            item = new MainMenuItem();
            item.title = titles[i];
            item.icon = iconNormal[i];
            item.iconSelected = iconSelected[i];
            menuData.add(item);
        }
        menuData.get(0).selected = true;
        menuAdapter = new MainMenuAdapter(getApplicationContext(), menuData);
        gvMenu = (GridView) this.findViewById(R.id.gv_menu);
        gvMenu.setAdapter(menuAdapter);
        gvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedMenu(position);
                viewPager.setCurrentItem(position);
            }
        });
    }

    private int lastPosition = 0;

    private void selectedMenu(int position) {
        menuData.get(lastPosition).selected = false;
        menuData.get(position).selected = true;
        lastPosition = position;
        menuAdapter.notifyDataSetChanged();
        closeActionMode();
    }

    private void initViews() {
        initMenu();

        adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager = (SlowViewPager) this.findViewById(R.id.viewpager_main);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(MainPagerAdapter.NUM - 1);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedMenu(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapter.setOnFragmentClickListener(new OnFragmentClickListener() {
            @Override
            public void onClick() {
                startActionMode(new MainActionMode());
            }
        });

        ivLayer = (ImageView) this.findViewById(R.id.iv_layer);
        ivLayer.setOnClickListener(this);

        layoutMainContainer = (RelativeLayout) this.findViewById(R.id.layout_main_container);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        menu.add(0, 0, 0, R.string.action_search)
                .setIcon(R.drawable.ic_action_search).setIntent(new Intent(getApplicationContext(),
                SearchActivity.class)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        SubMenu submenu = menu.addSubMenu(0, 1, 0, R.string.action_more);

        submenu.add(0, 1, 2, R.string.action_setting).setIntent(
                new Intent(getApplicationContext(), SettingActivity.class));

        submenu.add(0, 2, 1, R.string.action_feedback);

        submenu.getItem().setIcon(R.drawable.ic_action_more)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 2:
                sendFeedback();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendFeedback() {
        startActivity(FeedbackActivity.class);
    }

    /**
     * Main ActionMode
     * <p/>
     * Created by John on 2014年6月25日
     */
    public class MainActionMode implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            menu.add(0, ACTION_ITEM_DELETE, 1, R.string.remove_from_bookshelf).setIcon(R.drawable.ic_action_mode_delete)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mode.setTitle(R.string.remove_from_bookshelf);
            actionMode = mode;
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            if (item.getItemId() == ACTION_ITEM_DELETE) {
                adapter.deleteSelected();
                mode.finish();
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearAllSelect();
            actionMode = null;
        }

    }

    private void closeActionMode() {
        if (actionMode != null) {
            actionMode.finish();
            actionMode = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult");
        if (SinaWeiboLogin.mSsoHandler != null && data != null) {
            SinaWeiboLogin.mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

}
