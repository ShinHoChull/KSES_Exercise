package com.m2comm.kses_exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.m2comm.module.Common;
import com.m2comm.module.dao.ExerciseDAO;
import com.m2comm.module.dao.ScheduleDAO;
import com.m2comm.module.models.ExerciseDTO;
import com.m2comm.module.models.ScheduleDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PopupActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView close, centerImg ;
    TextView top_date, content, sub_content, bt1, bt2;
    final int todayCheckViewV = 0 ,
            todayCheckingV = 1,
            todayNotCheckingV = 2,
            alreadyTodayCheckedV = 3,
            alarmViewV = 4;

    ScheduleDTO row;
    private ScheduleDAO scheduleDAO;
    private ExerciseDAO exerciseDAO;

    //Schadule List
    private ArrayList<ExerciseDTO> schduleArray;

    private Date date;
    private Calendar cal;
    private String strDate , week;

    private int scheduleId = 0 , status;

    private void idSetting() {
        this.close = findViewById(R.id.pop_close);
        this.close.setColorFilter(Color.BLACK);
        this.close.setOnClickListener(this);

        this.centerImg = findViewById(R.id.img);
        this.top_date = findViewById(R.id.top_date);
        this.content = findViewById(R.id.content);
        this.sub_content = findViewById(R.id.sub_content);

        this.bt1 = findViewById(R.id.bt1);
        this.bt1.setOnClickListener(this);
        this.bt2 = findViewById(R.id.bt2);
        this.bt2.setOnClickListener(this);

        this.scheduleDAO = new ScheduleDAO(this);
        this.exerciseDAO = new ExerciseDAO(this);
        this.schduleArray = new ArrayList<>();

        if ( scheduleDAO.getID() > 1 ) row = this.scheduleDAO.find();
        if ( row != null ) {
            this.scheduleId = row.getNum();
            this.schduleArray = this.exerciseDAO.finds(this.scheduleId);
        }
    }

    private void todayDate() {
        // 오늘 날짜를 세팅 해준다.
        long now = System.currentTimeMillis();
        if ( date == null ) {

            this.date = new Date(now);
        }

        //연,월,일을 따로 저장
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
        this.strDate = curYearFormat.format(this.date) + "." + curMonthFormat.format(this.date) + "." + curDayFormat.format(this.date);

        this.cal = Calendar.getInstance();
        this.cal.setTime(this.date);
        this.week = this.getWeek(this.cal.get(Calendar.DAY_OF_WEEK));

        this.date = Common.getDate(this.strDate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        this.idSetting();

        Intent intent = getIntent();
        this.status = intent.getIntExtra("state",0);
        if ( intent.getLongExtra("date",-1) != -1 ) {
            Log.d("inDate","ok");
            this.date = new Date(intent.getLongExtra("date",-1));
        }
        this.todayDate();
        this.chageView(this.status);
    }

    private void todayCheck() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
        for ( ExerciseDTO row : schduleArray ) {
            try {
                Date rowDate = dateFormat.parse((String) row.getCheckDate());
                if ( rowDate.getTime() == this.date.getTime() ) {
                    this.status = this.alreadyTodayCheckedV;
                    this.chageView(this.status);
                    return;
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"reg 255 line Error",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if ( row != null && Common.getDate(row.getSdate()).getTime() <= this.date.getTime() && Common.getDate(row.getEdate()).getTime() >= this.date.getTime() ) {
            Calendar checkCalendar = Calendar.getInstance();
            checkCalendar.setTime(this.date);
            exerciseDAO.addExercise(new ExerciseDTO(0,row.getNum(),checkCalendar.get(Calendar.YEAR)+"."+
                    (checkCalendar.get(Calendar.MONTH)+1)+"."+checkCalendar.get(Calendar.DATE)));
            this.status = this.todayCheckingV;
            this.chageView(this.status);
        } else {
            Toast.makeText(getApplicationContext(),"오늘은 운동 기간이 아닙니다.",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.anim_slide_out_bottom_login);
    }

    private String getWeek(int dayNum) {
        String day = "";
        switch (dayNum) {
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;
        }
        return day;
    }

    public void chageView( int status ) {
        switch (status) {

            case todayCheckingV:
                this.todayChecking();
                break;

            case todayCheckViewV:
                this.todayCheckView();
                break;

            case todayNotCheckingV:
                this.todayNotChecking();
                break;

            case alreadyTodayCheckedV:
                this.alreadyTodayChecked();
                break;

            case alarmViewV:
                this.alarmView();
                break;

        }
    }

    public void submit( ) {
        //Toast.makeText(this,"yes="+status,Toast.LENGTH_SHORT).show();
        Intent intent;
        switch (this.status) {

            case todayCheckViewV:
                this.todayCheck();
                break;

            case todayCheckingV:
            case alreadyTodayCheckedV:
            case todayNotCheckingV:

                intent = new Intent(this, CalendarActivity.class);
                if( row == null )intent.putExtra("isStart",true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                if( row != null )overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                else overridePendingTransition(R.anim.anim_slide_in_bottom_login, 0);
                finish();
                break;

            case alarmViewV:
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                break;

        }
    }

    public void closeBtClick() {
        switch (this.status) {
            case todayCheckViewV:
                this.todayNotChecking();
                break;

            case todayCheckingV:
            case todayNotCheckingV:
            case alarmViewV:
            case alreadyTodayCheckedV:
                finish();
                break;
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt1:
                this.submit();
                break;

            case R.id.bt2:
                this.closeBtClick();
                break;

            case R.id.pop_close:
                finish();
                break;

        }
    }


    //운동체크하기
    private void todayCheckView() {
        this.top_date.setText(this.strDate+" ("+this.week+")");
        this.content.setText("오늘 견관절 운동을 \n하셨나요?");
        this.centerImg.setImageResource(R.drawable.main_pop1);
        this.sub_content.setVisibility(View.GONE);
        this.bt1.setText("네");
        this.bt2.setText("아니요");
    }

    //운동체크 후 네를 누를때
    private void todayChecking() {
        this.top_date.setText(this.strDate+" ("+this.week+")");
        this.content.setText("수고하셨습니다!");
        this.sub_content.setVisibility(View.VISIBLE);
        this.sub_content.setText("건강한 어깨를 위한 시작은\n견관절 운동으로 시작됩니다.");
        this.centerImg.setImageResource(R.drawable.main_pop3);
        this.bt1.setText("캘린더 바로가기");
        this.bt2.setText("닫기");
    }

    //운동체크 후 아니요를 누를때
    private void todayNotChecking() {
        this.top_date.setText(this.strDate+" ("+this.week+")");
        this.content.setText("늦지 않았습니다.\n지금 시작하세요!");
        this.sub_content.setVisibility(View.VISIBLE);
        this.sub_content.setText("건강한 어깨를 위한 시작은\n견관절 운동으로 시작됩니다.");
        this.centerImg.setImageResource(R.drawable.main_pop4);
        this.bt1.setText("캘린더 바로가기");
        this.bt2.setText("닫기");
        this.status = this.todayNotCheckingV;
    }

    //이미 운동체크
    private void alreadyTodayChecked() {
        this.top_date.setText(this.strDate+" ("+this.week+")");
        this.content.setText("이미\n운동을 체크하셨습니다.");
        this.sub_content.setVisibility(View.GONE);
        this.centerImg.setImageResource(R.drawable.main_pop2);
        this.bt1.setText("네");
        this.bt2.setText("닫기");
    }

    //알람이 울릴때
    private void alarmView() {
        this.top_date.setText(this.strDate+" ("+this.week+")");
        this.content.setText("건강한 어깨를 위해\n견관절 운동을 할 시간입니다!");
        this.sub_content.setVisibility(View.GONE);
        this.centerImg.setImageResource(R.drawable.main_pop5);
        this.bt1.setText("메인 바로가기");
        this.bt2.setText("아니요");
    }
}
