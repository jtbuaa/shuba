package com.qiwenge.android.utils;

import android.content.Context;
import android.util.Log;

import com.liuguangqiang.common.utils.PreferencesUtils;
import com.qiwenge.android.entity.Book;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Eric on 14/12/3.
 */
public class PushUtils {

    private final static String TAG = "PushUtils";

    private final static int STATUS_SUCCESS = 0;

    private final static int STATUS_TIMEOUT = 6002;

    private final static int RETRY_COUNT = 5;

    private final static String PUSH_MANAGER = "PUSH_MANAGER";

    private final static String IS_OPEN = "IS_OPEN";

    private Context mContext;

    public PushUtils(Context context) {
        mContext = context;
    }

    public void init() {
        if (isOpenPush()) {
            stopPush();
        } else {
            stopPush();
        }
    }

    public boolean isOpenPush() {
        return PreferencesUtils.getBoolean(mContext, PUSH_MANAGER, IS_OPEN);
    }

    public void stopPush() {
        JPushInterface.stopPush(mContext);
        PreferencesUtils.putBoolean(mContext, PUSH_MANAGER, IS_OPEN, false);
    }

    public void openPush() {
        JPushInterface.resumePush(mContext);
        PreferencesUtils.putBoolean(mContext, PUSH_MANAGER, IS_OPEN, true);
    }

    public void setAlias(Context context) {
        if (LoginManager.isLogin()) {
            setAlias(context, LoginManager.getUser().getId(), RETRY_COUNT);
        } else {
            clearAlias(context);
        }
    }

    private void setAlias(final Context context, final String alias, final int retryCount) {
        JPushInterface.setAlias(context, alias, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> strings) {
                if (i == STATUS_TIMEOUT && retryCount > 0) {
                    setAlias(context, alias, retryCount - 1);
                } else if (i == STATUS_SUCCESS) {
                    Log.i(TAG, "setAlias success:" + alias);
                }
            }
        });
    }

    public void setTags(Context context, List<Book> books) {
        Set<String> tags = new HashSet<>();
        for (Book book : books) {
            tags.add(book.getId());
        }
        setTags(context, tags, RETRY_COUNT);
    }

    private void setTags(final Context context, final Set<String> tags, final int retryCount) {
        JPushInterface.setTags(context, tags, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> strings) {
                if (i == STATUS_TIMEOUT && retryCount > 0) {
                    setTags(context, tags, retryCount - 1);
                } else if (i == STATUS_SUCCESS) {
                    Log.i(TAG, "setTags success:" + tags.toString());
                }
            }
        });
    }

    public void clearAlias(Context context) {
        setAlias(context, "", RETRY_COUNT);
    }

}
