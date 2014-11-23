package com.qiwenge.android.act;

import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import com.qiwenge.android.R;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.ThemeUtils;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.StringResponseHandler;

public class LegalActivity extends BaseActivity {

    private ScrollView scrollView;

    private TextView tvLegal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal);
        initViews();
        getStatement();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ThemeUtils.setThemeBg(scrollView);
    }

    private void initViews() {
        scrollView = (ScrollView) this.findViewById(R.id.scrollView_container);
        tvLegal = (TextView) this.findViewById(R.id.tv_legal);
        ThemeUtils.setTextColor(tvLegal);
    }

    private void getStatement() {
        String url = ApiUtils.getStatement();
        JHttpClient.get(url, null, new StringResponseHandler() {
            @Override
            public void onSuccess(String result) {
                if (result != null)
                    tvLegal.setText(result);
            }
        });
    }

}
