package com.qiwenge.android.adapters;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.qiwenge.android.R;
import com.qiwenge.android.adapters.base.MyBaseAdapter;
import com.qiwenge.android.models.Book;
import com.qiwenge.android.utils.FadeInDisplayer;
import com.qiwenge.android.utils.ImageLoaderUtils;

/**
 * BooksAdapter
 * 
 * Created by John on 2014-7-6
 */
public class BooksAdapter extends MyBaseAdapter<Book> {

    private ViewHolder viewHolder;

    private DisplayImageOptions mOptions;

    private AnimateFirstDisplayListener animateFirstDisplayListener;

    public BooksAdapter(Context context, List<Book> data) {
        this.data = data;
        this.context = context;
        mOptions = ImageLoaderUtils.createOptions(R.drawable.icon_place_holder);
        animateFirstDisplayListener=new AnimateFirstDisplayListener();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
            viewHolder.tvTitle.setText(model.title);
            viewHolder.tvDesc.setText(model.description);
            viewHolder.tvAuthor.setText(model.author);
            ImageLoaderUtils.display(model.cover, viewHolder.ivCover, mOptions,animateFirstDisplayListener);
        }
        return convertView;
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> images= Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if(loadedImage!=null){
                ImageView imageView=(ImageView)view;
                if (!images.contains(imageUri)) {
                    FadeInDisplayer.animate(imageView, 200);
                    images.add(imageUri);
                }
            }
        }
    }

    public class ViewHolder {
        public TextView tvTitle;
        public TextView tvAuthor;
        public TextView tvDesc;
        public ImageView ivCover;
    }

}
