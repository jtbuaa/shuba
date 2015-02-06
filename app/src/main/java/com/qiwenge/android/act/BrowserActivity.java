package com.qiwenge.android.act;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.qiwenge.android.R;
import com.qiwenge.android.base.BaseActivity;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.ui.dialogs.MirrorDialog;

/**
 * 网页浏览器。
 *
 * @author Eric
 */
public class BrowserActivity extends BaseActivity {

    private static final int ACTION_ITEM_SOURCE = 1;

    private Book book;

    public static final String EXTRA_TITLE = "EXTRA_TITLE";

    public static final String EXTRA_URL = "EXTRA_URL";

    public static final String EXTRA_BOOK = "EXTRA_BOOK";

    private WebView webview = null;
    private ProgressBar pbLoading;

    private String url;

    private MirrorDialog sourceDialog;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        getIntentParams();
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, ACTION_ITEM_SOURCE, 0, "换源").setIcon(R.drawable.ic_action_source).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == ACTION_ITEM_SOURCE) {
//            if (sourceDialog == null) {
//                sourceDialog = new SourceDialog(this, book);
//            }
//            sourceDialog.show(true);
        }

        return super.onOptionsItemSelected(item);
    }

    private void getIntentParams() {
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();

        if (extra != null && extra.containsKey(EXTRA_TITLE)) {
            setTitle(extra.getString(EXTRA_TITLE));
        }

        if (extra != null && extra.containsKey(EXTRA_URL)) {
            url = extra.getString(EXTRA_URL);
        }

        if (extra != null && extra.containsKey(EXTRA_BOOK)) {
            book = extra.getParcelable(EXTRA_BOOK);
        }
    }

    /**
     * 初始化视图。
     */
    private void initViews() {
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress > 50) {
                    pbLoading.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        webview.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    private void back() {
        if (webview != null && webview.canGoBack()) {
            webview.goBack();
        } else {
            if (webview != null) {
                webview.stopLoading();
            }
            webview = null;
            finish();
        }
    }

}
