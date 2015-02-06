package com.qiwenge.android.ui.dialogs;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

import com.qiwenge.android.adapters.SourceAdapter;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.entity.Mirror;

import java.util.ArrayList;
import java.util.List;

/**
 * 换源。
 * <p/>
 * Created by Eric on 14-10-12.
 */
public class MirrorDialog {

    private MyDialog mDialog;

    private List<Mirror> data = new ArrayList<Mirror>();

    private Book mBook;

    private boolean isSkip = false;

    private SourceAdapter adapter;

    public MirrorDialog(final Activity act, final Book book, List<Mirror> mirrors) {
        this.mBook = book;
        data.clear();
        data.addAll(mirrors);
        mDialog = new MyDialog(act, "选择来源");
        adapter = new SourceAdapter(act.getApplication(), data);
        mDialog.setItems(adapter, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    private void showSeleted(int position) {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).current = false;
        }
        data.get(position).current = true;
        adapter.notifyDataSetChanged();
    }

    public void show() {
        if (mDialog != null) mDialog.show();
    }

    public void show(boolean isSkip) {
        this.isSkip = isSkip;
        if (mDialog != null) mDialog.show();
    }


    public void dismiss() {
        if (mDialog != null) mDialog.dismiss();
    }

}
