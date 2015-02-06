package com.qiwenge.android.entity.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eric on 15/2/6.
 */
public class Template implements Parcelable {

    public String title = "";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
    }

    public Template() {
    }

    private Template(Parcel in) {
        this.title = in.readString();
    }

    public static final Parcelable.Creator<Template> CREATOR = new Parcelable.Creator<Template>() {
        public Template createFromParcel(Parcel source) {
            return new Template(source);
        }

        public Template[] newArray(int size) {
            return new Template[size];
        }
    };
}
