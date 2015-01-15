package com.qiwenge.android.entity.base;

import com.j256.ormlite.field.DatabaseField;

public class BaseModel {

    public static final String ID = "id";

    @DatabaseField
    private String id;

    public Id _id;

    public String getId() {
        if (id == null)
            setId(_id.get$id());
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
