package com.dev1024.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

/**
 * NotifyUtils
 * 
 * Created by John on 2014-5-19.
 */
public class NotifyUtils {

	/**
	 * Clear all notifications。
	 * 
	 * @param context
	 */
	public static void clearAll(Context context) {
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancelAll();
	}

	/**
	 * Show a notifications.
	 * 
	 * @param context
	 * @param title
	 * @param summary
	 * @param iconRsid
	 * @param classOf
	 */
	public static void showNotify(Context context, String title,
			String summary, int iconRsid, Class<?> intentClass) {
		showNotify(context, title, summary, iconRsid, 0, intentClass);
	}

	/**
	 * Show a notifications.
	 * 
	 * @param context
	 * @param title
	 * @param summary
	 * @param iconRsid
	 * @param soundRsid
	 * @param intentClass
	 */
	public static void showNotify(Context context, String title,
			String summary, int iconRsid, int soundRsid, Class<?> intentClass) {
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Builder builder = new NotificationCompat.Builder(context);
		builder.setContentTitle(title);
		builder.setContentText(summary);
		builder.setSmallIcon(iconRsid);

		if (intentClass != null)
			builder.setContentIntent(createPendingIntent(context, intentClass));

		Notification notification = builder.build();
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.icon = iconRsid;
		notification.tickerText = summary;
		if (soundRsid > 0) {
			notification.sound = Uri.parse("android.resource://"
					+ context.getPackageName() + "/" + soundRsid);
		}
		manager.notify(0, notification);
	}

	/**
	 * Return a PendingIntent that should skip to...。
	 * 
	 * @param context
	 * @param classOf
	 * @return
	 */
	public static PendingIntent createPendingIntent(Context context,
			Class<?> classOf) {
		Intent intent = new Intent(context, classOf);
		PendingIntent pi = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		return pi;
	}

}
