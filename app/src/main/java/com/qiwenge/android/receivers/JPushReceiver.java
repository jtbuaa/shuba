package com.qiwenge.android.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

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

        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)) {
            receiveMessage(context, intent.getExtras());
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(action)) {

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(action)) {
            openNotify(context, intent.getExtras());
        }
    }

    private void receiveMessage(Context context, Bundle bundle) {
        String title = context.getString(R.string.app_name);
        String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        if (StringUtils.isEmptyOrNull(msg)) {
            msg = "unknown message";
        }
        showJPUSHNotify(context, title, msg, bundle);
    }

    private void showJPUSHNotify(Context context, String title, String summary, Bundle extras) {
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
        notification.icon = R.drawable.ic_launcher;
        notification.tickerText = summary;
        notification.defaults = Notification.DEFAULT_SOUND;
        manager.notify(NOTIFY_NUM, notification);
    }

    private PendingIntent createPendingIntent(Context context, String action, Bundle extras) {
        Intent intent = new Intent(action);
        intent.putExtras(extras);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, NOTIFY_NUM, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private void openNotify(Context context, Bundle bundle) {
        Intent i = new Intent(context, MainActivity.class);
        i.putExtras(bundle);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

}
