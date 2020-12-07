package com.m2comm.kses_exercise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.camera2.params.MeteringRectangle;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.m2comm.module.Common;
import com.m2comm.module.CustomHandler;
import com.m2comm.module.adapters.CustomSchduleGridViewAdapter;
import com.m2comm.module.dao.ExerciseDAO;
import com.m2comm.module.dao.ScheduleDAO;
import com.m2comm.module.models.ExerciseDTO;
import com.m2comm.module.models.ScheduleDTO;
import com.tenclouds.gaugeseekbar.GaugeSeekBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener {

    ContentTopActivity contentTopActivity;
    PopTopActivity popTopActivity;
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
    private ArrayList<ExerciseDTO> schduleArray;

    //Date
    private String realDate;

    //현재 날짜 표시
    Date date, nDate , eDate;

    //Line표시
    Calendar lineCal;

    //상단 스텝
    LinearLayout step1 , step2 , step3 , runBt;

    //하단 버튼
    private TextView  calendarNextBt , todayBt;

    //달력 좌측 , 우측 버튼
    private ImageView nextButton , backButton;
    private TextView tvDate;

    //등록된 스케줄이 있을 경우
    private boolean isSchedul = false;
    //운동시작일 설정 버튼을 누를경우
    private boolean isRun = false;
    private boolean isToday = false;

    private String startDate = "";
    private String endDate = "";
    private int scheduleId = 0;

    private ScheduleDAO scheduleDAO;
    private ExerciseDAO exerciseDAO;

    //프로그레스
    GaugeSeekBar gaugeSeekBar;
    LinearLayout innerView;

    //프로그레스에 필요한 변수
    int main_per_count = 0;
    int counter = 0;
    Timer timer = new Timer();

    //텍스트 버튼 수정
    TextView count_day , count_date , per_count;

    ScheduleDTO row;
    private ArrayList<ExerciseDTO> exerciseDTOS;

    //step3 운동체크 버튼
    FrameLayout step3_exercise_check_bt;

    //나의 운동기록 버튼
    LinearLayout my_exercise_list_bt1 ,my_exercise_list_bt2;
    CustomHandler handler;

    private void reset() {
        this.isSchedul = false;
        this.scheduleDAO = new ScheduleDAO(this);
        this.exerciseDAO = new ExerciseDAO(this);
        this.dayList = new ArrayList<>();
        this.exerciseDTOS = new ArrayList<>();
        this.nDate = null;
        this.mCal = null;
        this.lineCal = null;
        this.startDate = "";
        this.endDate = "";
        this.main_per_count = 0;
        this.counter = 0;
        per_count.setText(String.valueOf(main_per_count));
        gaugeSeekBar.setProgress(main_per_count*0.01f);
        this.handler = new CustomHandler(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        this.idSetting();

        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ( !isRun ) {
                    Toast.makeText(getApplicationContext(),"운동 시작일 설정 버튼을 눌러주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
                try {
                    if ( ! isSchedul ) {
                        nDate = dateFormat.parse((String) tvDate.getText().toString()+"."+ dayList.get(position));
                        lineCal = Calendar.getInstance();
                        lineCal.setTime(nDate);
                        lineCal.add(Calendar.DAY_OF_MONTH , 30);
                        chnageAdapter();
                    } else {
                        //현재 선택한 날짜가 스케줄의 시작 날짜에 포함되어있는지 보기.
                        Date checkDate = dateFormat.parse((String) tvDate.getText().toString()+"."+ dayList.get(position));

                        if (nDate.getTime() <= checkDate.getTime() &&
                                checkDate.getTime() < eDate.getTime() )   {
                            regExerciseCheckDate(checkDate);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Date click Error",Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.gaugeSeekBar.post(new Runnable() {
            @Override
            public void run() {
                innerView.post(new Runnable() {
                    @Override
                    public void run() {
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) innerView.getLayoutParams();
                        params.width = (int) (gaugeSeekBar.getHeight() / 1.4);
                        params.height = (int) (gaugeSeekBar.getHeight() / 1.4);
                        innerView.setLayoutParams(params);
//                        main_start_button.getLayoutParams().width = gaugeSeekBar.getHeight() / 2;
//                        main_start_button.getLayoutParams().height = gaugeSeekBar.getHeight() / 2;
                    }
                });
            }
        });

        Intent intent = getIntent();
        this.isRun = intent.getBooleanExtra("isStart" , false);
        this.isToday = intent.getBooleanExtra("isToday" , false);
        if ( this.isRun) {
            this.topViewChange(this.step2);
            this.popTopActivity = new PopTopActivity(this ,this , getLayoutInflater() , R.id.content_top,"나의 운동일");
        } else {
            this.contentTopActivity = new ContentTopActivity(this ,this , getLayoutInflater() , R.id.content_top,"나의 운동일");
            this.bottomActivity = new BottomActivity(getLayoutInflater() , R.id.bottom , this , this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.schduleArray = new ArrayList<>();
        this.row = null;
        //데이터베이스 생성 전에 오류가 떨어져서 임시적으로 넣어둠.
        if ( scheduleDAO.getID() > 1 ) row = this.scheduleDAO.find();
        if ( row != null ) {
            this.startDate = row.getSdate();
            this.endDate = row.getEdate();
            this.scheduleId = row.getNum();
        }
        this.todayDate();
        this.dataReload();
        if ( this.isToday ) {
            this.regExerciseCheckDate(Common.getDate(this.realDate));
        }
    }

    private void dataReload() {
        //저장된 데이터가 있을 경우.
        if ( this.row != null && ! this.startDate.equals("")) {
            this.isSchedul = true;
            this.isRun = true;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
            try {
                nDate = dateFormat.parse(this.startDate);
                eDate = dateFormat.parse(this.endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            lineCal = Calendar.getInstance();
            lineCal.setTime(nDate);
            lineCal.add(Calendar.DAY_OF_MONTH , 30);
            chnageAdapter();
            this.exerciseDTOS = this.exerciseDAO.finds(this.row.getNum());
            this.topViewChange(this.step3);

            this.count_date.setText(this.row.getSdate()+"~"+this.row.getEdate());
            this.count_day.setText(this.exerciseDTOS.size()+"");
            this.main_per_count = this.exerciseDTOS.size() * 100 / 30;
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    Message msg = handler.obtainMessage();
                    msg.what = CustomHandler.PROGRESS_UPDATE1;
                    handler.sendMessage(msg);
                }
            };
            if ( timer != null ) timer.cancel();
            timer = new Timer();
            timer.schedule(tt, 0, 30);

        } else {
            if ( this.isRun )this.topViewChange(this.step2);
        }
    }

    public void updateProgress() {
        per_count.setText(String.valueOf(counter));
        gaugeSeekBar.setProgress(counter*0.01f);
        if ( counter >= main_per_count ) {
            timer.cancel();
            per_count.setText(String.valueOf(main_per_count));
            gaugeSeekBar.setProgress(main_per_count*0.01f);
            counter = 0;
        }
        counter = counter + 1;
    }

    private void topViewChange(LinearLayout linearLayout) {
        Log.d("viewview","view");
        this.step1.setVisibility(View.GONE);
        this.step2.setVisibility(View.GONE);
        this.step3.setVisibility(View.GONE);

        if ( linearLayout.equals(this.step2) ) {
            //this.todayBt.setVisibility(View.GONE);
            this.calendarNextBt.setVisibility(View.VISIBLE);
        } else {
            //this.todayBt.setVisibility(View.VISIBLE);
            this.calendarNextBt.setVisibility(View.GONE);
        }
        linearLayout.setVisibility(View.VISIBLE);
    }

    private void regExerciseCheckDate(final Date checkDate) {

        Intent intent = new Intent(CalendarActivity.this, PopupActivity.class);
        intent.putExtra("state",0);
        intent.putExtra("date",checkDate.getTime());
        startActivity(intent);
        overridePendingTransition(R.anim.anim_scale, 0);

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
//        for ( ExerciseDTO row : schduleArray ) {
//            try {
//                Date rowDate = dateFormat.parse((String) row.getCheckDate());
//                if ( rowDate.getTime() == checkDate.getTime() ) {
//                    Toast.makeText(getApplicationContext(),"운동체크가 되어있습니다.",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }catch (Exception e) {
//                Toast.makeText(getApplicationContext(),"reg 255 line Error",Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }
//        if (this.isToday) {
//            if ( row != null && Common.getDate(row.getSdate()).getTime() <= checkDate.getTime() && Common.getDate(row.getEdate()).getTime() >= checkDate.getTime() ) {
//                Calendar checkCalendar = Calendar.getInstance();
//                checkCalendar.setTime(checkDate);
//                exerciseDAO.addExercise(new ExerciseDTO(0,scheduleId,checkCalendar.get(Calendar.YEAR)+"."+
//                        (checkCalendar.get(Calendar.MONTH)+1)+"."+checkCalendar.get(Calendar.DATE)));
//                chnageAdapter();
//                dataReload();
//            }
//            this.isToday = false;
//        } else{
//            new MaterialDialog.Builder(CalendarActivity.this).title(R.string.app_name)
//                    .content("오늘 견관절 운동을 하셨나요?")
//                    .positiveText("네").negativeText("아니오").
//                    theme(Theme.LIGHT).onPositive(new MaterialDialog.SingleButtonCallback() {
//                @Override
//                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                    //운동 일자를 저장
//                    Calendar checkCalendar = Calendar.getInstance();
//                    checkCalendar.setTime(checkDate);
//
////                Toast.makeText(getApplicationContext(),checkCalendar.get(Calendar.YEAR)+"."+
////                        (checkCalendar.get(Calendar.MONTH)+1)+"."+checkCalendar.get(Calendar.DATE),Toast.LENGTH_SHORT).show();
//
//                    exerciseDAO.addExercise(new ExerciseDTO(0,scheduleId,checkCalendar.get(Calendar.YEAR)+"."+
//                            (checkCalendar.get(Calendar.MONTH)+1)+"."+checkCalendar.get(Calendar.DATE)));
//
//                    chnageAdapter();
//                    dataReload();
//                }
//            }).show();
//        }


    }


    public void chnageAdapter() {
        if ( this.isSchedul ) this.schduleArray = this.exerciseDAO.finds(this.scheduleId);
        if (this.schduleArray == null) this.schduleArray = new ArrayList<>();
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
        // 오늘 날짜를 세팅 해준다.
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
            case R.id.my_exercise_list2:
            case R.id.my_exercise_list1:
                //나의 운동기록
                intent = new Intent(this , MyExerciseList.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
                break;

            case R.id.step3_exercise_checkBt:

                this.regExerciseCheckDate(Common.getDate(this.realDate));
                break;

            case R.id.calendar_runBt:
                intent = new Intent(this, CalendarActivity.class);
                intent.putExtra("isStart",true);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_bottom_login, 0);
//                this.isRun = true;
//                this.topViewChange(this.step2);
                break;

            case R.id.schadule_nextDateButton:
                this.changeDate(1);
                break;

            case R.id.schadule_backDateButton:
                this.changeDate(-1);
                break;

            case R.id.calendar_nextBt:
                if ( this.nDate == null ) {
                    Toast.makeText(this , "운동 시작일을 선택해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                } else if ( this.isSchedul ) return;

                Calendar cal = Calendar.getInstance();
                cal.setTime(this.nDate);
                ScheduleDTO scheduleDTO = new ScheduleDTO(this.scheduleDAO.getID(),
                        cal.get(Calendar.YEAR)+"."+(cal.get(Calendar.MONTH)+1)+"."+cal.get(Calendar.DATE),
                        "",true);
                cal.add(Calendar.DAY_OF_MONTH , 30);
                scheduleDTO.setEdate(cal.get(Calendar.YEAR)+"."+(cal.get(Calendar.MONTH)+1)+"."+cal.get(Calendar.DATE));
                this.scheduleDAO.addSchedule(scheduleDTO);

                intent = new Intent(this , AlarmDetail.class);
                intent.putExtra("isStart",this.isRun);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_bottom_login, 0);
                if ( this.isRun )finish();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    private void idSetting() {

        this.gridView = findViewById(R.id.scheduleView);
        this.nextButton = findViewById(R.id.schadule_nextDateButton);
        this.nextButton.setColorFilter(Color.parseColor("#666666"));
        this.nextButton.setOnClickListener(this);

        this.backButton = findViewById(R.id.schadule_backDateButton);
        this.backButton.setColorFilter(Color.parseColor("#666666"));
        this.backButton.setOnClickListener(this);

        this.tvDate = findViewById(R.id.schadule_mainDate);
        this.calendarNextBt = findViewById(R.id.calendar_nextBt);
        this.calendarNextBt.setOnClickListener(this);

        this.step1 = findViewById(R.id.c_step1);
        this.step2 = findViewById(R.id.c_step2);
        this.step3 = findViewById(R.id.c_step3);
        this.runBt = findViewById(R.id.calendar_runBt);
        this.runBt.setOnClickListener(this);
        this.todayBt = findViewById(R.id.calendar_today);

        this.count_date = findViewById(R.id.count_date);
        this.count_day = findViewById(R.id.count_day);
        this.per_count = findViewById(R.id.main_per_count);
        this.gaugeSeekBar = findViewById(R.id.progress);
        this.innerView = findViewById(R.id.main_innerView2);
        this.step3_exercise_check_bt = findViewById(R.id.step3_exercise_checkBt);
        this.step3_exercise_check_bt.setOnClickListener(this);
        this.my_exercise_list_bt1 = findViewById(R.id.my_exercise_list1);
        this.my_exercise_list_bt1.setOnClickListener(this);
        this.my_exercise_list_bt2 = findViewById(R.id.my_exercise_list2);
        this.my_exercise_list_bt2.setOnClickListener(this);

        this.reset();
    }




}
