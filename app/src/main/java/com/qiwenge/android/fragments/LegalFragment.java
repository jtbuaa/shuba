package com.qiwenge.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liuguangqiang.android.mvp.BaseUi;
import com.liuguangqiang.android.mvp.Presenter;
import com.qiwenge.android.R;
import com.qiwenge.android.base.BaseFragment;
import com.qiwenge.android.mvp.presenter.LegalPresenter;
import com.qiwenge.android.mvp.ui.LegalUi;
import com.qiwenge.android.mvp.ui.LegalUiCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LegalFragment extends BaseFragment implements LegalUi {

    @InjectView(R.id.tv_legal)
    TextView tvLegal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_legal, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public Presenter setPresenter() {
        return new LegalPresenter(getActivity());
    }

    @Override
    public BaseUi setUi() {
        return this;
    }

    @Override
    public void setUiCallback(LegalUiCallback legalUiCallback) {

    }

    public void initViews() {
        tvLegal = (TextView) getView().findViewById(R.id.tv_legal);
    }

    @Override
    public void setLegal(String legal) {
        tvLegal.setText(legal);
    }

}


