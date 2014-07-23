package com.dev1024.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

/**
 * HtmlUtils
 * 
 * Created by John on 2014-5-19.
 */
public class HtmlUtils {

	private static final String TAG = "HtmlUtils";

	/**
	 * Return the hostname of a url.
	 * 
	 * @param url
	 * @return
	 */
	public static String getHostNameForUrl(String url) {
		Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
		Matcher m = p.matcher(url);
		if (m.find()) {
			return m.group();
		}
		return url;
	}

	/**
	 * Return some image paths from a html string.
	 * 
	 * @param htmlStr
	 * @return
	 */
	public static List<String> getImgFromHtmlStr(String htmlStr) {
		String img = "";
		Pattern p_image;
		Matcher m_image;
		List<String> pics = new ArrayList<String>();
		String regEx_img = "<img.*src=(.*?)[^>]*?>";
		p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
		m_image = p_image.matcher(htmlStr);
		Matcher m;
		while (m_image.find()) {
			img = img + "," + m_image.group();
			m = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img);
			while (m.find()) {
				pics.add(m.group(1));
			}
		}
		return pics;
	}

	/**
	 * Return a String appended css style.
	 * 
	 * @param loadData
	 *            a String of data in the given encoding
	 * @param cssName
	 *            The <tt>CSS</tt> file's name .It is located in the
	 *            <tt>assets</tt> folder.
	 * @return
	 */
	public static String loadDataWithLoaclCSS(String loadData, String cssName) {
		String header = "<html><head><link href=\"file:///android_asset/%s\" type=\"text/css\" rel=\"stylesheet\"/></head><body>";
		String footer = "</body></html>";
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(header, cssName));
		sb.append(loadData);
		sb.append(footer);
		return sb.toString();
	}

	/**
	 * Append a html tag at front.
	 * 
	 * @param loadData
	 *            a String of data in the given encoding
	 * @param tagName
	 *            tag name
	 * @param appendString
	 *            appended string
	 * @return
	 */
	public static String appendHtmlTagAtFront(String loadData, String tagName,
			String appendString) {
		String left = String.format("<%s>", tagName);
		String right = String.format("</%s>", tagName);
		StringBuilder sb = new StringBuilder();
		sb.append(left);
		sb.append(appendString);
		sb.append(right);
		sb.append(loadData);
		return sb.toString();
	}

	/**
	 * Append a html tag at behind.
	 * 
	 * @param loadData
	 *            a String of data in the given encoding
	 * @param tagName
	 *            tag name
	 * @param appendString
	 *            appended string
	 * @return
	 */
	public static String appendHtmlTagAtBehind(String loadData, String tagName,
			String appendString) {
		String left = String.format("<%s>", tagName);
		String right = String.format("</%s>", tagName);
		StringBuilder sb = new StringBuilder();
		sb.append(loadData);
		sb.append(left);
		sb.append(appendString);
		sb.append(right);
		return sb.toString();
	}

	/**
	 * Remove some unnecessary tags.
	 * <p>
	 * e.g. , hr
	 * 
	 * @param loadData
	 * @return
	 */
	public static String removeTags(String loadData) {
		String pattern = "<hr/>";
		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(loadData);
		String result = m.replaceAll("");
		return result;
	}

	/**
	 * Remove all image tags from a html string.
	 * 
	 * @param htmlStr
	 * @return
	 */
	public static String removeImgTags(String htmlStr) {
		String result = "";
		Pattern pattern;
		Matcher matcher;
		try {
			String regEx = "<img.*src=(.*?)[^>]*?>";
			pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(htmlStr);
			result = matcher.replaceAll("");
		} catch (Exception e) {
			System.err.println("filteImgTag: " + e.getMessage());
		}
		return result;
	}

	/**
	 * Remove all html,script and style tags.
	 * 
	 * @param inputString
	 * @return
	 */
	public static String removeAllTags(String htmlStr) {
		Pattern pScript;
		Pattern pHtml;
		Pattern pStyle;
		Matcher mScript;
		Matcher mStyle;
		Matcher mHtml;
		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
			String regEx_html = "<[^>]+>";
			pScript = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			mScript = pScript.matcher(htmlStr);
			htmlStr = mScript.replaceAll(""); // delete all script tags.
			pStyle = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			mStyle = pStyle.matcher(htmlStr);
			htmlStr = mStyle.replaceAll(""); // delete all style tags.
			pHtml = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			mHtml = pHtml.matcher(htmlStr);
			htmlStr = mHtml.replaceAll(""); // delete all html tags.
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return htmlStr;
	}

	public static void main(String[] args) {
		System.out.println(getHostNameForUrl("http://www.36kr.com/feed/"));
	}

}
