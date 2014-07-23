package com.qiwenge.android.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qiwenge.android.R;
import com.qiwenge.android.adapters.base.MyBaseAdapter;
import com.qiwenge.android.models.Chapter;

/**
 * 目录.
 * 
 * Created by John on 2014-5-5
 */
public class ChapterAdapter extends MyBaseAdapter<Chapter> {

    private String SHOW_FORMAT;

    private ViewHolder viewHolder;

    public ChapterAdapter(Context context, List<Chapter> data) {
        this.data = data;
        this.context = context;
        SHOW_FORMAT = context.getString(R.string.str_chapter_title);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chapter, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.item_tv_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Chapter model = data.get(position);
        if (model != null) {
            viewHolder.tvTitle.setText(String.format(SHOW_FORMAT, model.number, model.title));
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView tvTitle;
    }

}
