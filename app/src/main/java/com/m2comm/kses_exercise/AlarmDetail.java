package com.m2comm.kses_exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.m2comm.module.AlarmReceiver;
import com.m2comm.module.dao.AlarmDAO;
import com.m2comm.module.models.AlarmDTO;

import java.util.Calendar;

public class AlarmDetail extends AppCompatActivity implements View.OnClickListener {

    ContentTopActivity contentTopActivity;
    BottomActivity bottomActivity;

    TextView sun , mon , tue , wed , thu , fri , sat , successBt;
    LinearLayout sunV , monV , tueV , wedV , thuV , friV , satV;
    int hour , min;
    byte[] week = { 0 ,0 ,0 ,0 ,0 ,0 ,0 ,0  };

    AlarmManager mAlarmManager;
    AlarmDAO alarmDAO;
    Resources system;
    TimePicker time_picker;
    private int alarm_max_num;

    private void idSetting() {
        this.contentTopActivity = new ContentTopActivity(this ,this , getLayoutInflater() , R.id.content_top,"나의 운동일");
        this.bottomActivity = new BottomActivity(getLayoutInflater() , R.id.bottom , this , this);
        this.mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        this.alarmDAO = new AlarmDAO(this);
        this.time_picker = findViewById(R.id.timepicker);
        this.successBt = findViewById(R.id.alarm_detail_success);
        this.successBt.setOnClickListener(this);
        this.sun = findViewById(R.id.sun);
        this.sunV = findViewById(R.id.sun_cir);
        this.sun.setOnClickListener(this);
        this.mon = findViewById(R.id.mon);
        this.monV = findViewById(R.id.mon_cir);
        this.mon.setOnClickListener(this);
        this.tue = findViewById(R.id.tue);
        this.tueV = findViewById(R.id.tue_cir);
        this.tue.setOnClickListener(this);
        this.wed = findViewById(R.id.wed);
        this.wedV = findViewById(R.id.wed_cir);
        this.wed.setOnClickListener(this);
        this.thu = findViewById(R.id.thu);
        this.thuV = findViewById(R.id.thu_cir);
        this.thu.setOnClickListener(this);
        this.fri = findViewById(R.id.fri);
        this.friV = findViewById(R.id.fri_cir);
        this.fri.setOnClickListener(this);
        this.sat = findViewById(R.id.sat);
        this.satV = findViewById(R.id.sat_cir);
        this.sat.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_detail);
        this.idSetting();
        this.time_picker.setIs24HourView(false);
        this.time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                min = minute;
            }
        });
        //Pending Intent ID를 기억하기위해서 DB의 Max ID를 가져와서
        this.alarm_max_num = this.alarmDAO.getID();
    }


    private void isCheck(TextView tv , LinearLayout linearLayout ,  int num) {
        this.week[num] = (byte) (this.week[num] == 0 ? 1 : 0);

        if ( this.week[num] == 1 ) {
            tv.setTextColor(Color.parseColor("#ffffff"));
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            tv.setTextColor(Color.parseColor("#000000"));
            linearLayout.setVisibility(View.GONE);
        }
    }

    private void registAlarm()
    {
        //cancelAlarm();

        boolean isRepeat = false;
        int len = week.length;
        for (int i = 0; i < len; i++)
        {
            if (week[i] == 1)
            {
                isRepeat = true;
                break;
            }
        }

        if ( ! isRepeat ) {
            Toast.makeText(this , "요일반복을 선택해주세요." , Toast.LENGTH_SHORT).show();
            return;
        }

        // 알람 등록
        Intent intent = new Intent(this, AlarmReceiver.class);

        long triggerTime = 0;
        long intervalTime = 24 * 60 * 60 * 1000;// 24시간
        if(isRepeat)
        {
            intent.putExtra("one_time", false);
            intent.putExtra("day_of_week", week);
            //requestCode는 Noti ID
            PendingIntent pending = PendingIntent.getBroadcast(this , this.alarm_max_num , intent , 0);
            triggerTime = setTriggerTime();
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, intervalTime, pending);
        } else {
            intent.putExtra("one_time", true);
            PendingIntent pending = getPendingIntent(intent);
            triggerTime = setTriggerTime();
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pending);
        }
        Toast.makeText(this , "설정 완료 되었습니다.",Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    private PendingIntent getPendingIntent(Intent intent)
    {
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pIntent;
    }

    private long setTriggerTime()
    {
        // current Time
        long atime = System.currentTimeMillis();
        // timepicker
        Calendar curTime = Calendar.getInstance();
        curTime.set(Calendar.HOUR_OF_DAY, hour);
        curTime.set(Calendar.MINUTE, min);
        curTime.set(Calendar.SECOND, 0);
        curTime.set(Calendar.MILLISECOND, 0);
        long btime = curTime.getTimeInMillis();
        long triggerTime = btime;
        if (atime > btime)
            triggerTime += 1000 * 60 * 60 * 24;

        return triggerTime;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sun:
                this.isCheck(this.sun , this.sunV , 1);
                break;
            case R.id.mon:
                this.isCheck(this.mon , this.monV ,2);
                break;
            case R.id.tue:
                this.isCheck(this.tue , this.tueV ,3);
                break;
            case R.id.wed:
                this.isCheck(this.wed , this.wedV ,4);
                break;
            case R.id.thu:
                this.isCheck(this.thu , this.thuV ,5);
                break;
            case R.id.fri:
                this.isCheck(this.fri , this.friV ,6);
                break;
            case R.id.sat:
                this.isCheck(this.sat , this.satV ,7);
                break;
            case R.id.alarm_detail_success:

                if  (this.hour < 0 && this.min < 0) {
                    Toast.makeText(this , "알람 시간을 선택해주세요." , Toast.LENGTH_SHORT).show();
                    return;
                }

                String am_pm = "AM";
                if ( hour > 12 ) {
                    am_pm = "PM";
                }

                if ( alarmDAO.addAlarm(new AlarmDTO(0,am_pm,hour+":"+min,true,week,0)) ) {
                    this.registAlarm();
                }

            break;
        }

    }
}

