package com.qiwenge.android.entity;

import com.qiwenge.android.entity.base.BaseModel;

/**
 * 章节。 Chapter
 * <p/>
 * Created by John on 2014年5月5日
 */
public class Chapter extends BaseModel {

    /**
     * 章节标题
     */
    public String title;

    /**
     * 书籍名字
     */
    public String book_title;

    /**
     * 章节编号，整型
     */
    public int number;

    /**
     * 内容
     */
    public String content;

    /**
     * 审核状态
     * <p/>
     * 0：未审核；1:已经审核
     */
    public int status;

    /**
     * 内容来源，URL
     */
    public String source;

    /**
     * 上一页
     */
    public RefModel prev;

    /**
     * 下一页
     */
    public RefModel next;

    /**
     * 是否被选中，目录中定位到已经看过的地方。
     */
    public boolean isSelected = false;
}
