package com.m2comm.kses_exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.m2comm.module.adapters.ExerciseListAdapter;
import com.m2comm.module.dao.ScheduleDAO;
import com.m2comm.module.models.ScheduleDTO;

import java.util.ArrayList;
import java.util.List;

public class MyExerciseList extends AppCompatActivity implements View.OnClickListener {

    MyExerciseListTopActivity contentTopActivity;
    BottomActivity bottomActivity;
    private ListView listView;
    private List<ScheduleDTO> scheduleDTOS;
    private ExerciseListAdapter exerciseListAdapter;
    private LinearLayout nonImageView;
    private ScheduleDAO scheduleDAO;
    private TextView list_start_exercise;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_exercise_list);
        this.idSetting();
        this.reload();
    }

    private void reload() {
        if ( scheduleDAO.getID() > 1 ) {
            this.scheduleDTOS = this.scheduleDAO.getAllList();
        } else {
            this.scheduleDTOS = new ArrayList<>();
        }
        this.exerciseListAdapter = new ExerciseListAdapter(this,getLayoutInflater(),this.scheduleDTOS);
        this.listView.setAdapter(this.exerciseListAdapter);
        this.exerciseListAdapter.notifyDataSetChanged();

        if ( this.scheduleDTOS.size() > 0 ) {
            this.nonImageView.setVisibility(View.GONE);
        } else {
            this.nonImageView.setVisibility(View.VISIBLE);
        }
    }

    public void updateListView() {
        this.reload();
    }

    private void idSetting () {
        this.contentTopActivity = new MyExerciseListTopActivity(this ,this , getLayoutInflater() , R.id.content_top,"나의 운동기록");
        this.bottomActivity = new BottomActivity(getLayoutInflater() , R.id.bottom , this , this);
        this.listView = findViewById(R.id.listView);
        this.nonImageView = findViewById(R.id.nonItem_view);
        this.scheduleDAO = new ScheduleDAO(this);
        this.list_start_exercise = findViewById(R.id.list_start_exercise);
        this.list_start_exercise.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.list_start_exercise:
                Intent intent = new Intent(this, CalendarActivity.class);
                intent.putExtra("isStart",true);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_bottom_login, 0);
                break;
        }
    }
}
