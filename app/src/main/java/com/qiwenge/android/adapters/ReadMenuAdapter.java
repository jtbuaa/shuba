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
import com.qiwenge.android.entity.ReadMenu;

/**
 * ReadMenuAdapter
 * 
 * Created by John on 2014-6-14
 */
public class ReadMenuAdapter extends MyBaseAdapter<ReadMenu> {

    private ViewHolder viewHolder;

    public ReadMenuAdapter(Context context, List<ReadMenu> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_read_menu, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.item_tv_title);
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.item_iv_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ReadMenu model = data.get(position);
        if (model != null) {
            viewHolder.tvTitle.setText(model.title);
            viewHolder.ivIcon.setBackgroundResource(model.icon);
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView tvTitle;
        public ImageView ivIcon;
    }

}
