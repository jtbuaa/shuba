package com.qiwenge.android.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.qiwenge.android.entity.base.BaseModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 14/12/22.
 */
public abstract class AbstractDao<T extends BaseModel> {

    private Context mContext;

    public AbstractDao(Context context) {
        this.mContext = context;
    }

    public DatabaseHelper getHelper() {
        return new DatabaseHelper(mContext);
    }

    public abstract Dao<T, Integer> getDao() throws SQLException;

    public boolean isExists(T t) {
        try {
            Dao<T, Integer> dao = getDao();
            return !dao.queryForEq(T.ID, t.getId()).isEmpty();
        } catch (SQLException ex) {
        }
        return false;
    }

    public List<T> queryAll() {
        List<T> list = new ArrayList<>();
        try {
            Dao<T, Integer> dao = getDao();
            QueryBuilder<T, Integer> builder = dao.queryBuilder();
            list.addAll(builder.query());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public T queryById(String id) {
        try {
            Dao<T, Integer> dao = getDao();
            List<T> list = dao.queryForEq(T.ID, id);
            if (!list.isEmpty()) return list.get(0);
            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int add(T t) {
        try {
            return getDao().create(t);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public void add(List<T> list) {
        for (T t : list) {
            add(t);
        }
    }

    public int remove(String columnName, Object value) {
        try {
            Dao<T, Integer> dao = getDao();
            DeleteBuilder<T, Integer> builder = dao.deleteBuilder();
            Where<T, Integer> wheres = builder.where();
            wheres.eq(columnName, value);
            builder.setWhere(wheres);
            return builder.delete();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public int remove(T t) {
        return remove(T.ID, t.getId());
    }

    public int update(T t) {
        return update(t, T.ID, t.getId());
    }

    public int update(T t, String columnName, Object value) {
        try {
            UpdateBuilder<T, Integer> builder = getDao().updateBuilder();
            Where<T, Integer> wheres = builder.where();
            wheres.eq(columnName, value);
            builder.setWhere(wheres);
            return getDao().update(t);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

}
