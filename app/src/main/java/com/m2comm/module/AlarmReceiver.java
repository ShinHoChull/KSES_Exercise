package com.m2comm.module;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.m2comm.kses_exercise.MainActivity;
import com.m2comm.kses_exercise.PopupActivity;
import com.m2comm.kses_exercise.R;
import com.m2comm.module.dao.AlarmDAO;
import com.m2comm.module.dao.ScheduleDAO;
import com.m2comm.module.models.AlarmDTO;
import com.m2comm.module.models.ScheduleDTO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {

    ScheduleDAO scheduleDAO;
    AlarmDAO alarmDAO;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Bundle extra = intent.getExtras();
        this.scheduleDAO = new ScheduleDAO(context);
        this.alarmDAO = new AlarmDAO(context);
        if (extra != null)
        {
            boolean isOneTime = extra.getBoolean("one_time");
            if (isOneTime) {
                //AlarmDataManager.getInstance().setAlarmEnable(context, false);
                // 알람 울리기.
            } else {
                int num = extra.getInt("alarmNum");
                int scheduleNum = extra.getInt("alarmNum");
                byte[] week = extra.getByteArray("day_of_week");

                Log.d("kkkk","sche="+scheduleNum);
                Log.d("kkkk","num="+num);
                if ( scheduleNum <= 0) {
                    return;
                }
                //전송된 푸시가 활성화가 아닐때 등록된 푸시를 삭제...
                ScheduleDTO scheduleDTO = this.scheduleDAO.find(scheduleNum);
                if ( scheduleDTO != null ) {
                    if (!scheduleDTO.isRun()) {
                        this.cancelAlarm(context,num);
                        return;
                    }
                    Log.d("schedu",scheduleDTO.getEdate());
                    //저장한 날짜 Month를 +1 해서 번거로운 작업을 해야한다....
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    String nCal = cal.get(Calendar.YEAR)+"."+(cal.get(Calendar.MONTH)+1)+"."+cal.get(Calendar.DATE);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);

                    try{
                        Date nDate = dateFormat.parse(nCal);
                        Date eDate = dateFormat.parse(scheduleDTO.getEdate());
                        //예약 푸시가 발송된 시간이 저장된 스케줄 날짜보다 후이면 종료시킨다.
                        if (nDate.getTime() > eDate.getTime()) {
                            this.cancelAlarm(context,num);
                            return;
                        }
                    } catch (Exception e) {
                        Log.d("alarmReceiverError",e.toString());
                    }
                }

                //등록된 알림이 꺼져있을때
                AlarmDTO alarmDTO = this.alarmDAO.find(num);
                if ( alarmDTO == null || !alarmDTO.isPush()) {
                    return;
                }

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                if (week[Integer.valueOf(cal.get(Calendar.DAY_OF_WEEK))] == 1) {
                    ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
                    ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
                    Log.d("activityManager","Actiivtymanager="+cn.getClassName());
                    if ( cn.getClassName().contains("m2comm") ) {
                        Bundle bun = new Bundle();
                        Intent popupIntent = new Intent(context, PopupActivity.class);
                        popupIntent.putExtra("state", 4);
                        popupIntent.putExtras(bun);
                        PendingIntent pie= PendingIntent.getActivity(context, 0, popupIntent, PendingIntent.FLAG_ONE_SHOT);
                        try {
                            pie.send();
                        } catch (PendingIntent.CanceledException e) {
                            Log.d("serviceError",e.toString());
                        }
                    } else {
                        this.sendPush(context, intent);
                    }
                }
                // 알람 울리기.
            }
        }
    }

    public void sendPush (Context context , Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(context.getResources().getString(R.string.app_name), context.getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);
            builder = new NotificationCompat.Builder(context, mChannel.getId());
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        builder.setAutoCancel(true)
                .setSmallIcon((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ? R.mipmap.ic_launcher : R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setTicker(context.getResources().getText(R.string.app_name))
                .setContentTitle(context.getResources().getText(R.string.app_name))
                .setContentText("오늘 견관절 운동을 하셨나요?")
                .setPriority(NotificationCompat.PRIORITY_MAX); //최대로 펼침

        builder.setContentIntent(pendingIntent);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        notificationManager.notify(0, builder.build());
    }

    public void cancelAlarm(Context context , int alarmId) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,alarmId,intent,0);
        alarmManager.cancel(pendingIntent);
    }


}
