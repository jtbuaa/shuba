package com.qiwenge.android.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qiwenge.android.fragments.BookCityFragment;
import com.qiwenge.android.fragments.BookshelfFragment;
import com.qiwenge.android.fragments.MeFragment;
import com.qiwenge.android.listeners.OnFragmentClickListener;

/**
 * 书城适配器。
 * <p/>
 * Created by John on 2014-5-15
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    public static final int NUM = 3;

    private BookCityFragment bookCity;
    private BookshelfFragment bookShelf;

    private OnFragmentClickListener clickListener;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int arg0) {
        switch (arg0) {
            case 0:
                if (bookShelf == null) {
                    bookShelf = new BookshelfFragment();
                    if (clickListener != null) bookShelf.setOnFragmentClickListener(clickListener);
                }
                return bookShelf;
            case 1:
                if (bookCity == null) bookCity = new BookCityFragment();
                return bookCity;
            case 2:
                return new MeFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM;
    }

    public void setOnFragmentClickListener(OnFragmentClickListener listener) {
        this.clickListener = listener;
    }

    public void clearAllSelect() {
        if (bookShelf != null) bookShelf.clearAllSelect();
    }

    public void deleteSelected() {
        if (bookShelf != null) bookShelf.deleteSelected();
    }

}
