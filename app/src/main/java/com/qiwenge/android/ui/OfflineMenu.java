package com.qiwenge.android.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.liuguangqiang.framework.utils.DisplayUtils;
import com.qiwenge.android.R;
import com.qiwenge.android.adapters.OfflineAdapter;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.entity.Offline;
import com.qiwenge.android.utils.OfflineUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 15/5/2.
 */
public class OfflineMenu extends LinearLayout {

    private Book mBook;

    public void setBook(Book book) {
        mBook = book;
    }

    public OfflineMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

        addView(createGridView(context));
        addView(createStartBtn(context));
    }

    private int currentPosition = 0;

    private GridView createGridView(Context context) {
        String[] titles = context.getResources().getStringArray(R.array.offline_items);
        final List<Offline> offlineList = new ArrayList<>();
        Offline offline;
        for (String str : titles) {
            offline = new Offline();
            offline.title = str;
            offlineList.add(offline);
        }
        offlineList.get(0).selected = true;

        GridView gv = new GridView(context);
        final OfflineAdapter adapter = new OfflineAdapter(context, offlineList);
        gv.setAdapter(adapter);
        gv.setNumColumns(4);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                offlineList.get(currentPosition).selected = false;
                offlineList.get(position).selected = true;
                currentPosition = position;
                adapter.notifyDataSetChanged();
            }
        });

        return gv;
    }

    private Button createStartBtn(Context context) {
        Button btn = new Button(context);
        btn.setText("开始离线");
        btn.setBackgroundResource(R.drawable.btn_hollow_gray);
        btn.setTextColor(context.getResources().getColor(R.color.white_p50));
        btn.setTextSize(12);

        int height = DisplayUtils.dip2px(context, 36);
        int width = DisplayUtils.dip2px(context, 160);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        btn.setLayoutParams(params);

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBook != null) {
                    OfflineUtils.createChapterFolder(mBook.id);
                    OfflineUtils.saveChapter(mBook.id, "testChapterId", mBook.description);
                }
            }
        });

        return btn;
    }

}
