package com.qiwenge.android.utils;


public class ApiUtils {

    private final static String HOST_NAME = "http://api.qiwenge.com";

    public final static String FORMAT_3_PARAMS = HOST_NAME + "/%s/%s/%s";

    public final static String FORMAT_2_PARAMS = HOST_NAME + "/%s/%s";

    public final static String FORMAT_1_PARAMS = HOST_NAME + "/%s";

    /**
     * 获取所有的书。
     * 
     * @return
     */
    public static String getBooks() {
        return String.format(FORMAT_1_PARAMS, ApiModels.books.toString());
    }

    /**
     * 获取一本书。
     * 
     * @param bookId 书id
     * @return
     */
    public static String getBook(String bookId) {
        return String.format(FORMAT_2_PARAMS, ApiModels.books, bookId);
    }

    /**
     * 获取一本书下的，所有章节
     * 
     * @return
     */
    public static String getBookChpaters() {
        return String.format(FORMAT_1_PARAMS, ApiModels.chapters);
    }

    /**
     * 获取一个章节
     * 
     * @param chapterId
     * @return
     */
    public static String getChapter(String chapterId) {
        return String.format(FORMAT_2_PARAMS, ApiModels.chapters, chapterId);
    }

    /**
     * post书籍点击自增
     * 
     * @param bookId
     * @return
     */
    public static String postViewTotal(String bookId) {
        return String.format(FORMAT_3_PARAMS, ApiModels.books, bookId, ApiModels.view_total);
    }

    /**
     * 获取相关推荐书籍
     * 
     * @param bookId
     * @return
     */
    public static String getRelated(String bookId) {
        return String.format(FORMAT_3_PARAMS, ApiModels.books, bookId, ApiModels.related);
    }

    /**
     * 获取分类列表
     * 
     * @return
     */
    public static String getCategories() {
        return String.format(FORMAT_1_PARAMS, ApiModels.categories.toString());
    }

    /**
     * 获取推荐列表
     * 
     * @return
     */
    public static String getRecommend() {
        return String.format(FORMAT_2_PARAMS, ApiModels.books, ApiModels.recommend);
    }

    /**
     * 获取书籍排行
     * 
     * @return
     */
    public static String getBooksByTop() {
        return String.format(FORMAT_2_PARAMS, ApiModels.books, ApiModels.top);
    }

    /**
     * 获取免责声明
     * @return
     */
    public static String getStatement(){
        return String.format(FORMAT_1_PARAMS, "statement.txt");
    }

    public static String getConfigures(){
        return String.format(FORMAT_1_PARAMS, ApiModels.configures);
    }

    /**
     * Test
     * 
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(postViewTotal("abcd"));
    }
}
