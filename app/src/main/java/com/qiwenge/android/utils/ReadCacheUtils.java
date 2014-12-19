package com.qiwenge.android.utils;

import java.util.HashMap;

import com.qiwenge.android.entity.Chapter;

/**
 * ReadCacheUtils
 * 
 * Created by Eric on 2014年6月18日
 */
public class ReadCacheUtils {

	/**
	 * 阅读缓存。
	 */
	public static HashMap<String, Chapter> cache = new HashMap<String, Chapter>();;

	/**
	 * 临时缓存加载过的章节
	 * 
	 * @param chapter
	 */
	public static void addChapter(Chapter chapter) {
		if (chapter == null)
			return;
		if (!cache.containsKey(chapter.get_id())) {
			cache.put(chapter.getId(), chapter);
			System.out.println("缓存：" + chapter.title);
		}
	}

	/**
	 * 是否缓存了id为chapterId的章节
	 * 
	 * @param chapterId
	 * @return
	 */
	public boolean containsChapter(String chapterId) {
		return cache.containsKey(chapterId);
	}

	/**
	 * 获取id为chapterId的章节
	 * 
	 * @param chapterId
	 * @return
	 */
	public static Chapter getChapter(String chapterId) {
		return cache.get(chapterId);
	}

}
