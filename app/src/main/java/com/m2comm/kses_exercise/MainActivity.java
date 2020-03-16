package com.m2comm.kses_exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.transition.CircularPropagation;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.m2comm.module.Common;
import com.m2comm.module.Custom_SharedPreferences;
import com.m2comm.module.dao.AlarmDAO;
import com.m2comm.module.dao.ExerciseDAO;
import com.m2comm.module.dao.ScheduleDAO;
import com.m2comm.module.models.ExerciseDTO;
import com.m2comm.module.models.ScheduleDTO;
import com.tenclouds.gaugeseekbar.GaugeSeekBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView bt1,bt2,bt3,bt4,cehckImg;
    GaugeSeekBar gaugeSeekBar;

    BottomActivity bottomActivity;
    Custom_SharedPreferences csp;

    private Date nDate;


    //프로그레스바 원형 뷰 ( 2는 현재 운동이 있을경우 )
    LinearLayout main_start_button , main_start_button2;
    TextView main_per_num , main_count_day;

    //상단 운동 체크 & 운동시작 버튼
    private LinearLayout exercise_start_bt,exercise_check_bt;

    //상단 운동 기록 텍스트
    private LinearLayout main_exercise_base_text , main_exercise_detail_text;
    private TextView main_exercise_detail_date ,main_exercise_detail_count_day;

    private AlarmDAO alarmDAO;
    private ScheduleDAO scheduleDAO;
    private ExerciseDAO exerciseDAO;
    private ArrayList<ExerciseDTO> exerciseDTOS;
    private ScheduleDTO row;

    int main_per_count = 0;
    int counter = 0;
    Timer timer = new Timer();

    private void idSetting () {
        this.bt1 = findViewById(R.id.main_bt1);
        this.bt1.setOnClickListener(this);
        this.bt2 = findViewById(R.id.main_bt2);
        this.bt2.setOnClickListener(this);
        this.bt3 = findViewById(R.id.main_bt3);
        this.bt3.setOnClickListener(this);
        this.bt4 = findViewById(R.id.main_bt4);
        this.bt4.setOnClickListener(this);
        this.cehckImg = findViewById(R.id.check_bt);
        this.cehckImg.setColorFilter(Color.parseColor("#d71447"));

        this.exercise_check_bt = findViewById(R.id.main_exercise_check_bt);
        this.exercise_start_bt = findViewById(R.id.main_exercise_start_bt);
        this.exercise_start_bt.setOnClickListener(this);
        this.exercise_check_bt.setOnClickListener(this);

        this.main_exercise_base_text = findViewById(R.id.main_exercise_base_text);
        this.main_exercise_detail_text = findViewById(R.id.main_exercise_detail_text);
        this.main_exercise_detail_date = findViewById(R.id.main_exercise_detail_date);
        this.main_exercise_detail_count_day = findViewById(R.id.main_exercise_detail_CountDay);

        this.gaugeSeekBar = findViewById(R.id.progress);
        this.main_start_button = findViewById(R.id.main_innerView1);
        this.main_start_button.setOnClickListener(this);
        this.main_start_button2 = findViewById(R.id.main_innerView2);
        this.main_per_num = findViewById(R.id.main_per_count);
        this.main_count_day = findViewById(R.id.main_count_day);

        this.csp = new Custom_SharedPreferences(this);
        this.scheduleDAO = new ScheduleDAO(this);
        this.exerciseDAO = new ExerciseDAO(this);
        this.alarmDAO = new AlarmDAO(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.idSetting();
        this.createNotificationChannel();

        this.bt1.setImageBitmap(this.getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.main_bt3)));
        this.bt2.setImageBitmap(this.getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.main_bt2)));
        this.bt3.setImageBitmap(this.getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.main_bt1)));
        this.bt4.setImageBitmap(this.getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.main_bt4)));
        this.bottomActivity = new BottomActivity(getLayoutInflater() , R.id.bottom , this , this);

        this.gaugeSeekBar.post(new Runnable() {
            @Override
            public void run() {
                Log.d("heightt",gaugeSeekBar.getHeight()+"");
                //gaugeSeekBar.setProgressWidth(gaugeSeekBar.getHeight());
                main_start_button.post(new Runnable() {
                    @Override
                    public void run() {
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) main_start_button.getLayoutParams();
                        params.width = (int) (gaugeSeekBar.getHeight() / 1.4);
                        params.height = (int) (gaugeSeekBar.getHeight() / 1.4);
                        main_start_button.setLayoutParams(params);
//                        main_start_button.getLayoutParams().width = gaugeSeekBar.getHeight() / 2;
//                        main_start_button.getLayoutParams().height = gaugeSeekBar.getHeight() / 2;
                    }
                });
                main_start_button2.post(new Runnable() {
                    @Override
                    public void run() {
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) main_start_button.getLayoutParams();
                        params.width = (int) (gaugeSeekBar.getHeight() / 1.4);
                        params.height = (int) (gaugeSeekBar.getHeight() / 1.4);
                        main_start_button2.setLayoutParams(params);
                    }
                });
            }
        });

        //Menu List 한번만 불러와서 디바이스에 저장한다.
        try {
            if ( csp.getValue("menu","").equals("") ) {
                this.csp.put("menu",getMenu());
            }
        } catch (Exception e) {
            Toast.makeText(this , "Menu Save Fail",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.row = null;
        //데이터베이스 생성 전에 오류가 떨어져서 임시적으로 넣어둠.
        if ( scheduleDAO.getID() > 1 ) row = this.scheduleDAO.find();
        if ( row != null ) {
            long now = System.currentTimeMillis();
            this.nDate = new Date(now);
            Date eDate = Common.getDate(row.getEdate());
            if ( nDate.getTime() > eDate.getTime() ) {
                //현재 날짜가 등록된 스케줄 끝나는 날짜보다 크면 완료 스테이트 변경
                this.scheduleDAO.updateSchedule(row.getNum());
                this.alarmDAO.delete(row.getNum());
            } else {
                //아니면 현재 운동 진행상황 표시!
                scheduleCheck();
            }
        }
    }

    private void scheduleCheck() {
        this.main_start_button.setVisibility(View.GONE);
        this.main_exercise_base_text.setVisibility(View.GONE);
        this.exercise_start_bt.setVisibility(View.GONE);

        this.main_exercise_detail_text.setVisibility(View.VISIBLE);
        this.main_start_button2.setVisibility(View.VISIBLE);
        this.exercise_check_bt.setVisibility(View.VISIBLE);
        this.main_exercise_detail_date.setText(this.row.getSdate()+"~"+this.row.getEdate());
        this.exerciseDTOS = this.exerciseDAO.finds(this.row.getNum());
        this.main_exercise_detail_count_day.setText(String.valueOf(this.exerciseDTOS.size()));
        this.main_count_day.setText(this.exerciseDTOS.size()+"/30일");
        main_per_count = this.exerciseDTOS.size() * 100 / 30;
        //this.main_per_num.setText(String.valueOf(main_per_count));

        //this.gaugeSeekBar.setProgress(main_per_count*0.01f);

        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                Log.d("main_per_count",""+main_per_count);
                main_per_num.setText(String.valueOf(counter));
                gaugeSeekBar.setProgress(counter*0.01f);
                if ( counter >= main_per_count ) {
                    timer.cancel();
                    gaugeSeekBar.setProgress(main_per_count*0.01f);
                    counter = 0;
                }
                counter = counter + 1;

            }
        };
        timer = new Timer();
        timer.schedule(tt, 0, 50);

    }



    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.main_bt1:
                intent = new Intent(getApplicationContext() , ContentListActivity.class);
                intent.putExtra("groupId",0);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;
            case R.id.main_bt2:
                intent = new Intent(getApplicationContext() , ContentListActivity.class);
                intent.putExtra("groupId",1);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;
            case R.id.main_bt3:
                intent = new Intent(getApplicationContext() , ContentListActivity.class);
                intent.putExtra("groupId",2);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;
            case R.id.main_bt4:
                intent = new Intent(getApplicationContext() , ContentListActivity.class);
                intent.putExtra("groupId",3);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;

            case R.id.main_innerView1:
            case R.id.main_exercise_check_bt:
            case R.id.main_exercise_start_bt:
                intent = new Intent(this , CalendarActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;
        }
    }

    private String getMenu() throws IOException {
        InputStream is = getResources().openRawResource(R.raw.menu);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e){
            Toast.makeText(this , "Menu Paser Error2",Toast.LENGTH_SHORT).show();
        } finally
        {
            is.close();
        }
        return writer.toString();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(getResources().getString(R.string.app_name), getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(getResources().getString(R.string.app_name));
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


    public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 50; // 테두리 곡률 설정

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }



}
