package com.qiwenge.android.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.qiwenge.android.R;
import com.qiwenge.android.adapters.base.MyBaseAdapter;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.utils.ImageLoaderUtils;
import com.qiwenge.android.utils.ReaderUtils;

/**
 * 相关推荐适配器。 AboutRmdAdapter
 */
public class AboutRmdAdapter extends MyBaseAdapter<Book> {

    private ViewHolder viewHolder;

    private DisplayImageOptions mOptions;

    public AboutRmdAdapter(Context context, List<Book> data) {
        this.data = data;
        this.context = context;
        mOptions = ImageLoaderUtils.createOptions(R.drawable.icon_place_holder);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_about_recommend, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.item_tv_title);
            viewHolder.ivCover = (ImageView) convertView.findViewById(R.id.item_iv_cover);
            viewHolder.tvDesc = (TextView) convertView.findViewById(R.id.item_tv_desc);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Book model = data.get(position);
        if (model != null) {
            viewHolder.tvTitle.setText(model.title);
            viewHolder.tvDesc.setText(ReaderUtils.formatItemDesc(model.description.trim()));
            ImageLoaderUtils.display(model.cover, viewHolder.ivCover, mOptions);
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView tvTitle;
        public ImageView ivCover;
        public TextView tvDesc;
    }

}
