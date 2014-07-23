package com.dev1024.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * AppUtils
 * 
 * Created by John on 2014-5-19.
 */
public class AppUtils {

	/**
	 * Return the model of product.
	 * 
	 * @return
	 */
	public static String getPhoneModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 
	 * Return the name of the overall product.
	 * 
	 * @return
	 */
	public static String getProduct() {
		return android.os.Build.PRODUCT;
	}

	public static String getVersionRELEASE() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * Return the IMEI code
	 * 
	 * @param context
	 * @return
	 */
	public static String getImeiCode(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/**
	 * Return the mobile number.
	 * 
	 * @param context
	 * @return
	 */
	public static String getMobileNumber(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getLine1Number();
	}

	/**
	 * Return version name of the application.
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		PackageInfo info = getPackageInfo(context);
		return info.versionName;
	}

	/**
	 * Return version code of the application.
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		PackageInfo info = getPackageInfo(context);
		return info.versionCode;
	}

	/**
	 * Return the package name of the application.
	 * 
	 * @param context
	 * @return
	 */
	public static String getPackageName(Context context) {
		PackageInfo info = getPackageInfo(context);
		return info.packageName;
	}

	/**
	 * Return the package info.
	 * 
	 * @param context
	 * @return
	 */
	public static PackageInfo getPackageInfo(Context context) {
		PackageManager manager = null;
		PackageInfo info = null;
		manager = context.getPackageManager();
		try {
			info = manager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			Log.e("AppUtils", e.getMessage());
		}
		return info;
	}

}
