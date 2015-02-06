package com.qiwenge.android.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.qiwenge.android.entity.base.BaseModel;
import com.qiwenge.android.entity.base.Id;
import com.qiwenge.android.entity.base.Template;

/**
 * Created by Eric on 15/2/6.
 */
public class Mirror extends BaseModel implements Parcelable {

    public String book_id;

    public Template template = new Template();

    public Progress progress = new Progress();

    public boolean current = false;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.book_id);
        dest.writeParcelable(this.template, flags);
        dest.writeParcelable(this.progress, flags);
        dest.writeByte(current ? (byte) 1 : (byte) 0);
        dest.writeString(this.id);
        dest.writeParcelable(this._id, 0);
    }

    public Mirror() {
    }

    private Mirror(Parcel in) {
        this.book_id = in.readString();
        this.template = in.readParcelable(Template.class.getClassLoader());
        this.progress = in.readParcelable(Progress.class.getClassLoader());
        this.current = in.readByte() != 0;
        this.id = in.readString();
        this._id = in.readParcelable(Id.class.getClassLoader());
    }

    public static final Parcelable.Creator<Mirror> CREATOR = new Parcelable.Creator<Mirror>() {
        public Mirror createFromParcel(Parcel source) {
            return new Mirror(source);
        }

        public Mirror[] newArray(int size) {
            return new Mirror[size];
        }
    };
}
