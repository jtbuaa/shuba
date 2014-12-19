package com.qiwenge.android.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dev1024.utils.StringUtils;
import com.qiwenge.android.act.BookDetailActivity;
import com.qiwenge.android.act.BrowserActivity;
import com.qiwenge.android.act.ChapterActivity;
import com.qiwenge.android.act.ReadActivity;
import com.qiwenge.android.entity.Book;

public class SkipUtils {

    //贴吧
    private static final String FORMAT_TIEBA = "http://tieba.baidu.com/f?kw=%s";

    //百度搜索
    private static final String FORMAT_BAIDU = "http://www.baidu.com/s?cl=3&wd=%s";

    //宜搜
    private static final String FORMAT_EASOU = "http://book.easou.com/ta/search.m?q=%s";

    public static void skipToBookDetail(Context context, Book book) {
        Bundle extra = new Bundle();
        extra.putParcelable(BookDetailActivity.EXTRA_BOOK, book);
        Intent intent = new Intent(context.getApplicationContext(), BookDetailActivity.class);
        intent.putExtras(extra);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳到阅读页面。
     *
     * @param context
     */
    public static void skipToReader(Context context, Book book) {
        String lastReadId =
                BookShelfUtils.getRecordChapterId(context, book.getId());
        if (StringUtils.isEmptyOrNull(lastReadId) && SourceUtils.getSource(context) == SourceUtils.AUTO) {
            skipToChapter(context, book);
        } else {
            skipToReader(context, book, lastReadId);
        }
    }

    public static void skipToReader(Context context, Book book, String chapterId) {
        int source = SourceUtils.getSource(context);
        switch (source) {
            case SourceUtils.AUTO://智能搜索
                showReader(context, book, chapterId);
                break;
            case SourceUtils.TIEBA://贴吧
            case SourceUtils.BAIDU://百度搜索
            case SourceUtils.EASOU://宜搜
                showReaderBrowser(context, book);
                break;
        }
    }

    private static void showReaderBrowser(Context context, Book book) {
        int source = SourceUtils.getSource(context);
        switch (source) {
            case SourceUtils.TIEBA://贴吧
                skipToBrowser(context, "百度贴吧", String.format(FORMAT_TIEBA, book.title), book);
                break;
            case SourceUtils.BAIDU://百度搜索
                skipToBrowser(context, "百度搜索", String.format(FORMAT_BAIDU, book.title), book);
                break;
            case SourceUtils.EASOU://宜搜
                skipToBrowser(context, "宜搜", String.format(FORMAT_EASOU, book.title), book);
                break;
        }
    }

    private static void skipToChapter(Context context, Book book) {
        Bundle extra = new Bundle();
        extra.putParcelable(ChapterActivity.EXTRA_BOOK, book);
        Intent intent = new Intent(context, ChapterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(extra);
        context.startActivity(intent);
    }

    private static void showReader(Context context, Book book,
                                   String chapterId) {
        Bundle extra = new Bundle();
        extra.putParcelable(ReadActivity.Extra_Book, book);
        extra.putString(ReadActivity.Extra_BookId, book.getId());
        extra.putString(ReadActivity.Extra_ChapterId, chapterId);
        extra.putString(ReadActivity.Extra_BookTitle, book.title);
        Intent intent = new Intent(context, ReadActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(extra);
        context.startActivity(intent);
    }

    private static void skipToBrowser(Context context, String title, String url, Book book) {
        Bundle extra = new Bundle();
        extra.putString(BrowserActivity.EXTRA_TITLE, title);
        extra.putString(BrowserActivity.EXTRA_URL, url);
        extra.putParcelable(BrowserActivity.EXTRA_BOOK, book);
        Intent intent = new Intent(context.getApplicationContext(), BrowserActivity.class);
        intent.putExtras(extra);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
