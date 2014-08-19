package com.qiwenge.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private final static int DURATION = 150;

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
            viewHolder.tvDesc = (TextView) convertView.findViewById(R.id.item_tv_desc);
            viewHolder.tvAuthor = (TextView) convertView.findViewById(R.id.item_tv_author);
            viewHolder.ivCover = (ImageView) convertView.findViewById(R.id.item_iv_cover);
            viewHolder.ivSelected = (ImageView) convertView.findViewById(R.id.item_iv_select);
            viewHolder.layoutBookShelf = (LinearLayout) convertView.findViewById(R.id.item_layout_bookshelf);
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
                viewHolder.layoutBookShelf
                        .setBackgroundColor(context.getResources().getColor(R.color.item_focus_bg_color));
                if (model.showAnim)
                    showFront(viewHolder.ivCover, viewHolder.ivSelected);
                else {
                    viewHolder.ivSelected.setVisibility(View.VISIBLE);
                    viewHolder.ivCover.setVisibility(View.GONE);
                }
            } else {
                viewHolder.layoutBookShelf
                        .setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                if (model.showAnim)
                    showFront(viewHolder.ivSelected, viewHolder.ivCover);
                else {
                    viewHolder.ivSelected.setVisibility(View.GONE);
                    viewHolder.ivCover.setVisibility(View.VISIBLE);
                }
            }
            data.get(position).showAnim = false;
        }
        return convertView;
    }

    private class ViewHolder {
        public TextView tvTitle;
        public TextView tvAuthor;
        public TextView tvDesc;
        public ImageView ivCover;
        public ImageView ivSelected;
        public LinearLayout layoutBookShelf;
    }

    private ScaleAnimation animaFront;
    private ScaleAnimation animaBack;

    public void showFront(final View view, final View back) {
        if (view.getVisibility() == View.GONE) return;

        animaFront = new ScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0);
        animaFront.setInterpolator(new DecelerateInterpolator());
        animaFront.setDuration(DURATION);
        animaFront.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                showBack(back);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(animaFront);
    }

    public void showBack(View back) {
        if (back.getVisibility() == View.VISIBLE) return;
        back.setVisibility(View.VISIBLE);
        if (animaBack == null) {
            animaBack = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0);
            animaBack.setInterpolator(new DecelerateInterpolator());
            animaBack.setDuration(DURATION);
        }
        back.startAnimation(animaBack);
    }

}
