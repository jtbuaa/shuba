package com.qiwenge.android.utils;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.entity.Chapter;
import com.qiwenge.android.entity.Mirror;
import com.qiwenge.android.utils.book.BookManager;
import com.qiwenge.android.utils.http.BaseResponseHandler;
import com.qiwenge.android.utils.http.JHttpClient;

public class BookShelfUtils {

    private static final String TAG = "BookShelf";

    /**
     * 获取阅读的章节Number
     *
     * @param bookId
     * @return
     */
    public static int getReadNumber(String bookId) {
        Book book = BookManager.getInstance().getById(bookId);
        if (book != null) {
            Mirror mirror = book.currentMirror();
            if (mirror != null) return mirror.progress.chapters;
        }
        return 0;
    }

    /**
     * 更新小说的进度。
     *
     * @param context
     * @param book
     * @param chapter
     * @param chars
     */
    public static void updateReadRecord(Context context, Book book, Chapter chapter, int chars) {
        Log.i(TAG, "mirrorList-size:" + book.mirrorList.size());
        Mirror mirror = book.currentMirror();
        if (mirror != null) {
            mirror.progress.chapter_id = chapter.getId();
            mirror.progress.chapters = chapter.number;
            mirror.progress.chars = chars;
            Log.i("chapter_id", chapter.getId());

            String url = ApiUtils.putProgresses();
            RequestParams requestParams = new RequestParams();
            requestParams.put("book_id", book.getId());
            requestParams.put("mirror_id", mirror.getId());
            requestParams.put("chapter_id", chapter.getId());
            requestParams.put("chapters", chapter.number);
            requestParams.put("chars", chars);
            Log.i("putProgress", url + "?" + requestParams.toString());
            JHttpClient.put(url, requestParams, new BaseResponseHandler() {
                @Override
                public void onSuccess(String result) {
                    Log.i("putProgress", "success");
                }
            });
        }

        BookManager.getInstance().update(context, book);

//        new AsyncUpdateBook(context).execute(book);
    }

    /**
     * 更新本地小说的ChapterTotal
     *
     * @param context
     * @param bookId
     * @param chapterTotal
     */
    public static void updateChapterTotal(Context context, String bookId, int chapterTotal) {
        Book book = BookManager.getInstance().getById(bookId);
        if (book != null) {
            book.chapter_total = chapterTotal;
            BookManager.getInstance().update(context, book);
        }
    }
}
