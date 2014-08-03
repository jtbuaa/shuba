package com.qiwenge.android.models;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.qiwenge.android.models.base.BaseModel;

/**
 * Book
 * 
 * Created by John on 2014-5-6
 */
public class Book extends BaseModel implements Parcelable {

    public String title;

    public String description;

    public String author;

    public String cover;

    public int status;

    /**
     * 1：完本；0:连载
     */
    public int finish;

    public ArrayList<String> categories;

    /**
     * 上传阅读的章节id.
     */
    public String lastReadId = "";

    /**
     * 是否被选中
     */
    public boolean selected=false;

    public Book() {}

    public Book(Parcel source) {
        Bundle bundle = source.readBundle();
        title = bundle.getString("title");
        description = bundle.getString("description");
        author = bundle.getString("author");
        cover = bundle.getString("cover");
        status = bundle.getInt("status");
        finish = bundle.getInt("finish");
        categories = bundle.getStringArrayList("categories");
        lastReadId = bundle.getString("lastReadId");
        setId(bundle.getString("id"));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle data = new Bundle();
        data.putString("title", title);
        data.putString("description", description);
        data.putString("author", author);
        data.putString("cover", cover);
        data.putInt("status", status);
        data.putInt("finish", finish);
        data.putString("lastReadId", lastReadId);
        data.putString("id", getId());
        data.putStringArrayList("categories", categories);
        dest.writeBundle(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        public Book createFromParcel(Parcel data) {
            return new Book(data);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

}
