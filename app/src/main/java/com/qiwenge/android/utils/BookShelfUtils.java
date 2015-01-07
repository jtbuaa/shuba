package com.qiwenge.android.utils;

import android.content.Context;

import com.qiwenge.android.dao.DaoFactory;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.entity.Chapter;

/**
 * 书架工具类
 * <p/>
 * Created by Eric on 2014－6－2
 */
public class BookShelfUtils {


    /**
     * 获取阅读的章节Number
     *
     * @param context
     * @param bookId
     * @return
     */
    public static int getReadNumber(Context context, String bookId) {

        Book newBook = DaoFactory.createBookDao(context).queryById(bookId);
        if (newBook != null) {
            return newBook.chapter_number;
        }

        return 0;
    }

    public static void updateReadRecord(Context context, Book book, Chapter chapter, int characterNumber) {
        Book newBook = DaoFactory.createBookDao(context).queryById(book.getId());
        if (newBook != null) {
            newBook.chapter_id = chapter.getId();
            newBook.chapter_number = chapter.number;
            newBook.character_number = characterNumber;
            DaoFactory.createBookDao(context).update(newBook);
        }
    }

}
