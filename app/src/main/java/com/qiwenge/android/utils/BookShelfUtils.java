package com.qiwenge.android.utils;

import android.content.Context;

import com.qiwenge.android.entity.Book;
import com.qiwenge.android.entity.Chapter;
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
            return book.chapter_number;
        }
        return 0;
    }

    public static void updateReadRecord(Context context, Book book, Chapter chapter, int characterNumber) {
        book.chapter_id = chapter.getId();
        book.chapter_number = chapter.number;
        book.character_number = characterNumber;
        BookManager.getInstance().update(context, book);
    }

}
