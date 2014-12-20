package com.qiwenge.android.act;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.liuguangqiang.common.utils.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.qiwenge.android.R;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.utils.ApiUtils;
import com.qiwenge.android.utils.http.BaseResponseHandler;
import com.qiwenge.android.utils.http.JHttpClient;


public class FeedbackActivity extends BaseActivity {

    private static final int ACTION_SEND = 0;

    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(ACTION_SEND, 0, 0, R.string.action_send)
                .setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_ALWAYS
                                | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
                );
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ACTION_SEND:
                postFeedback();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        etContent = (EditText) this.findViewById(R.id.et_content);
    }

    private void postFeedback() {
        if (etContent.getText().toString().trim().length() == 0) return;

        String url = ApiUtils.postFeedBack();
        RequestParams params = new RequestParams();
        params.put("content", etContent.getText().toString().trim());
        JHttpClient.post(url, params, new BaseResponseHandler() {

            @Override
            public void onStart() {
                //TODO DialogUtils
//                DialogUtils.showLoading(FeedbackActivity.this);
            }

            @Override
            public void onSuccess(String result) {
                ToastUtils.show(getApplicationContext(), "感谢你的反馈!");
                finish();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(getApplicationContext(), "发送失败");
            }

            @Override
            public void onFinish() {
//                DialogUtils.hideLoading();
            }
        });
    }

}
