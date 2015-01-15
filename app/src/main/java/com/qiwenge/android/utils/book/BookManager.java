package com.qiwenge.android.utils.book;

import android.content.Context;

import com.google.gson.Gson;
import com.liuguangqiang.common.utils.PreferencesUtils;
import com.liuguangqiang.common.utils.StringUtils;
import com.qiwenge.android.entity.Book;
import com.qiwenge.android.entity.BookList;
import com.qiwenge.android.entity.User;
import com.qiwenge.android.utils.LoginManager;

import java.util.ArrayList;
import java.util.List;

/**
 * BookManager.
 * <p>书籍管理,管理书架中的小说<p/>
 * Created by Eric on 15/1/15.
 */
public class BookManager {

    private static final String SHUBA_BOOK_SHELF = "SHUBA_BOOK_SHELF";

    private static ArrayList<Book> books = new ArrayList<>();

    private static BookManager ourInstance = new BookManager();

    public static BookManager getInstance() {
        return ourInstance;
    }

    private BookManager() {
    }

    public void init(Context context) {
        books.clear();
        if (LoginManager.isLogin()) {
            User user = LoginManager.getUser();
            String json = PreferencesUtils.getString(context, SHUBA_BOOK_SHELF, user.getId());
            if (!StringUtils.isEmptyOrNull(json)) {
                BookList bookList = new Gson().fromJson(json, BookList.class);
                if (bookList != null && bookList.result != null) {
                    books.addAll(bookList.result);
                }
            }
        }
    }

    private void save(Context context) {
        BookList bookList = new BookList();
        bookList.result = books;
        String json = new Gson().toJson(bookList);
        PreferencesUtils.putString(context, SHUBA_BOOK_SHELF, LoginManager.getUser().getId(), json);
    }


    public void clear() {
        books.clear();
    }

    public List<Book> getAll() {
        return books;
    }

    public Book getById(String id) {
        if (books.isEmpty()) return null;

        List<Book> list = books;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (list.get(i).getId().equals(id)) {
                return list.get(i);
            }
        }
        return null;
    }

    public void add(Context context, Book book) {
        if (LoginManager.isLogin()) {
            books.add(book);
            save(context);
        }
    }

    public void delete(Context context, Book book) {
        if (LoginManager.isLogin()) {
            books.remove(book);
            save(context);
        }
    }

    public void update(Context context, Book book) {
        int position = indexOf(book);
        if (position >= 0) {
            books.set(position, book);
            save(context);
        }
    }

    private int indexOf(Book book) {
        if (books.isEmpty()) return -1;

        List<Book> list = books;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (list.get(i).getId().equals(book.getId())) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains(Book book) {
        if (books.isEmpty()) return false;

        return indexOf(book) >= 0;
    }

}


