package com.qiwenge.android.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qiwenge.android.fragments.BookCityFragment;
import com.qiwenge.android.fragments.BookshelfFragment;

/**
 * 首页Fragment适配器。
 * 
 * Created by John on 2014-5-15
 */
public class MainFragmentAdapter extends FragmentPagerAdapter {

    private BookshelfFragment bookshelfFragment;

    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int arg0) {
        switch (arg0) {
            case 0:
                if (bookshelfFragment == null) bookshelfFragment = new BookshelfFragment();
                return bookshelfFragment;
            case 1:
                return new BookCityFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

}
