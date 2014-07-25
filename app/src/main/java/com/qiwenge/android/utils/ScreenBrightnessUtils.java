package com.qiwenge.android.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.WindowManager;

import com.dev1024.utils.PreferencesUtils;

/**
 * 屏幕亮度工具类.
 *
 * Created by John on 2014-5-31
 */
public class ScreenBrightnessUtils {

    private static final String BRIGHT_NAME = "SHUBA_BRIGHT_NAME";

    private static final String BRIGHT_KEY = "SHUBA_BRIGHT_KEY";

    /**
     * 最小屏幕亮度。如果为0，会自动锁屏。
     */
    private static final int MIN_BRIGHT = 1;

    /**
     * 保存屏幕亮度。
     */
    public static void saveScreenBright(Context context, int p) {
        if (p == 0) p = MIN_BRIGHT;
        PreferencesUtils.putString(context, BRIGHT_NAME, BRIGHT_KEY, "" + p);
    }

    /**
     * 从共享引用中获取屏幕亮度设置。
     * 
     * @param context
     * @return
     */
    private static int getScreenBright(Context context) {
        String s = PreferencesUtils.getString(context, BRIGHT_NAME, BRIGHT_KEY);
        if (s != null && !s.equals("")) return Integer.parseInt(s);
        return 0;
    }

    /**
     * 获取屏幕亮度。
     * 
     * @param context
     * @return
     */
    public static int getBrightness(Context context) {
        int brightness = 0;

        brightness = getScreenBright(context);
        if (brightness > 0) return brightness;

        ContentResolver res = context.getContentResolver();
        try {
            brightness =
                    Settings.System.getInt(res, Settings.System.SCREEN_BRIGHTNESS);
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }
        return brightness;
    }

    /**
     * 设置屏幕亮度。
     * 
     * @param context
     * @param p 0-255;
     */
    public static void setBrightness(Activity context, int p) {
        WindowManager.LayoutParams params = context.getWindow().getAttributes();
        params.screenBrightness = (p <= 0 ? MIN_BRIGHT : p) / 255f;
        context.getWindow().setAttributes(params);
    }
}
