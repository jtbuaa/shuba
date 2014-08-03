package com.qiwenge.android.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qiwenge.android.R;
import com.qiwenge.android.adapters.base.MyBaseAdapter;
import com.qiwenge.android.models.ReadTheme;
import com.qiwenge.android.utils.ThemeUtils;

/**
 * 阅读主题适配器
 * 
 * Created by John on 2014-6-22
 */
public class ReadThemeAdapter extends MyBaseAdapter<ReadTheme> {

    private ViewHolder viewHolder;

    public ReadThemeAdapter(Context context, List<ReadTheme> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_read_theme, null);
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.item_iv_icon);
            viewHolder.ivSelected = (ImageView) convertView.findViewById(R.id.item_iv_selected);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ReadTheme model = data.get(position);
        if (model != null) {
            ThemeUtils.setReaderTheme(model.theme,viewHolder.ivIcon);
            if (model.selected)
                viewHolder.ivSelected.setVisibility(View.VISIBLE);
            else
                viewHolder.ivSelected.setVisibility(View.GONE);
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView ivIcon;
        public ImageView ivSelected;
    }

}
