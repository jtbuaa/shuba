package com.qiwenge.android.entity;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.qiwenge.android.entity.base.BaseModel;

/**
 * Book
 * <p/>
 * Created by John on 2014-5-6
 */
public class Book extends BaseModel implements Parcelable {

    @DatabaseField(generatedId = true, canBeNull = false)
    public int generatedId;

    @DatabaseField
    public String title;

    @DatabaseField
    public String description;

    @DatabaseField
    public String author;

    @DatabaseField
    public String cover;

    @DatabaseField
    public int status;

    /**
     * 1：完本；0:连载
     */
    @DatabaseField
    public int finish;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    public ArrayList<String> categories;

    /**
     * 阅读进度:章节Id
     */
    @DatabaseField
    public String chapter_id;

    /**
     * 阅读进度:章节number
     */
    @DatabaseField
    public int chapter_number;

    /**
     * 阅读进度:阅读字数
     */
    @DatabaseField
    public int character_number = 0;

    /**
     * 是否被选中
     */
    public boolean selected = false;

    public boolean showAnim = false;

    public int chapter_total;

    public Book() {
    }

    public Book(Parcel source) {
        Bundle bundle = source.readBundle();
        title = bundle.getString("title");
        description = bundle.getString("description");
        author = bundle.getString("author");
        cover = bundle.getString("cover");
        status = bundle.getInt("status");
        finish = bundle.getInt("finish");
        categories = bundle.getStringArrayList("categories");
        chapter_id = bundle.getString("chapter_id");
        chapter_total = bundle.getInt("chapter_total");
        character_number = bundle.getInt("character_number");
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
        data.putString("chapter_id", chapter_id);
        data.putString("id", getId());
        data.putStringArrayList("categories", categories);
        data.putInt("chapter_total", chapter_total);
        data.putInt("character_number", character_number);
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
