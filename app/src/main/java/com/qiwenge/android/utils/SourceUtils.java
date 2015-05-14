package com.qiwenge.android.utils;

import android.content.Context;

import com.liuguangqiang.framework.utils.PreferencesUtils;
import com.qiwenge.android.constant.Constants;

/**
 * Created by Eric on 14-10-13.
 */
public class SourceUtils {

    /**
     * 自动的，智能的
     */
    public static final int AUTO = 0;

    /**
     * 手动的
     */
    public static final int MANUAL = 1;

    /**
     * 百度贴吧
     */
    public static final int TIEBA = 2;

    /**
     * 百度
     */
    public static final int BAIDU = 3;

    /**
     * 宜搜
     */
    public static final int EASOU = 4;

    private static final String SOURCE_KEY = "SHUBA_SOURCE_KEY";

    public static void saveSource(Context context, int source) {
        PreferencesUtils.putInt(context, Constants.PRE_SAVE_NAME, SOURCE_KEY, source);
    }

    /**
     * 默认用贴吧
     *
     * @param context
     * @return
     */
    public static int getSource(Context context) {
        int source = PreferencesUtils.getInt(context, Constants.PRE_SAVE_NAME, SOURCE_KEY, AUTO);

//        if (source == AUTO && !Constants.openAutoReading) {
//            source = TIEBA;
//        }

        return source;
    }

}
