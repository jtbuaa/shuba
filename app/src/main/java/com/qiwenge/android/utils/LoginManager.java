package com.qiwenge.android.utils;

import com.qiwenge.android.models.User;

/**
 * Created by Eric on 14/11/22.
 */
public class LoginManager {

    private static User mUser;

    public static boolean isLogin() {
        return mUser != null;
    }

    public static User getUser() {
        return mUser;
    }

    public static void logout() {
        mUser = null;
    }

}
