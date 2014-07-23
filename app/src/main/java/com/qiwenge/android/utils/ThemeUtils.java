package com.qiwenge.android.utils;

import android.content.Context;

import com.dev1024.utils.PreferencesUtils;
import com.qiwenge.android.constant.Constants;

/**
 * 阅读主题工具类。
 * 
 * Created by John on 2014年7月2日
 */
public class ThemeUtils {

    /**
     * 夜间模式 value:{@value}
     */
    public static final int NIGHT = 4;

    /**
     * 普通 value:{@value}
     */
    public static final int NORMAL = 0;

    /**
     * 黄色 value:{@value}
     */
    public static final int YELLOW = 1;

    /**
     * 绿色 value:{@value}
     */
    public static final int GREEN = 2;

    /**
     * 皮革 value:{@value}
     */
    public static final int LEATHER = 3;

    private static final String SAVE_KEY = "reader_theme";

    /**
     * 当前主题，默认为：普通。
     */
    private static int currentTheme = NORMAL;

    /**
     * 初始化主题。
     * 
     * @param context
     */
    public static void initTheme(Context context) {
        int theme = getTheme(context);
        if (theme > -1) {
            currentTheme = theme;
        }
        System.out.println("theme:" + theme);
        System.out.println("currentTheme:" + currentTheme);
    }

    /**
     * 获取当前的主题。
     * 
     * @param context
     * @return
     */
    public static int getCurrentTheme() {
        return currentTheme;
    }

    /**
     * 设置阅读主题。
     * <p>
     * 
     * @param theme
     */
    public static void setTheme(Context context, int theme) {
        currentTheme = theme;
        System.out.println("new Theme:" + theme);
        PreferencesUtils.putInt(context, Constants.PRE_SAVE_NAME, SAVE_KEY, theme);
    }

    /**
     * 获取主题。
     * 
     * @param context
     */
    private static int getTheme(Context context) {
        return PreferencesUtils.getInt(context, Constants.PRE_SAVE_NAME, SAVE_KEY);
    }

}
