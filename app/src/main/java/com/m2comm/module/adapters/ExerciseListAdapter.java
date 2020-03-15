package com.m2comm.module.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.m2comm.kses_exercise.CalendarActivity;
import com.m2comm.kses_exercise.MyExerciseList;
import com.m2comm.kses_exercise.R;
import com.m2comm.module.dao.ExerciseDAO;
import com.m2comm.module.dao.ScheduleDAO;
import com.m2comm.module.models.AlarmDTO;
import com.m2comm.module.models.ExerciseDTO;
import com.m2comm.module.models.ScheduleDTO;
import com.tenclouds.gaugeseekbar.GaugeSeekBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ExerciseListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    List<ScheduleDTO> contentArray;
    private ArrayList<ExerciseDTO> exerciseDTOS;
    private ExerciseDAO exerciseDAO;
    private ScheduleDAO scheduleDAO;

    public ExerciseListAdapter(Context context, LayoutInflater layoutInflater , List<ScheduleDTO> contentArray) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.contentArray = contentArray;
    }

    @Override
    public int getCount() {
        return this.contentArray.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if ( convertView == null ) {
            final ScheduleDTO row = contentArray.get(position);
            this.exerciseDAO = new ExerciseDAO(this.context);
            this.scheduleDAO = new ScheduleDAO(this.context);

            convertView  = this.layoutInflater.inflate(R.layout.my_exercise_item,parent,false);
            final GaugeSeekBar gaugeSeekBar = convertView.findViewById(R.id.progress);
            final LinearLayout innerView = convertView.findViewById(R.id.main_innerView2);
            TextView main_per_count = convertView.findViewById(R.id.main_per_count);
            TextView count_day = convertView.findViewById(R.id.count_day);
            TextView count_date = convertView.findViewById(R.id.count_date);
            TextView run_state = convertView.findViewById(R.id.run_state);
            LinearLayout deleteBt = convertView.findViewById(R.id.deleteBt);
            gaugeSeekBar.post(new Runnable() {
                @Override
                public void run() {
                    innerView.post(new Runnable() {
                        @Override
                        public void run() {
                            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) innerView.getLayoutParams();
                            params.width = (int) (gaugeSeekBar.getHeight() / 1.4);
                            params.height = (int) (gaugeSeekBar.getHeight() / 1.4);
                            innerView.setLayoutParams(params);
                        }
                    });
                }
            });
            this.exerciseDTOS = this.exerciseDAO.finds(row.getNum());
            if (row.isRun()) {
                run_state.setText("운동진행중");
                run_state.setBackgroundColor(Color.parseColor("#d71447"));
            } else {
                run_state.setText("운동종료");
                run_state.setBackgroundColor(Color.parseColor("#3e51a2"));
            }
            count_date.setText(row.getSdate()+"~"+row.getEdate());
            count_day.setText(this.exerciseDTOS.size()+"");

            int per_count = this.exerciseDTOS.size() * 100 / 30;
            main_per_count.setText(String.valueOf(per_count));
            gaugeSeekBar.setProgress(per_count*0.01f);
            deleteBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(context).title(R.string.app_name)
                            .content("삭제하시겠습니까?")
                            .positiveText("네").negativeText("아니요").
                            theme(Theme.LIGHT).onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            //운동 일자를 저장
                            scheduleDAO.findDelete(row.getNum());
                            exerciseDAO.delete(row.getNum());
                        }
                    }).show();


                }
            });

        }

        return convertView;
    }



}
