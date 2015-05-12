package com.qiwenge.android.mvp.model;

import com.qiwenge.android.mvp.ui.LegalUi;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.http.JHttpClient;
import com.qiwenge.android.utils.http.StringResponseHandler;

import javax.inject.Inject;

/**
 * Created by Eric on 15/5/12.
 */
public class LegalModel {

    @Inject
    public LegalModel() {
    }

    public void getLegal(final LegalUi ui) {
        String url = ApiUtils.getStatement();
        JHttpClient.get(url, null, new StringResponseHandler() {
            @Override
            public void onSuccess(String result) {
                if (result != null)
                    ui.setLegal(result);
            }
        });
    }

}
