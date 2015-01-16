package com.qiwenge.android.login;


public interface AuthListener {

    public void onStart();

    public void onFailure();

    public void authSuccess(String uid, String username, String avatarUrl, LoginType loginType);

}
