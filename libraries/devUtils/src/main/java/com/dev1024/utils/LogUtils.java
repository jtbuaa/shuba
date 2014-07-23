package com.dev1024.utils;

import android.util.Log;

/**
 * LogUtils
 * 
 * Created by John on 2014-5-19.
 */
public class LogUtils {

	/**
	 * Log.d
	 * <p>
	 * Send a DEBUG log message.
	 * 
	 * @param TAG
	 * @param msg
	 */
	public static void d(String TAG, String msg) {
		if (msg == null)
			return;
		Log.d(TAG, msg);
	}

	/**
	 * Log.e
	 * <p>
	 * Send an ERROR log message.
	 * 
	 * @param TAG
	 * @param msg
	 */
	public static void e(String TAG, String msg) {
		if (msg == null)
			return;
		Log.e(TAG, msg);
	}

	/**
	 * Log.i
	 * <p>
	 * Send an INFO log message.
	 * 
	 * @param TAG
	 * @param msg
	 */
	public static void i(String TAG, String msg) {
		if (msg == null)
			return;
		Log.i(TAG, msg);
	}

	/**
	 * Log.v
	 * <p>
	 * Send a VERBOSE log message.
	 * 
	 * @param TAG
	 * @param msg
	 */
	public static void v(String TAG, String msg) {
		if (msg == null)
			return;
		Log.v(TAG, msg);
	}

	/**
	 * Log.w
	 * <p>
	 * Send a WARN log message.
	 * 
	 * @param TAG
	 * @param msg
	 */
	public static void w(String TAG, String msg) {
		if (msg == null)
			return;
		Log.w(TAG, msg);
	}

	public static void println(String msg) {
		if (msg == null)
			return;
		System.out.println(msg);
	}

	public static void println(String TAG, String msg) {
		if (msg == null)
			return;
		System.out.println(String.format("%s------>%s", TAG, msg));
	}

	public static void println(String className, String methodName, String msg) {
		if (msg == null)
			return;
		System.out.println(String.format("%s:%s---->%s", className, methodName,
				msg));
	}

}
