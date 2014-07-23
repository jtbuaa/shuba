package com.qiwenge.android.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qiwenge.android.R;
import com.qiwenge.android.adapters.BookCityAdapter;
import com.qiwenge.android.base.BaseFragment;
import com.qiwenge.android.ui.SlowViewPager;
import com.viewpagerindicator.UnderlinePageIndicator;

/**
 * 书城。 BookCityFragment
 * 
 * Created by John on 2014-5-31
 */
public class BookCityFragment extends BaseFragment implements OnClickListener {

    private SlowViewPager viewPager;
    private BookCityAdapter adpater;
    private TextView tvRecommend;
    private TextView tvRank;
    private TextView tvCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_city, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        tvRecommend = (TextView) getView().findViewById(R.id.tv_recommend);
        tvRank = (TextView) getView().findViewById(R.id.tv_rank);
        tvCategory = (TextView) getView().findViewById(R.id.tv_category);
        tvRecommend.setOnClickListener(this);
        tvRank.setOnClickListener(this);
        tvCategory.setOnClickListener(this);

        adpater = new BookCityAdapter(getChildFragmentManager());
        viewPager = (SlowViewPager) getView().findViewById(R.id.viewpager_book_city);
        viewPager.setAdapter(adpater);
        viewPager.setOffscreenPageLimit(2);
        UnderlinePageIndicator indicator =
                (UnderlinePageIndicator) getView().findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        indicator.setFades(false);
        indicator.setSelectedColor(getResources().getColor(R.color.main_dress_color));
        indicator.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                selectePage(arg0);
                adpater.refresh(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}

            @Override
            public void onPageScrollStateChanged(int arg0) {}
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_recommend:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tv_rank:
                viewPager.setCurrentItem(1);
                break;
            case R.id.tv_category:
                viewPager.setCurrentItem(2);
                break;

            default:
                break;
        }
    }

    private void selectePage(int position) {
        clearSelected();
        switch (position) {
            case 0:
                tvRecommend.setTextColor(getResources().getColor(R.color.main_dress_color));
                break;
            case 1:
                tvRank.setTextColor(getResources().getColor(R.color.main_dress_color));
                break;
            case 2:
                tvCategory.setTextColor(getResources().getColor(R.color.main_dress_color));
                break;

            default:
                break;
        }
    }

    private void clearSelected() {
        tvRecommend.setTextColor(getResources().getColor(R.color.tv_desc_color));
        tvRank.setTextColor(getResources().getColor(R.color.tv_desc_color));
        tvCategory.setTextColor(getResources().getColor(R.color.tv_desc_color));
    }



}
