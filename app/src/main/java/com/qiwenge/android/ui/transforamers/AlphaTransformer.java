package com.qiwenge.android.ui.transforamers;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Eric on 14/11/18.
 */
public class AlphaTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View view, float position) {
        System.out.println("position:"+position);
        if (position < -1) { // [-Infinity,-1)
            // 页面正在向左离开屏幕
            view.setAlpha(0);
        } else if (position <= 0) { // [-1,0]
            // 使用正常的滑动效果，
            view.setAlpha(1);
        } else if (position <= 1) { // (0,1]
            // 渐隐
            view.setAlpha(1 - position);
        } else { // (1,+Infinity]
            // 页面向右离开屏幕
            view.setAlpha(position);
        }
    }
}
