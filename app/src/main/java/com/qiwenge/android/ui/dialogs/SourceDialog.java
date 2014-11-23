package com.qiwenge.android.ui.dialogs;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.qiwenge.android.R;
import com.qiwenge.android.adapters.SourceAdapter;
import com.qiwenge.android.constant.Constants;
import com.qiwenge.android.models.Book;
import com.qiwenge.android.models.Source;
import com.qiwenge.android.ui.dialogs.MyDialog;
import com.qiwenge.android.utils.SkipUtils;
import com.qiwenge.android.utils.SourceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 换源。
 * <p/>
 * Created by Eric on 14-10-12.
 */
public class SourceDialog {

    //贴吧
    private final String FORMAT_TIEBA = "http://tieba.baidu.com/f?kw=%s";

    //百度搜索
    private final String FORMAT_BAIDU = "http://www.baidu.com/s?cl=3&wd=%s";

    //宜搜
    private final String FORMAT_EASOU = "http://book.easou.com/ta/search.m?q=%s";

    private MyDialog mDialog;

    private List<Source> data = new ArrayList<Source>();

    private Book mBook;

    private boolean isSkip = false;

    private SourceAdapter adapter;

    public SourceDialog(final Activity act, final Book book) {
        this.mBook = book;
        initData(act.getApplicationContext());
        mDialog = new MyDialog(act, "选择来源");
        adapter = new SourceAdapter(act.getApplication(), data);
        mDialog.setItems(adapter, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SourceUtils.saveSource(act.getApplicationContext(), data.get(position).value);
                showSeleted(position);
                if (isSkip && book != null) {
                    SkipUtils.skipToReader(act, book);
                    act.finish();
                }

            }
        });
    }


    private void initData(Context context) {
        String[] titles = context.getResources().getStringArray(R.array.source_items);
        int[] values = new int[]{SourceUtils.AUTO, SourceUtils.TIEBA, SourceUtils.BAIDU, SourceUtils.EASOU};
        Source source;
        int selectedSource = SourceUtils.getSource(context);
        for (int i = 0; i < titles.length; i++) {
            source = new Source();
            source.title = titles[i];
            source.value = values[i];
            if (source.value == selectedSource) {
                source.isSelected = true;
            }
            data.add(source);
        }
        removeAutoReading();
    }

    private void removeAutoReading() {
        if (!Constants.openAutoReading) {
            data.remove(0);
        }
    }

    private void showSeleted(int position) {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).isSelected = false;
        }
        data.get(position).isSelected = true;
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
