package com.qiwenge.android.act;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.liuguangqiang.android.mvp.BaseUi;
import com.liuguangqiang.android.mvp.Presenter;
import com.qiwenge.android.R;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.mvp.presenter.FeedbackPresenter;
import com.qiwenge.android.mvp.ui.FeedbackUi;
import com.qiwenge.android.mvp.ui.FeedbackUiCallback;
import com.qiwenge.android.utils.DialogUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FeedbackActivity extends BaseActivity implements FeedbackUi {

    private static final int ACTION_SEND = 0;

    @InjectView(R.id.et_content)
    EditText etContent;

    private FeedbackUiCallback mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }

    @Override
    public void setUiCallback(FeedbackUiCallback feedbackUiCallback) {
        mCallback = feedbackUiCallback;
    }

    @Override
    public Presenter setPresenter() {
        return new FeedbackPresenter(getApplicationContext(), this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_feedback;
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

    @Override
    public void showLoading() {
        DialogUtils.showLoading(FeedbackActivity.this);
    }

    @Override
    public void hideLoading() {
        DialogUtils.hideLoading();
    }

    @Override
    public void onSuccess() {
        finish();
    }

    private void postFeedback() {
        String content = etContent.getText().toString().trim();
        if (content.length() > 0) {
            mCallback.feedback(content);
        }
    }

}
