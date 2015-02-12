package com.qiwenge.android.entity;

/**
 * Created by Eric on 14-8-6.
 */
public class BookUpdate {

    public String book_id;

    public String mirror_id;

    /**
     * 是否更新
     */
    public int has_update = 0;

    public boolean updated() {
        return has_update == 1;
    }

    /**
     * 更新的数量
     */
    public int arrival = 0;

    /**
     * 章节总数
     */
    public int chapter_total = 0;

}
