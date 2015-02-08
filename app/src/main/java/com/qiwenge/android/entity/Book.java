package com.qiwenge.android.entity;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.qiwenge.android.entity.base.BaseModel;
import com.qiwenge.android.entity.base.Id;

/**
 * Book
 * <p/>
 * Created by John on 2014-5-6
 */
public class Book extends BaseModel implements Parcelable {

    public boolean hasUpdate = false;

    public int updateArrival = 0;

    public List<Mirror> mirrorList = new ArrayList<Mirror>();

    public Mirror currentMirror() {
        if (mirrorList == null || mirrorList.isEmpty()) return null;

        for (Mirror mirror : mirrorList) {
            if (mirror.current) return mirror;
        }

        return mirrorList.get(0);
    }

    public Mirror getMirror(String mirrorId) {
        if (mirrorList == null || mirrorList.isEmpty()) return null;

        for (Mirror mirror : mirrorList) {
            if (mirror.getId().equals(mirrorId)) return mirror;
        }

        return mirrorList.get(0);
    }

    public String currentMirrorId() {
        Mirror current = currentMirror();
        return current == null ? "" : current.getId();
    }

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

    public int chapter_total;

    public Book() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mirrorList);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.author);
        dest.writeString(this.cover);
        dest.writeInt(this.status);
        dest.writeInt(this.finish);
        dest.writeSerializable(this.categories);
        dest.writeInt(this.chapter_total);
        dest.writeString(this.id);
        dest.writeParcelable(this._id, 0);
    }

    private Book(Parcel in) {
        in.readTypedList(mirrorList, Mirror.CREATOR);
        this.title = in.readString();
        this.description = in.readString();
        this.author = in.readString();
        this.cover = in.readString();
        this.status = in.readInt();
        this.finish = in.readInt();
        this.categories = (ArrayList<String>) in.readSerializable();
        this.chapter_total = in.readInt();
        this.id = in.readString();
        this._id = in.readParcelable(Id.class.getClassLoader());
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
