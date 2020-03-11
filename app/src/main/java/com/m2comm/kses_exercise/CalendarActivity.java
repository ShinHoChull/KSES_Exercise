package com.m2comm.kses_exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.m2comm.module.adapters.CustomSchduleGridViewAdapter;
import com.m2comm.module.models.ScheduleDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener {

    ContentTopActivity contentTopActivity;
    BottomActivity bottomActivity;

    /**
     * 그리드뷰
     */
    private GridView gridView;

    /**
     * 그리드뷰 어댑터
     */
    private CustomSchduleGridViewAdapter gridAdapter;

    /**
     * 캘린더 변수
     */
    private Calendar mCal;
    /**
     * 일 저장 할 리스트
     */
    private ArrayList<String> dayList;

    //Schadule List
    private ArrayList<ScheduleDTO> schduleArray;

    //Date
    private String realDate;

    //현재 날짜 표시
    Date date, nDate;

    //Line표시
    Calendar lineCal;

    //달력 좌측 , 우측 버튼
    private ImageView nextButton , backButton;

    private TextView tvDate , calendarNextBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        this.idSetting();
        this.schduleArray = new ArrayList<>();
        this.todayDate();
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
                try {
                    nDate = dateFormat.parse((String) tvDate.getText().toString()+"."+ dayList.get(position));

                    lineCal = Calendar.getInstance();
                    lineCal.setTime(nDate);
                    lineCal.add(Calendar.DAY_OF_MONTH , 30);
                    chnageAdapter();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Date click Error",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void idSetting() {
        this.contentTopActivity = new ContentTopActivity(this ,this , getLayoutInflater() , R.id.content_top,"나의 운동일");
        this.bottomActivity = new BottomActivity(getLayoutInflater() , R.id.bottom , this , this);

        this.gridView = findViewById(R.id.scheduleView);

        this.nextButton = findViewById(R.id.schadule_nextDateButton);
        this.nextButton.setColorFilter(Color.parseColor("#666666"));
        this.nextButton.setOnClickListener(this);

        this.backButton = findViewById(R.id.schadule_backDateButton);
        this.backButton.setColorFilter(Color.parseColor("#666666"));
        this.backButton.setOnClickListener(this);

        this.tvDate = findViewById(R.id.schadule_mainDate);
        this.dayList = new ArrayList<>();

        this.calendarNextBt = findViewById(R.id.calendar_nextBt);
        this.calendarNextBt.setOnClickListener(this);

    }

    public void chnageAdapter() {
        if (this.schduleArray == null) this.schduleArray = new ArrayList<ScheduleDTO>();
        if (this.gridAdapter != null) this.gridAdapter.notifyDataSetChanged();
        this.gridAdapter = new CustomSchduleGridViewAdapter(this, getLayoutInflater(), this.mCal, this.nDate,
                this.dayList, this.schduleArray,this.lineCal);
        this.gridView.setAdapter(this.gridAdapter);
    }

    /**
     * 해당 월에 표시할 일 수 구함
     *
     * @param month
     */
    private void setCalendarDate(int month) {
        this.mCal.set(this.mCal.MONTH, month - 1);
        //getActualMaximum => 마지막 일 현재날짜 기준 최대수
        //getMaximum => 마지막 일 카렌더가 가진 최대수
        for (int i = 0; i < this.mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            this.dayList.add("" + (i + 1));
        }
    }

    private void todayDate() {
        // 오늘에 날짜를 세팅 해준다.
        long now = System.currentTimeMillis();
        this.date = new Date(now);

        //연,월,일을 따로 저장
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        //현재 날짜 텍스트뷰에 뿌려줌
        this.tvDate.setText(curYearFormat.format(this.date) + "." + curMonthFormat.format(this.date));
        this.realDate = curYearFormat.format(this.date) + "." + curMonthFormat.format(this.date) + "." + curDayFormat.format(this.date);
        this.changeDate(0);
    }

    /**
     * Date Next 1 & Back -1
     *
     * @param temp
     */
    public void changeDate(int temp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);

        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFormat.parse((String) this.realDate));
            cal.add(this.mCal.MONTH, temp);

            this.mCal = cal;

            //연,월,일을 따로 저장
            final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
            final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
            final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
            this.realDate = curYearFormat.format(cal.getTime()) + "." + curMonthFormat.format(cal.getTime()) + "." + curDayFormat.format(cal.getTime());
            this.tvDate.setText(curYearFormat.format(cal.getTime()) + "." + curMonthFormat.format(cal.getTime()));

            this.mCal.set(Integer.parseInt(curYearFormat.format(cal.getTime())), Integer.parseInt(curMonthFormat.format(cal.getTime())) - 1, 1);
            int dayNum = this.mCal.get(this.mCal.DAY_OF_WEEK);
            //1일 - 요일 매칭 시키기 위해 공백 add
            this.dayList.clear();
            for (int i = 1; i < dayNum; i++) {
                this.dayList.add("");
            }
            setCalendarDate(this.mCal.get(mCal.MONTH) + 1);

            //월 데이트 가져오기..
            //this.getMonthEvent(curYearFormat.format(cal.getTime()) + "-" + curMonthFormat.format(cal.getTime()));
            this.chnageAdapter();

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.schadule_nextDateButton:
                this.changeDate(1);
                break;

            case R.id.schadule_backDateButton:
                this.changeDate(-1);
                break;

            case R.id.calendar_nextBt:
                intent = new Intent(this , AlarmDetail.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;

        }
    }
}
