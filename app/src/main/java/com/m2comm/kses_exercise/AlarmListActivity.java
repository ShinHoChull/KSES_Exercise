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
import com.m2comm.module.models.AlarmDTO;
import com.m2comm.module.models.ContentDTO;

import java.util.ArrayList;
import java.util.List;

public class AlarmListActivity extends AppCompatActivity {

    ContentTopActivity contentTopActivity;
    BottomActivity bottomActivity;
    private ListView listView;
    List<AlarmDTO> alarmList;
    AlarmDAO alarmDAO;

    private LinearLayout clock_view;
    private TextView alarmAddBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);
        this.idSetting();
        this.alarmAddBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , AlarmDetail.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
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
        this.clock_view = findViewById(R.id.alarm_nonItem_view);
        this.alarmAddBt = findViewById(R.id.add_alarmBt);

    }



}
