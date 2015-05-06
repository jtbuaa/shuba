package com.qiwenge.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qiwenge.android.R;
import com.qiwenge.android.base.BaseFragment;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.ThemeUtils;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.StringResponseHandler;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LegalFragment extends BaseFragment {

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getStatement();
    }

    public void initViews() {
        tvLegal = (TextView) getView().findViewById(R.id.tv_legal);
        ThemeUtils.setTextColor(tvLegal);
    }

    private void getStatement() {
        String url = ApiUtils.getStatement();
        JHttpClient.get(getActivity(), url, null, new StringResponseHandler() {
            @Override
            public void onSuccess(String result) {
                if (result != null)
                    tvLegal.setText(result);
            }
        });
    }

}


