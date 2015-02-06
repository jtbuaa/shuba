package com.qiwenge.android.utils;

import android.content.Context;

import com.qiwenge.android.entity.Book;
import com.qiwenge.android.entity.Chapter;
import com.qiwenge.android.entity.Mirror;
import com.qiwenge.android.utils.book.BookManager;

public class BookShelfUtils {

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

    public static void updateReadRecord(Context context, Book book, Chapter chapter, int chars) {
        Mirror mirror = book.currentMirror();
        if (mirror != null) {
            mirror.progress.chapter_id = chapter.getId();
            mirror.progress.chapters = chapter.number;
            mirror.progress.chars = chars;
        }

        BookManager.getInstance().update(context, book);
    }
}
