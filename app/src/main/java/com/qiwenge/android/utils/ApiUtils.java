package com.qiwenge.android.utils;

/**
 * API工具类，生产不同的API地址。
 */
public class ApiUtils {

    private final static String HOST_NAME = "http://api.qiwenge.com";

    public final static String FORMAT_3_PARAMS = HOST_NAME + "/%s/%s/%s";

    public final static String FORMAT_2_PARAMS = HOST_NAME + "/%s/%s";

    public final static String FORMAT_1_PARAMS = HOST_NAME + "/%s";

    public static String build(String param) {
        return String.format(FORMAT_1_PARAMS, param);
    }

    public static String build(String param1, String param2) {
        return String.format(FORMAT_2_PARAMS, param1, param2);
    }

    public static String build(String param1, String param2, String param3) {
        return String.format(FORMAT_3_PARAMS, param1, param2, param3);
    }

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
     *
     * @return
     */
    public static String getStatement() {
        return String.format(FORMAT_1_PARAMS, "statement.txt");
    }

    /**
     * 检查版本更新
     *
     * @return
     */
    public static String getConfigures() {
        return String.format(FORMAT_1_PARAMS, ApiModels.configures);
    }

    /**
     * 检查Book是否更新
     *
     * @return
     */
    public static String checkBookUpdate() {
        return String.format(FORMAT_2_PARAMS, ApiModels.books, ApiModels.updates);
    }

    /**
     * 赞
     *
     * @param bookId
     * @return
     */
    public static String postBookVoteUp(String bookId) {
        return String.format(FORMAT_3_PARAMS, ApiModels.books, bookId, ApiModels.voteup);
    }

    public static String postFeedBack() {
        return String.format(FORMAT_1_PARAMS, ApiModels.feedbacks);
    }

    public static String putAuth() {
        return String.format(FORMAT_1_PARAMS, ApiModels.auths);
    }

    public static String postUser() {
        return String.format(FORMAT_1_PARAMS, ApiModels.users);
    }

    public static String getUser(String userId) {
        return String.format(FORMAT_2_PARAMS, ApiModels.users, userId);
    }

    public static String postLevel() {
        return String.format(FORMAT_1_PARAMS, ApiModels.levels);
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
