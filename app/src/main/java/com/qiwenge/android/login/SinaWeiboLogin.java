package com.qiwenge.android.login;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.qiwenge.android.constant.Constant_Sina;
import com.qiwenge.android.models.SinaUser;
import com.qiwenge.android.utils.http.BaseResponseHandler;
import com.qiwenge.android.utils.http.JHttpClient;
import com.sina.weibo.sdk.WeiboSDK;
import com.sina.weibo.sdk.api.IWeiboAPI;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.sso.SsoHandler;
import com.weibo.sdk.android.util.AccessTokenKeeper;

public class SinaWeiboLogin {

    public static SsoHandler mSsoHandler;
    public static Oauth2AccessToken mAccessToken;

    private static IWeiboAPI mWeiboAPI = null;

    private static AuthListener authListener;

    /**
     * 新浪微博登陆。
     */
    public static void login(final Activity activity,
                             AuthListener listener) {
        System.out.println("login by weibo");
        authListener = listener;
        mWeiboAPI = WeiboSDK.createWeiboAPI(activity, Constant_Sina.APP_KEY);
        mWeiboAPI.registerApp();

        mSsoHandler = new SsoHandler(activity, getSinaWeibo());
        mSsoHandler.authorize(new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException e) {
                Toast.makeText(activity.getApplicationContext(),
                        "Auth error : " + e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onError(WeiboDialogError e) {
                Toast.makeText(activity.getApplicationContext(),
                        "Auth error : " + e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onComplete(Bundle values) {
                System.out.println("login sina onComplete");
                String token = values.getString("access_token");
                String expires_in = values.getString("expires_in");
                String username = "";
                String uid = "";
                mAccessToken = new Oauth2AccessToken(token, expires_in);
                if (mAccessToken.isSessionValid()) {
                    AccessTokenKeeper.keepAccessToken(
                            activity.getApplicationContext(), mAccessToken);
                    if (values.containsKey("userName"))
                        username = values.getString("userName");

                    if (values.containsKey("uid"))
                        uid = values.getString("uid");

                    getUserInfoOfSina(token, uid, username);
                }


            }

            @Override
            public void onCancel() {
                System.out.println("login sina onCancel");
            }
        });
    }

    public static void getUserInfoOfSina(String accessToken, final String uid, final String username) {
        String url = "https://api.weibo.com/2/users/show.json";
        System.out.println("getUserInfoOfSina");
        RequestParams params = new RequestParams();
        params.put("access_token", accessToken);
        params.put("uid", uid);
        JHttpClient.get(url, params, new BaseResponseHandler() {
            @Override
            public void onSuccess(String result) {
                SinaUser sinaUser = new Gson().fromJson(result, SinaUser.class);
                if (sinaUser != null) {
                    System.out.println("avatar-sina:" + sinaUser.avatar_large);
                    if (authListener != null) {
                        authListener.authSuccess(uid, username, sinaUser.avatar_large,
                                LoginType.sina_weibo);
                    }
                }
            }
        });
    }

    /**
     * 初始化新浪微博。
     */
    public static Weibo getSinaWeibo() {
        return Weibo.getInstance(Constant_Sina.APP_KEY,
                Constant_Sina.REDIRECT_URL, Constant_Sina.SCOPE);
    }
}
