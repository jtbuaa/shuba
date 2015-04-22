package com.qiwenge.android.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.liuguangqiang.common.utils.StringUtils;
import com.qiwenge.android.R;
import com.qiwenge.android.act.MainActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义JpushReceiver
 */
public class JPushReceiver extends BroadcastReceiver {

    private static final String TAG = "MyJpushReceiver";

    private static int NOTIFY_NUM = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        Log.i(TAG, "onReceive:" + action);

        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)) {
            receiveMessage(context, intent.getExtras());
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(action)) {
            receiveMessage(context, intent.getExtras());
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(action)) {
            openNotify(context, intent.getExtras());
        }
    }

    private void receiveMessage(Context context, Bundle bundle) {
        Log.i(TAG, "receiveMessage");
        printBundle(bundle);
        String title = context.getString(R.string.app_name);

        String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);

        if (StringUtils.isEmptyOrNull(msg)) {
            msg = bundle.getString(JPushInterface.EXTRA_ALERT);
        }

        if (StringUtils.isEmptyOrNull(msg)) {
            msg = "unknown message";
        }

        showJPUSHNotify(context, title, msg, bundle);
    }

    private void showJPUSHNotify(Context context, String title, String summary, Bundle extras) {
        Log.i(TAG, "showJPUSHNotify");
        NOTIFY_NUM = NOTIFY_NUM + 1;
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
        bigStyle.bigText(summary);
        bigStyle.setBigContentTitle(title);
        builder.setStyle(bigStyle);
        builder.setContentTitle(title);
        builder.setContentText(summary);
        builder.setContentIntent(createPendingIntent(context,
                JPushInterface.ACTION_NOTIFICATION_OPENED, extras));
        builder.setTicker(summary);
        builder.setSmallIcon(R.drawable.ic_launcher);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.tickerText = summary;
        notification.defaults = Notification.DEFAULT_SOUND;
        if (Build.VERSION.SDK_INT >= 19) {
            notification.icon = R.drawable.ic_notify_logo;
        } else {
            notification.icon = R.drawable.ic_launcher;
        }
        manager.cancelAll();
        manager.notify(NOTIFY_NUM, notification);
    }

    private PendingIntent createPendingIntent(Context context, String action, Bundle extras) {
        Intent intent = new Intent(action);
        intent.putExtras(extras);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, NOTIFY_NUM, intent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
    }

    private void openNotify(Context context, Bundle bundle) {
        Log.i(TAG, "openNotify");
        Intent i = new Intent(context, MainActivity.class);
        i.putExtras(bundle);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    private void printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        Log.i(TAG, "printBundle:" + sb.toString());
    }

}
