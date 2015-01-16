package com.qiwenge.android.entity.base;

public class BaseModel {

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
