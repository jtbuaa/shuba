package com.qiwenge.android.adapters;

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

import java.util.List;

public class BookShelfAdapter extends MyBaseAdapter<Book> {

    private ViewHolder viewHolder;

    private DisplayImageOptions mOptions;

    public BookShelfAdapter(Context context, List<Book> data) {
        this.data = data;
        this.context = context;
        mOptions = ImageLoaderUtils.createOptions(R.drawable.icon_place_holder);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_book_shelf, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.item_tv_title);
            viewHolder.tvUpdate = (TextView) convertView.findViewById(R.id.item_tv_update);
            viewHolder.tvDesc = (TextView) convertView.findViewById(R.id.item_tv_desc);
            viewHolder.tvAuthor = (TextView) convertView.findViewById(R.id.item_tv_author);
            viewHolder.ivCover = (ImageView) convertView.findViewById(R.id.item_iv_cover);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Book book = data.get(position);
        if (book != null) {
            viewHolder.tvTitle.setText(book.title.trim());
            viewHolder.tvDesc.setText(ReaderUtils.formatItemDesc(book.description.trim()));
            viewHolder.tvAuthor.setText(book.author.trim());

            int updateVisiblity = book.hasUpdate ? View.VISIBLE : View.GONE;
            viewHolder.tvUpdate.setVisibility(updateVisiblity);
            viewHolder.tvUpdate.setText(String.format("已更新 %s 章", book.updateArrival));

            ImageLoaderUtils.display(book.cover, viewHolder.ivCover, mOptions);
        }
        return convertView;
    }

    private class ViewHolder {
        public TextView tvTitle;
        public TextView tvAuthor;
        public TextView tvDesc;
        public TextView tvUpdate;
        public ImageView ivCover;
    }

}
