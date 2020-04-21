package com.m2comm.kses_exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.m2comm.module.adapters.AlarmViewAdapter;
import com.m2comm.module.adapters.ContentListViewAdapter;
import com.m2comm.module.dao.AlarmDAO;
import com.m2comm.module.dao.ScheduleDAO;
import com.m2comm.module.models.AlarmDTO;
import com.m2comm.module.models.ContentDTO;
import com.m2comm.module.models.ScheduleDTO;

import java.util.ArrayList;
import java.util.List;

public class AlarmListActivity extends AppCompatActivity {

    ContentTopActivity contentTopActivity;
    BottomActivity bottomActivity;
    private ListView listView;
    List<AlarmDTO> alarmList;
    private AlarmDAO alarmDAO;
    private ScheduleDAO scheduleDAO;
    ScheduleDTO row;
    private LinearLayout clock_view;
    private TextView alarmAddBt , alarm_list_start_exercise;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);
        this.idSetting();

        this.row = null;
        //데이터베이스 생성 전에 오류가 떨어져서 임시적으로 넣어둠.
        if ( scheduleDAO.getID() > 1 ) row = this.scheduleDAO.find();

        this.alarmAddBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , AlarmDetail.class);
                intent.putExtra("isStart",true);
                intent.putExtra("isList",true);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_bottom_login, 0);
            }
        });

        this.alarm_list_start_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( row != null ) {
                    Intent intent = new Intent(getApplicationContext() , AlarmDetail.class);
                    intent.putExtra("isStart",true);
                    intent.putExtra("isList",true);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext() , CalendarActivity.class);
                    intent.putExtra("isStart",true);
                    startActivity(intent);
                }
                overridePendingTransition(R.anim.anim_slide_in_bottom_login, 0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.alarmList = this.alarmDAO.getAllList();

        if ( this.alarmList.size() > 0 ) {
            this.alarmAddBt.setVisibility(View.VISIBLE);
            this.clock_view.setVisibility(View.GONE);
            AlarmViewAdapter contentListViewAdapter = new AlarmViewAdapter(this , getLayoutInflater() , this.alarmList);
            listView.setAdapter(contentListViewAdapter);
        } else {
            this.alarmAddBt.setVisibility(View.GONE);
            this.clock_view.setVisibility(View.VISIBLE);
        }
    }

    private void idSetting() {
        this.contentTopActivity = new ContentTopActivity(this ,this , getLayoutInflater() , R.id.content_top , "나의 운동기록");
        this.bottomActivity = new BottomActivity(getLayoutInflater() , R.id.bottom , this , this);
        this.listView = findViewById(R.id.alarm_list);
        this.alarmDAO = new AlarmDAO(this);
        this.scheduleDAO = new ScheduleDAO(this);
        this.clock_view = findViewById(R.id.alarm_nonItem_view);
        this.alarmAddBt = findViewById(R.id.add_alarmBt);
        this.alarm_list_start_exercise = findViewById(R.id.alarm_list_start_exercise);
    }




}
