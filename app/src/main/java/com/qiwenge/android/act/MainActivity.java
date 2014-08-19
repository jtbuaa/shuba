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
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.dev1024.utils.IntentUtils;
import com.dev1024.utils.LogUtils;
import com.dev1024.utils.NetworkUtils;
import com.qiwenge.android.R;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.fragments.BookCityFragment;
import com.qiwenge.android.fragments.BookshelfFragment;
import com.qiwenge.android.listeners.OnFragmentClickListener;
import com.qiwenge.android.openudid.OpenUDID_manager;
import com.qiwenge.android.utils.ImageLoaderUtils;
import com.qiwenge.android.utils.ThemeUtils;

public class MainActivity extends BaseActivity implements OnClickListener, OnQueryTextListener {

    private static final int ACTION_ITEM_DELETE = 1;

    private BookCityFragment bookCity;
    private BookshelfFragment bookShelf;

    private LinearLayout layoutBookShelf;
    private LinearLayout layoutBookCity;
    private LinearLayout layoutMainMenu;
    private RelativeLayout layoutMainContainer;

    private ImageView ivBookCity;
    private ImageView ivBookShelf;
    private ImageView ivLayer;

    private boolean doubleBackToExitPressedOnce = false;

    private ActionMode actionMode;

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
            case R.id.layout_book_shelf:// 书架
                closeActionMode();
                hideAllFragment();
                showBookShelf();
                break;
            case R.id.layout_book_city:// 书库
                closeActionMode();
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

    @Override
    protected void onResume() {
        super.onResume();
        ThemeUtils.setThemeBg(layoutMainContainer);
        ThemeUtils.setBottomMenuBg(layoutMainMenu);
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
        ivBookShelf.setBackgroundResource(R.drawable.ic_main_menu_fav_s);
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
        ivBookShelf.setBackgroundResource(R.drawable.ic_main_menu_fav_n);
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
        clear();
        bookShelf = new BookshelfFragment();
        addFragment(R.id.layout_content, bookShelf);
        bookShelf.setOnFragmentClickListener(new OnFragmentClickListener() {
            @Override
            public void onClick() {
                //长按Fragment中的item，回调到MainActivity.
                startActionMode(new MainActionMode());
            }
        });
    }

    private void clear() {
        FrameLayout layout = (FrameLayout) this.findViewById(R.id.layout_content);
        layout.removeAllViews();
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

    private void initViews() {
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(false);
        getActionBar().setIcon(R.drawable.ic_action_logo);

        layoutBookShelf = (LinearLayout) this.findViewById(R.id.layout_book_shelf);
        layoutBookCity = (LinearLayout) this.findViewById(R.id.layout_book_city);
        layoutBookShelf.setOnClickListener(this);
        layoutBookCity.setOnClickListener(this);

        ivBookShelf = (ImageView) this.findViewById(R.id.iv_book_shelf);
        ivBookCity = (ImageView) this.findViewById(R.id.iv_book_city);
        ivLayer = (ImageView) this.findViewById(R.id.iv_layer);
        ivLayer.setOnClickListener(this);

        layoutMainContainer = (RelativeLayout) this.findViewById(R.id.layout_main_container);
        layoutMainMenu = (LinearLayout) this.findViewById(R.id.layout_main_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        SearchView searchView = getSearchView(getApplicationContext());
        searchView.setOnQueryTextListener(this);
        menu.add(0, 0, 0, R.string.action_search)
                .setIcon(R.drawable.ic_action_search)
                .setActionView(searchView)
                .setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_ALWAYS
                                | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
                );

        SubMenu submenu = menu.addSubMenu(0, 1, 0,R.string.action_more);
        submenu.add(0, 1, 0, R.string.action_setting).setIntent(
                new Intent(getApplicationContext(), SettingActivity.class));
        submenu.add(0, 2, 1, R.string.action_feedback);
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
        String email = getString(R.string.feedback_email);
        String subject = getString(R.string.feedback_subject);
        String content = getString(R.string.feedback_content);
        String chooserTitle = getString(R.string.feedback_chooser_title);
        IntentUtils.sendEmail(MainActivity.this, email, content, subject, chooserTitle);
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
                bookShelf.deleteSelected();
                mode.finish();
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            bookShelf.clearAllSelect();
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


    public static SearchView getSearchView(Context context) {
        SearchView searchView = new SearchView(context);
        // 修改字体颜色
        int id = searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        AutoCompleteTextView textView = (AutoCompleteTextView) searchView
                .findViewById(id);
        textView.setTextColor(context.getResources().getColor(android.R.color.white));
        searchView.setQueryHint(context.getString(R.string.action_search_hint));
        // Background
        int searchPlateId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);
        searchPlate.setBackgroundResource(R.drawable.search_view_bg);
        return searchView;
    }

}
