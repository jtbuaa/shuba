package com.qiwenge.android.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 阅读进度。
 * Created by Eric on 15/2/6.
 */
public class Progress implements Parcelable {

    /**
     * 阅读进度:章节Id
     */
    public String chapter_id = "";

    /**
     * 阅读进度:章节number
     */
    public int chapters = 0;

    /**
     * 阅读进度:阅读字数
     */
    public int chars = 0;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.chapter_id);
        dest.writeInt(this.chapters);
        dest.writeInt(this.chars);
    }

    public Progress() {
    }

    private Progress(Parcel in) {
        this.chapter_id = in.readString();
        this.chapters = in.readInt();
        this.chars = in.readInt();
    }

    public static final Parcelable.Creator<Progress> CREATOR = new Parcelable.Creator<Progress>() {
        public Progress createFromParcel(Parcel source) {
            return new Progress(source);
        }

        public Progress[] newArray(int size) {
            return new Progress[size];
        }
    };
}
