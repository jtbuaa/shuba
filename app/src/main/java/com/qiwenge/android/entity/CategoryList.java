package com.qiwenge.android.entity;

import java.util.ArrayList;
import java.util.List;

import com.qiwenge.android.entity.base.BaseModel;

/**
 * 分类的集合。
 * <p/>
 * Created by John on 2014年7月6日
 */
public class CategoryList extends BaseModel {

    public List<Category> result = new ArrayList<Category>();

    public int total;

}
