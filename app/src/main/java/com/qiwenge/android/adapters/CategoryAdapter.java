package com.qiwenge.android.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qiwenge.android.R;
import com.qiwenge.android.adapters.base.MyBaseAdapter;
import com.qiwenge.android.entity.Category;

/**
 * CategoryAdapter
 * 
 * Created by John on 2014-5-10
 */
public class CategoryAdapter extends MyBaseAdapter<Category> {

    private ViewHolder viewHolder;

    public CategoryAdapter(Context context, List<Category> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.item_tv_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Category model = data.get(position);
        if (model != null) {
            viewHolder.tvTitle.setText(model.name);
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView tvTitle;
    }

}
