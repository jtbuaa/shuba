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
import com.qiwenge.android.models.Book;
import com.qiwenge.android.utils.ImageLoaderUtils;

import java.util.List;

/**
 * 书架
 * <p/>
 * Created by John on 2014-7-26
 */
public class BookShelfAdapter extends MyBaseAdapter<Book> {

    private ViewHolder viewHolder;

    DisplayImageOptions mOptions;

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
            viewHolder.tvDesc = (TextView) convertView.findViewById(R.id.item_tv_desc);
            viewHolder.tvAuthor = (TextView) convertView.findViewById(R.id.item_tv_author);
            viewHolder.ivCover = (ImageView) convertView.findViewById(R.id.item_iv_cover);
            viewHolder.ivSelected = (ImageView) convertView.findViewById(R.id.item_iv_select);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Book model = data.get(position);
        if (model != null) {
            viewHolder.tvTitle.setText(model.title);
            viewHolder.tvDesc.setText(model.description);
            viewHolder.tvAuthor.setText(model.author);
            ImageLoaderUtils.display(model.cover, viewHolder.ivCover, mOptions);

            if (model.selected) {
                viewHolder.ivSelected.setVisibility(View.VISIBLE);
            } else viewHolder.ivSelected.setVisibility(View.GONE);
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView tvTitle;
        public TextView tvAuthor;
        public TextView tvDesc;
        public ImageView ivCover;
        public ImageView ivSelected;
    }

}
