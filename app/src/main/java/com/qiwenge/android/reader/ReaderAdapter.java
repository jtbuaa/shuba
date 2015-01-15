package com.qiwenge.android.reader;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qiwenge.android.R;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.entity.Page;
import com.qiwenge.android.utils.ThemeUtils;

/**
 * 阅读适配器。Created by John at 2014.
 */
public class ReaderAdapter extends PagerAdapter {

    private List<Page> pages = new ArrayList<Page>();
    private LayoutInflater mInflater;
    private int mTextSize = Constants.DEFAULT_TEXT_SIZE;

    public ReaderAdapter(Context context, List<Page> pages) {
        this.pages = pages;
        mInflater = LayoutInflater.from(context);
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        Log.i("ReaderAdapter", "instantiateItem");
        View view = mInflater.inflate(R.layout.item_reader, container, false);
        TextView tv = (TextView) view.findViewById(R.id.tv_reader);
        tv.setText(pages.get(position).content);
        tv.setTextSize(mTextSize);
        ThemeUtils.setTextColor(tv);
        container.addView(view, 0);
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.i("ReaderAdapter", "destroyItem");
        final View view = (View) object;
        container.removeView(view);
    }


}
