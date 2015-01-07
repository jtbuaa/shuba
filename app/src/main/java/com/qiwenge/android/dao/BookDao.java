package com.qiwenge.android.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.qiwenge.android.entity.Book;

import java.sql.SQLException;


/**
 * Created by Eric on 14/12/21.
 */
public class BookDao extends AbstractDao<Book> {

    public BookDao(Context context) {
        super(context);
    }

    @Override
    public Dao<Book, Integer> getDao() throws SQLException {
        return getHelper().getBookDao();
    }

}
