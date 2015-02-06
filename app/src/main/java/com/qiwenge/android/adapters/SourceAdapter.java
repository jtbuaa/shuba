package com.qiwenge.android.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiwenge.android.R;
import com.qiwenge.android.adapters.base.MyBaseAdapter;
import com.qiwenge.android.entity.Mirror;

/**
 * SourceAdapter
 */
public class SourceAdapter extends MyBaseAdapter<Mirror> {

    private ViewHolder viewholder;

    public SourceAdapter(Context c, List<Mirror> list) {
        this.context = c;
        this.data = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewholder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_source, null);
            viewholder.tvTitle = (TextView) convertView.findViewById(R.id.item_tv_title);
            viewholder.ivSeleted = (ImageView) convertView.findViewById(R.id.item_iv_selected);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        viewholder.tvTitle.setText(data.get(position).template.title);

        if (data.get(position).current) {
            viewholder.ivSeleted.setBackgroundResource(R.drawable.icon_check_selected);
        } else {
            viewholder.ivSeleted.setBackgroundResource(R.drawable.icon_check_normal);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView tvTitle;
        ImageView ivSeleted;
    }
}
