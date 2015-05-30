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

public class BooksAdapter extends MyBaseAdapter<Book> {

    private DisplayImageOptions mOptions;

    public BooksAdapter(Context context, List<Book> data) {
        this.data = data;
        this.context = context;
        mOptions = ImageLoaderUtils.createOptions(R.drawable.icon_place_holder);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_book, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.item_tv_title);
            viewHolder.tvDesc = (TextView) convertView.findViewById(R.id.item_tv_desc);
            viewHolder.tvAuthor = (TextView) convertView.findViewById(R.id.item_tv_author);
            viewHolder.ivCover = (ImageView) convertView.findViewById(R.id.item_iv_cover);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Book model = data.get(position);
        if (model != null) {
            viewHolder.tvTitle.setText(model.title.trim());
            viewHolder.tvDesc.setText(ReaderUtils.formatItemDesc(model.description.trim()));
            viewHolder.tvAuthor.setText(model.author.trim());
            ImageLoaderUtils.display(model.cover, viewHolder.ivCover, mOptions);
        }
        return convertView;
    }

    private class ViewHolder {
        public TextView tvTitle;
        public TextView tvAuthor;
        public TextView tvDesc;
        public ImageView ivCover;
    }

}
