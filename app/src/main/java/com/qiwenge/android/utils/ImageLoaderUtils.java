package com.qiwenge.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.dev1024.utils.PreferencesUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * ImageLoaderUtils
 *
 * Created by John on 2014-7-7
 */
public class ImageLoaderUtils {

    /**
     * 加载器状态。1:开启；0:关闭
     */
    private static int LOADER_STATUS = 1;

    private static int STATUS_OPENED = 1;

    private static int STATUS_CLOSED = 0;

    private static final String SAVE_NAME = "LoaderUtils";

    private static final String SAVE_KEY = "LoaderStatus";

    private static boolean wifiEnable = true;

    /**
     * 初始化。
     * 
     * @param context
     */
    public static void init(Context context) {
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
        LOADER_STATUS = PreferencesUtils.getInt(context, SAVE_NAME, SAVE_KEY, STATUS_OPENED);
    }

    /**
     * 设置wifi是否可用。
     * 
     * @param b
     */
    public static void setWifiEnable(boolean b) {
        wifiEnable = b;
    }

    /**
     * 加载器是否打开。
     * 
     * @return
     */
    public static boolean isOpen() {
        return LOADER_STATUS == STATUS_OPENED;
    }

    /**
     * 显示图片。
     * 
     * @param url
     * @param iv
     * @param mOptions
     */
    public static void display(String url, ImageView iv, DisplayImageOptions mOptions) {
        if (wifiEnable) {
            ImageLoader.getInstance().displayImage(url, iv, mOptions);
        } else if (LOADER_STATUS == STATUS_OPENED) {
            ImageLoader.getInstance().displayImage(url, iv, mOptions);
        }
    }

    /**
     * 关闭图片加载
     */
    public static void closeLoader(Context context) {
        LOADER_STATUS = STATUS_CLOSED;
        PreferencesUtils.putInt(context, SAVE_NAME, SAVE_KEY, STATUS_CLOSED);
    }

    /**
     * 开启图片加载
     */
    public static void openLoader(Context context) {
        LOADER_STATUS = STATUS_OPENED;
        PreferencesUtils.putInt(context, SAVE_NAME, SAVE_KEY, STATUS_OPENED);
    }

    /**
     * 初始化ImageLoader
     * 
     * @param context
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(context)
                        .threadPriority(Thread.NORM_PRIORITY - 2)
                        // .memoryCache(new LruMemoryCache(1024 * 1024 * 10))
                        .memoryCache(new WeakMemoryCache()).denyCacheImageMultipleSizesInMemory()
                        .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    public static DisplayImageOptions createOptions(int imgLoading) {
        return new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(imgLoading).cacheInMemory(true).build();
    }

}
