package com.qiwenge.android.openudid;

import android.content.Context;

import com.liuguangqiang.framework.utils.AppUtils;
import com.liuguangqiang.framework.utils.StringUtils;

/**
 * Created by Eric on 14/11/30.
 */
public class OpenUDIDUtils {

    /**
     * 获取设备唯一标识
     *
     * @param context
     * @return 设备OpenUDID
     */
    public static String getOpenUDID(Context context) {
        String openUdId = "";
        OpenUDID_manager.sync(context);
        OpenUDID_manager.isInitialized();
        openUdId = OpenUDID_manager.getOpenUDID();
        if (StringUtils.isEmptyOrNull(openUdId)) {
            openUdId = AppUtils.getImeiCode(context);
        }
        return openUdId;
    }

}
