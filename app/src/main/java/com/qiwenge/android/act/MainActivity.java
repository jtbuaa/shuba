package com.qiwenge.android.act;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.liuguangqiang.common.utils.NetworkUtils;
import com.qiwenge.android.R;
import com.qiwenge.android.adapters.MainMenuAdapter;
import com.qiwenge.android.adapters.MainPagerAdapter;
import com.qiwenge.android.async.AsyncCheckUpdate;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.entity.MainMenuItem;
import com.qiwenge.android.ui.SlowViewPager;
import com.qiwenge.android.utils.ImageLoaderUtils;
import com.qiwenge.android.utils.level.LevelUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements OnClickListener {

    private static final int ACTION_ITEM_DELETE = 1;

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

        initViews();
        initActionBar();
        chkUpdate();
        LevelUtils.dailyLogin(getApplicationContext());

//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().setExitTransition(new Explode());
//        }

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
    public void onBackPressed() {
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
    }

    private void initMenu() {
        String[] titles = getResources().getStringArray(R.array.main_menu_titles);

        int[] iconNormal = {R.drawable.ic_main_menu_bookshelf_n,
                R.drawable.ic_main_menu_bookcity_n, R.drawable.ic_main_menu_me_n};

        int[] iconSelected = {R.drawable.ic_main_menu_bookshelf_s,
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

        ivLayer = (ImageView) this.findViewById(R.id.iv_layer);
        ivLayer.setOnClickListener(this);
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
                mode.finish();
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }

    }

    private void closeActionMode() {
        if (actionMode != null) {
            actionMode.finish();
            actionMode = null;
        }
    }


}
