package com.dev1024.utils;

import android.os.Environment;

/**
 * StorageUtils
 * 
 * Created by John on 2014-5-19.
 */
public class StorageUtils {

	private static final String SDCARD_ROOT = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/";

	/**
	 * Return the root path of SDCard.
	 * 
	 * @return
	 */
	public static String getSDCardPath() {
		return SDCARD_ROOT;
	}

	public static boolean sdCardIsAvailable() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

}
