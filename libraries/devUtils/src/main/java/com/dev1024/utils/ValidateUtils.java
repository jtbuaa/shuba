package com.dev1024.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ValidateUtils
 * 
 * Created by John on 2014-5-19.
 */
public class ValidateUtils {

	public static boolean isMobileNumber(String str) {
		String reg = "^(13[0-9]|15[0-9]|18[7|8|9|6|5])\\d{4,8}$";
		return validate(reg, str);
	}

	public static boolean isEmail(String str) {
		String reg = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]*@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		return validate(reg, str);
	}

	public static boolean isNumeric(String str) {
		return validate("^[0-9]+$", str);
	}

	public static boolean isNumAndAlphabet(String str) {
		return validate("^[A-Za-z0-9]+$", str);
	}

	public static boolean isUrl(String str) {
		String reg = "(http(s)?://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
		return validate(reg, str);
	}

	public static boolean containsChinese(String str) {
		int count = 0;
		String regEx = "[\\u4e00-\\u9fa5]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		while (m.find()) {
			for (int i = 0; i <= m.groupCount(); i++) {
				count++;
			}
		}
		if (count > 0) {
			return true;
		}
		return false;
	}

	public static boolean validate(String expression, String str) {
		Pattern p = Pattern.compile(expression);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * Unit tests the <tt>ValidateUtils</tt>.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(isNumAndAlphabet("a111."));
	}

}
