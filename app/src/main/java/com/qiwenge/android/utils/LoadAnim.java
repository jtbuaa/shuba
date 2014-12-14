package com.qiwenge.android.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.dev1024.utils.LogUtils;
import com.qiwenge.android.R;

/**
 * Created by Eric on 14-8-1.
 */
public class LoadAnim {


    private View mView;

    private Animation mAnimLoading;

    private LoadAnim(){}

    public LoadAnim(View view) {
        this.mView = view;
    }

    public void start() {
        if (mView == null) return;
        if (mAnimLoading == null) {
            mAnimLoading = AnimationUtils.loadAnimation(mView.getContext(), R.anim.roate_loading);
            mAnimLoading.setInterpolator(new LinearInterpolator());
        }
        mView.startAnimation(mAnimLoading);
        LogUtils.i("LoadAnim","start");
    }

    public void cancel() {
        if (mView == null) return;
        LogUtils.i("LoadAnim","hide");
        mView.setVisibility(View.GONE);
        mView.clearAnimation();
    }

}
