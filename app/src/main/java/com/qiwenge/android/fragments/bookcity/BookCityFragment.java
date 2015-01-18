package com.qiwenge.android.fragments.bookcity;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.qiwenge.android.R;
import com.qiwenge.android.adapters.BookCityAdapter;
import com.qiwenge.android.adapters.TabAdapter;
import com.qiwenge.android.base.BaseFragment;
import com.qiwenge.android.entity.MainMenuItem;
import com.qiwenge.android.ui.SlowViewPager;
import com.viewpagerindicator.UnderlinePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 书城。 BookCityFragment
 * <p/>
 * Created by John on 2014-5-31
 */
public class BookCityFragment extends BaseFragment {

    private SlowViewPager viewPager;
    private BookCityAdapter adpater;

    private GridView gvTab;
    private TabAdapter tabAdapter;
    private List<MainMenuItem> tabItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_city, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initTabItems() {
        String[] tabs = getResources().getStringArray(R.array.book_city_tabs);
        MainMenuItem item;
        for (String tab : tabs) {
            item = new MainMenuItem();
            item.title = tab;
            tabItems.add(item);
        }
        tabItems.get(0).selected = true;
    }

    private void initViews() {
        initTabItems();
        gvTab = (GridView) getView().findViewById(R.id.gv_tab);
        tabAdapter = new TabAdapter(getActivity(), tabItems);
        gvTab.setAdapter(tabAdapter);
        gvTab.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewPager.setCurrentItem(position);
            }
        });
        gvTab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getAction() == MotionEvent.ACTION_MOVE ? true : false;
            }
        });

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
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void selectePage(int position) {
        for (MainMenuItem item : tabItems) {
            item.selected = false;
        }
        tabItems.get(position).selected = true;
        tabAdapter.notifyDataSetChanged();
    }

}
