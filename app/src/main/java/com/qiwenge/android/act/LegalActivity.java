package com.qiwenge.android.act;

import android.os.Bundle;
import android.widget.TextView;

import com.qiwenge.android.R;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.ThemeUtils;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.StringResponseHandler;

/**
 * 免责声明
 */
public class LegalActivity extends BaseActivity {

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
    }

    private void initViews() {
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
