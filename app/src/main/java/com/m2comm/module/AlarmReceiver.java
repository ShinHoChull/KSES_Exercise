package com.m2comm.module;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.m2comm.kses_exercise.R;

import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extra = intent.getExtras();
        if (extra != null)
        {
            boolean isOneTime = extra.getBoolean("one_time");
            if (isOneTime) {
                //AlarmDataManager.getInstance().setAlarmEnable(context, false);
                // 알람 울리기.
            } else {
                int num = extra.getInt("num");
                byte[] week = extra.getByteArray("day_of_week");
                Log.d("kkkk","week"+week);
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                if (week[Integer.valueOf(cal.get(Calendar.DAY_OF_WEEK))] == 1) {
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel mChannel = new NotificationChannel("ebookPush", "ebookPush", NotificationManager.IMPORTANCE_DEFAULT);
                        notificationManager.createNotificationChannel(mChannel);
                        builder = new NotificationCompat.Builder(context, mChannel.getId());
                    } else {
                        builder = new NotificationCompat.Builder(context);
                    }

                    builder.setAutoCancel(true)
                            .setSmallIcon((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ? R.mipmap.ic_launcher : R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                            .setTicker("Hello")
                            .setContentTitle("Alarm!!")
                            .setContentText("반갑습니다.")
                            .setPriority(NotificationCompat.PRIORITY_MAX); //최대로 펼침


                    builder.setContentIntent(pendingIntent);
                    builder.setDefaults(Notification.DEFAULT_VIBRATE);
                    builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

                    notificationManager.notify(0, builder.build());
                }

                // 알람 울리기.
            }
        }
    }

}
