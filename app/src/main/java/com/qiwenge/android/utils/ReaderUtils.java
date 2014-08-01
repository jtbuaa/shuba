package com.qiwenge.android.utils;

import android.content.Context;

import com.dev1024.utils.PreferencesUtils;
import com.qiwenge.android.constant.Constants;

/**
 * ReaderUtils
 * <p/>
 * Created by John on 2014-4-24
 */
public class ReaderUtils {

    private static final String TEXT_SIZE_KEY = "TEXT_SIZE_KEY";

    /**
     * 保存字体大小
     *
     * @param context
     * @param textSize
     */
    public static void saveTextSize(Context context, int textSize) {
        PreferencesUtils.putInt(context, Constants.PRE_SAVE_NAME, TEXT_SIZE_KEY, textSize);
    }

    /**
     * 获取字体大小
     *
     * @param context
     */
    public static int getTextSize(Context context) {
        return PreferencesUtils.getInt(context, Constants.PRE_SAVE_NAME, TEXT_SIZE_KEY, Constants.DEFAULT_TEXT_SIZE);
    }

    /**
     * Return a String that only has two spaces.
     *
     * @return
     */
    public static String getTwoSpaces() {
        return "\u3000\u3000";
    }

    /**
     * 格式化小说内容。
     * <p/>
     * <li>小说的开头，缩进2格。在开始位置，加入2格空格。
     * <li>所有的段落，缩进2格。所有的\n,替换为2格空格。
     *
     * @param str
     * @return
     */
    public static String formatContent(String str) {
        str = str.trim();
        str = getTwoSpaces() + str;
        str = str.replace("\n", "\n" + getTwoSpaces());
        return str;
    }

}