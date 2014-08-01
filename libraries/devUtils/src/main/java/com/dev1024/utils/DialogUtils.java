package com.dev1024.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.LayoutInflater;
import android.view.View;

import com.dev1024.johnliu.R;

public class DialogUtils {

    private static Dialog loadingDialog = null;

    public static void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    public static void showLoading(Activity act) {
        if (loadingDialog == null) loadingDialog = buildLoading(act);
        loadingDialog.show();
    }

    public static void showLoadingWithoutBg(Activity act) {
        if (loadingDialog == null) loadingDialog = buildLoadingWithoutBg(act);
        loadingDialog.show();
    }

    private static Dialog buildLoading(Activity context) {
        loadingDialog = new Dialog(context, R.style.dialog);
        View progressView = LayoutInflater.from(context).inflate(R.layout.loading, null);
        loadingDialog.setContentView(progressView);
        loadingDialog.setCancelable(true);
        loadingDialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                loadingDialog = null;
            }
        });
        return loadingDialog;
    }

    private static Dialog buildLoadingWithoutBg(Activity context) {
        loadingDialog = new Dialog(context, R.style.dialog);
        View progressView = LayoutInflater.from(context).inflate(R.layout.loading_without_bg, null);
        loadingDialog.setContentView(progressView);
        loadingDialog.setCancelable(true);
        loadingDialog.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                loadingDialog = null;
            }
        });
        return loadingDialog;
    }

}
