package com.qiwenge.android.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qiwenge.android.fragments.bookcity.CategorysFragment;
import com.qiwenge.android.fragments.bookcity.RankFragment;
import com.qiwenge.android.fragments.bookcity.RecommendFragment;

public class BookCityAdapter extends FragmentPagerAdapter {

    private RecommendFragment recommend;
    private RankFragment rank;
    private CategorysFragment category;

    public BookCityAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int arg0) {
        switch (arg0) {
            case 0:
                if (rank == null) rank = new RankFragment();
                return rank;
            case 1:
                if (recommend == null) recommend = new RecommendFragment();
                return recommend;
            case 2:
                if (category == null) category = new CategorysFragment();
                return category;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    /**
     * 刷新Fragment数据。
     *
     * @param position
     */
    public void refresh(int position) {
        switch (position) {
            case 0:
//                if (rank != null) rank.refresh();
                break;
            case 1:
//                if (recommend != null) recommend.refresh();
                break;
            case 2:
//                if (category != null) category.refresh();
                break;
            default:
                break;
        }
    }

}
