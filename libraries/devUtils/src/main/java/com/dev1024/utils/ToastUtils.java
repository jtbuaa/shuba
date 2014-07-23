package com.dev1024.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * ToastUtils
 * 
 * Created by John on 2014-5-19
 */
public class ToastUtils {

	/**
	 * Show a toast.
	 * 
	 * @param context
	 * @param content
	 */
	public static void alert(Context context, String content) {
		if (context == null || content == null) {
			return;
		}
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
	}

}
