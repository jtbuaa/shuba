package com.qiwenge.android.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.dev1024.utils.NetworkUtils;
import com.qiwenge.android.R;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.fragments.BookCityFragment;
import com.qiwenge.android.fragments.BookshelfFragment;
import com.qiwenge.android.listeners.OnFragmentClickListener;
import com.qiwenge.android.openudid.OpenUDID_manager;
import com.qiwenge.android.utils.ImageLoaderUtils;

public class MainActivity extends BaseActivity implements OnClickListener, OnQueryTextListener {

    private BookCityFragment bookCity;
    private BookshelfFragment bookShelf;

    private LinearLayout layoutBookShelf;
    private LinearLayout layoutBookCity;

    private ImageView ivBookCity;
    private ImageView ivBookShelf;
    private ImageView ivLayer;

    private boolean doubleBackToExitPressedOnce = false;

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
            case R.id.layout_book_shelf:// 书架
                hideAllFragment();
                showBookShelf();
                break;
            case R.id.layout_book_city:// 书库
                hideAllFragment();
                showBookCity();
                break;
            case R.id.iv_layer:// 点击遮罩层
                invalidateOptionsMenu();
                ivLayer.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void showBookShelf() {
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();

        if (bookShelf != null) {
            tran.show(bookShelf);
        } else {
            bookShelf = new BookshelfFragment();
            addFragment(R.id.layout_content, bookShelf);
        }
        tran.commit();
        getSupportFragmentManager().executePendingTransactions();
        ivBookShelf.setBackgroundResource(R.drawable.icon_main_bookshelf_s);
        ivBookCity.setBackgroundResource(R.drawable.icon_main_bookcity_n);
    }

    private void showBookCity() {
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();

        if (bookCity != null) {
            tran.show(bookCity);
        } else {
            bookCity = new BookCityFragment();
            addFragment(R.id.layout_content, bookCity);
        }
        tran.commit();
        getSupportFragmentManager().executePendingTransactions();
        ivBookShelf.setBackgroundResource(R.drawable.icon_main_bookshelf_n);
        ivBookCity.setBackgroundResource(R.drawable.icon_main_bookcity_s);
    }

    /**
     * 隐藏所有的Fragment
     */
    private void hideAllFragment() {
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        if (bookShelf != null) tran.hide(bookShelf);

        if (bookCity != null) tran.hide(bookCity);
        tran.commit();
    }

    private void initFragment() {
        bookShelf = new BookshelfFragment();
        addFragment(R.id.layout_content, bookShelf);
        bookShelf.setOnFragmentClickListener(new OnFragmentClickListener() {
            @Override
            public void onClick() {
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
    public static String getOpenUDID(Context context) {
        OpenUDID_manager.sync(context);
        OpenUDID_manager.isInitialized();
        return OpenUDID_manager.getOpenUDID();
    }

    private void initViews() {
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(false);

        layoutBookShelf = (LinearLayout) this.findViewById(R.id.layout_book_shelf);
        layoutBookCity = (LinearLayout) this.findViewById(R.id.layout_book_city);
        layoutBookShelf.setOnClickListener(this);
        layoutBookCity.setOnClickListener(this);

        ivBookShelf = (ImageView) this.findViewById(R.id.iv_book_shelf);
        ivBookCity = (ImageView) this.findViewById(R.id.iv_book_city);
        ivLayer = (ImageView) this.findViewById(R.id.iv_layer);
        ivLayer.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        SearchView searchView = new SearchView(getApplicationContext());
        searchView.setOnQueryTextListener(this);
        menu.add(0, 0, 0, "搜索")
                .setIcon(R.drawable.ic_action_search)
                .setActionView(searchView)
                .setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_ALWAYS
                                | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
                );

        SubMenu submenu = menu.addSubMenu(0, 1, 0, "More");
        submenu.add(0, 1, 0, "设置").setIntent(
                new Intent(getApplicationContext(), SettingActivity.class));
        submenu.add(0, 2, 1, "反馈");
        submenu.getItem().setIcon(R.drawable.ic_action_more)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.findItem(0).setOnActionExpandListener(new OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                menu.findItem(1).setVisible(false);
                ivLayer.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                menu.findItem(1).setVisible(true);
                ivLayer.setVisibility(View.GONE);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Main ActionMode
     * <p/>
     * Created by John on 2014年6月25日
     */
    public class MainActionMode implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            menu.add(0, 1, 1, "DELETE").setIcon(R.drawable.btn_nav_delete)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Bundle extra = new Bundle();
        extra.putString(SearchActivity.KEYWORD, query);
        startActivity(SearchActivity.class, extra);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


}
