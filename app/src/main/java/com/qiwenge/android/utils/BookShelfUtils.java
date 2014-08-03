package com.qiwenge.android.utils;

import android.content.Context;

import com.dev1024.utils.GsonUtils;
import com.dev1024.utils.LogUtils;
import com.dev1024.utils.PreferencesUtils;
import com.dev1024.utils.StringUtils;
import com.google.gson.Gson;
import com.qiwenge.android.models.Book;
import com.qiwenge.android.models.BookList;

/**
 * 书架工具类
 * <p/>
 * Created by John on 2014－6－2
 */
public class BookShelfUtils {

    private static final String NAME = "QIWENGE";

    private static final String KEY = "BOOK_SHELF";

    /**
     * 阅读记录。
     */
    private static final String READ_RECORD = "READ_RECORD";

    /**
     * 获取书架中的所有书籍
     *
     * @return
     */
    public static BookList getBooks(Context context) {
        String json = PreferencesUtils.getString(context, NAME, KEY);
        if (StringUtils.isEmptyOrNull(json))
            return new BookList();
        else {
            BookList books = GsonUtils.getModel(json, BookList.class);
            return books;
        }
    }

    /**
     * 添加一本书到书架
     *
     * @param book
     */
    public static void addBook(Context context, Book book) {
        BookList books = getBooks(context);
        books.result.add(book);
        saveBooks(context, books);
    }

    /**
     * 从书架中移除一本书
     *
     * @param context
     * @param book
     */
    public static void removeBook(Context context, Book book) {
        BookList books = getBooks(context);
        for (int i = 0; i < books.result.size(); i++) {
            if (books.result.get(i).getId().equals(book.getId())) {
                removeBook(context, i);
                break;
            }
        }
    }

    /**
     * 从书架中移除一本书
     *
     * @param position
     */
    public static void removeBook(Context context, int position) {
        BookList books = getBooks(context);
        books.result.remove(position);
        saveBooks(context, books);
    }

    /**
     * 是否包含了某本书
     *
     * @param book
     */
    public static boolean contains(Context context, Book book) {
        BookList books = getBooks(context);
        for (int i = 0; i < books.result.size(); i++) {
            if (books.result.get(i).getId().equals(book.getId())) return true;
        }
        return false;
    }

    /**
     * 保存到SharedPreferences
     *
     * @param context
     * @param books
     */
    private static void saveBooks(Context context, BookList books) {
        String json = new Gson().toJson(books);
        if (json != null) PreferencesUtils.putString(context, NAME, KEY, json);
    }

    /**
     * 保存阅读记录。
     *
     * @param context
     * @param book
     * @param chapterid 阅读的章节id
     */
    public static void saveRecord(Context context, String bookId, String chapterid) {
        if (context == null || bookId == null || chapterid == null) return;
        PreferencesUtils.putString(context, READ_RECORD, bookId, chapterid);
    }


    /**
     * 获取阅读记录的章节Id
     *
     * @param context
     * @param bookId
     * @return
     */
    public static String getRecordChapterId(Context context, String bookId) {
        return PreferencesUtils.getString(context, READ_RECORD, bookId);
    }

    /**
     * 保存阅读字数。
     * @param context
     * @param chapterId 章节Id
     * @param length
     */
    public static void saveReadLength(Context context, String chapterId, int length) {
        LogUtils.i("saveReadLength:",""+length);
        PreferencesUtils.putInt(context, READ_RECORD, "length" + chapterId, length);
    }

    /**
     * 获取阅读字数。
     * @param context
     * @param chapterId
     * @return
     */
    public static int getReadLenght(Context context, String chapterId) {
        return PreferencesUtils.getInt(context, READ_RECORD, "length" + chapterId,0);
    }

}
