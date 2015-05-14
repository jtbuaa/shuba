package com.qiwenge.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiwenge.android.R;
import com.qiwenge.android.adapters.base.MyBaseAdapter;
import com.qiwenge.android.entity.Offline;

import java.util.List;

/**
 * OfflineAdapter
 */
public class OfflineAdapter extends MyBaseAdapter<Offline> {

    private ViewHolder viewHolder;

    public OfflineAdapter(Context context, List<Offline> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_offline, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.item_tv_title);
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.item_iv_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Offline model = data.get(position);
        if (model != null) {
            viewHolder.tvTitle.setText(model.title);

            viewHolder.ivIcon.setBackgroundResource(
                    model.selected ? R.drawable.icon_check_selected : R.drawable.icon_check_normal);
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView tvTitle;
        public ImageView ivIcon;
    }

}
