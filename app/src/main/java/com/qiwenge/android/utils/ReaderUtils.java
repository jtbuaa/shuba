package com.qiwenge.android.utils;

/**
 * ReaderUtils
 * 
 * Created by John on 2014-4-24
 */
public class ReaderUtils {

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
     * <p>
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
