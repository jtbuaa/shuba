package com.qiwenge.android.dao;

import android.content.Context;

/**
 * Created by Eric on 14/12/22.
 */
public class DaoFactory {

    private static BookDao bookDao;

    private DaoFactory() {
    }

    public static BookDao createBookDao(Context context) {
        if (bookDao == null) {
            bookDao = new BookDao(context);
        }
        return bookDao;
    }

}
