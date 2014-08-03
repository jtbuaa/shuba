package com.qiwenge.android.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.qiwenge.android.act.ReadActivity;

public class SkipUtils {

    /**
     * 跳到阅读页面。
     * 
     * @param context
     * @param bookId
     * @param bookTitle
     * @param chapterId
     */
    public static void skipToReader(Context context, String bookId, String bookTitle,
            String chapterId) {
        Bundle extra = new Bundle();
        extra.putString(ReadActivity.Extra_BookId, bookId);
        extra.putString(ReadActivity.Extra_ChapterId, chapterId);
        extra.putString(ReadActivity.Extra_BookTitle, bookTitle);
        Intent intent = new Intent(context, ReadActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(extra);
        context.startActivity(intent);
    }

}
