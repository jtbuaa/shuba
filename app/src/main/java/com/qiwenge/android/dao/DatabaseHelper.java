package com.qiwenge.android.dao;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.qiwenge.android.entity.Book;

import java.sql.SQLException;

/**
 * Created by Eric on 14/12/21.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String FORMAT_ADD_COLUMN = "ALTER TABLE '%s' ADD COLUMN %s %s;";

    private static final String TAG = "DatabaseHelper";

    private static final String NAME = "shuba.db";

    private static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(TAG, "onCreate");
            TableUtils.createTable(connectionSource, Book.class);
        } catch (SQLException ex) {
            Log.e(TAG, "Unable to create database", ex);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(TAG, "onUpgrade");
            TableUtils.dropTable(connectionSource, Book.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "Unable to upgrade database from version " + oldVersion
                    + " to new " + newVersion, e);
        }
    }

    private Dao<Book, Integer> bookDao;

    public Dao<Book, Integer> getBookDao() throws SQLException {
        if (bookDao == null) {
            bookDao = getDao(Book.class);
        }
        return bookDao;
    }


    /**
     * 为表添加字段。在onUpgrade的时候使用。
     * <p>getBookDao().executeRaw("ALTER TABLE 'book' ADD COLUMN character_number INTEGER;");</p>
     *
     * @param dao
     * @param tableName
     * @param columnType
     * @param type
     */
    public void addColumn(Dao dao, String tableName, String columnType, String type) {
        try {
            String sql = String.format(FORMAT_ADD_COLUMN, tableName, columnType, type);
            dao.executeRaw(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
