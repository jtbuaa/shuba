package com.dev1024.utils;

public class StringUtils {

    /**
     * Return whether a String is Empty or null.
     * 
     * @param str
     * @return
     */
    public static boolean isEmptyOrNull(String str) {
        if (str == null) return true;

        if (str.trim().equals("")) return true;

        return false;
    }

}
