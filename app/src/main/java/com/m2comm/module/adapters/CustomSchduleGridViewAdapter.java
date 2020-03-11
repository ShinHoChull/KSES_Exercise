package com.m2comm.module.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.m2comm.kses_exercise.R;
import com.m2comm.module.models.ScheduleDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomSchduleGridViewAdapter extends BaseAdapter {

    private int x, y, w, h;

    private Context c;
    private LayoutInflater inflater;

    private ArrayList<String> list; //date ArrayList
    private ArrayList<ScheduleDTO> scheduleArray; //Schadule ArrayList

    private Calendar mCal;
    private Calendar realmCal , lineCal;
    private Date date , lineDate;

    public CustomSchduleGridViewAdapter(Context c, LayoutInflater inflater, Calendar mCal, Date date, ArrayList<String> list , ArrayList<ScheduleDTO> scheduleDTO , Calendar lineCal) {
        this.c = c;
        this.inflater = inflater;
        this.mCal = mCal;
        this.realmCal = mCal;
        this.list = list;
        this.date = date;
        this.scheduleArray = scheduleDTO;
        this.lineCal = lineCal;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.schdule_grid_item, parent, false);
            holder = new ViewHolder();
            holder.tvItemGridView = (TextView) convertView.findViewById(R.id.tv_item_gridview);
            holder.cirV= (LinearLayout) convertView.findViewById(R.id.schedule_startView);
            holder.choice_start_view = convertView.findViewById(R.id.choice_start_day);
            holder.line = convertView.findViewById(R.id.schedule_choice_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvItemGridView.setText("" + getItem(position));

        //해당 날짜 텍스트 컬러,배경 변경
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd",Locale.KOREA);
        String dayString = getItem(position);
        if (!dayString.equals("")) {
            dayString = String.valueOf(mCal.get(Calendar.YEAR)) + "." + this.zeroPoint(String.valueOf(mCal.get(Calendar.MONTH) + 1)) + "." + this.zeroPoint(dayString);
        } else {
            return convertView;
        }

        Date nDate = null;
        try {
            nDate = dateFormat.parse(dayString);

            //new Calendar
            Calendar newCal = Calendar.getInstance();
            //오늘 날짜 표시해야 해서 새로운 인스턴스를 생성해준다..
            Integer today = newCal.get(Calendar.DAY_OF_MONTH);
            String sToday = String.valueOf(today);

            //이번주 구하기
            Integer nowWeekState = newCal.get(Calendar.WEEK_OF_MONTH);
            Integer oldWeekState = realmCal.get(Calendar.WEEK_OF_MONTH);
            if ( newCal.get(Calendar.YEAR) == realmCal.get(Calendar.YEAR) && newCal.get(Calendar.MONTH) == realmCal.get(Calendar.MONTH) && nowWeekState == oldWeekState) {
                //같은 주차면 뷰의 백그라운드 색상을 변경
               // convertView.setBackgroundColor(Color.rgb(244,244,244));
            }

            this.realmCal.setTime(nDate);
            switch (realmCal.get(Calendar.DAY_OF_WEEK)) {
                case 1:
                    holder.tvItemGridView.setTextColor(this.c.getResources().getColor(R.color.schadule_sun_color));
                    break;
                case 7:
                    holder.tvItemGridView.setTextColor(this.c.getResources().getColor(R.color.schadule_sat_color));
                    break;
                default:
                    holder.tvItemGridView.setTextColor(this.c.getResources().getColor(R.color.main_color_black));
            }

            if (newCal.get(Calendar.YEAR) == realmCal.get(Calendar.YEAR) && newCal.get(Calendar.MONTH) ==
                    realmCal.get(Calendar.MONTH) && sToday.equals(getItem(position))) {
                //오늘 day 텍스트 컬러 변경
                //convertView.setBackgroundColor(Color.rgb(221, 221, 221));
                //holder.tvItemGridView.setTextColor(this.c.getResources().getColor(R.color.main_color_skyblue));
            }

            if ( this.date != null ) {
                Calendar click_calendar = Calendar.getInstance();
                click_calendar.setTime(this.date);

                Integer startDay = realmCal.get(Calendar.DAY_OF_MONTH);
                String startToday = String.valueOf(startDay);

                //Start 날짜 구하기.
                if ( realmCal.get(Calendar.YEAR) == click_calendar.get(Calendar.YEAR) &&
                        realmCal.get(Calendar.MONTH) == click_calendar.get(Calendar.MONTH) &&
                        startToday.equals(String.valueOf(click_calendar.get(Calendar.DATE)))
                ) {
                    holder.line.setVisibility(View.VISIBLE);
                    holder.choice_start_view.setVisibility(View.VISIBLE);
                    holder.tvItemGridView.setBackgroundColor(Color.parseColor("#ff3a7f"));
                    holder.tvItemGridView.setTextColor(Color.parseColor("#ffffff"));
                    //click_calendar.add(Calendar.DAY_OF_MONTH , 30);
                    //this.lineCal = click_calendar;
                }

                if ( this.lineCal != null ) {
                    int lineDay = realmCal.get(Calendar.DAY_OF_MONTH);

                    Log.d("year1",lineCal.get(Calendar.YEAR)+"");
                    Log.d("year2",realmCal.get(Calendar.YEAR)+"");
                    Log.d("MONTH1",lineCal.get(Calendar.MONTH)+"");
                    Log.d("MONTH2",realmCal.get(Calendar.MONTH)+"");
                    Log.d("DATE1",lineCal.get(Calendar.DATE)+"");
                    Log.d("DATE2",realmCal.get(Calendar.DATE)+"");
                    Log.d("DATE",lineCal.compareTo(realmCal)+"");
                    Log.d("DATE23",click_calendar.compareTo(realmCal)+"");

                    if ( click_calendar.compareTo(realmCal) == -1  && lineCal.compareTo(realmCal) == 1 ) {
                        holder.line.setVisibility(View.VISIBLE);
                    }
                }
            }

            if ( ! dayString.equals("") && this.scheduleArray != null) {
                for ( ScheduleDTO scheduleDTO : scheduleArray ) {

                    int pointDay = Integer.parseInt(getItem(position));
                    String[] sDayArr = scheduleDTO.getSdate().split("-");
                    int sDay = Integer.parseInt(sDayArr[sDayArr.length - 1 ]);

                    Log.d("sDay_pointDay",sDay+"/"+pointDay);

                    if ( ! scheduleDTO.getEdate().equals("") ) {
                        String[] eDayArr = scheduleDTO.getEdate().split("-");
                        int eDay = Integer.parseInt(eDayArr[eDayArr.length - 1 ]);
                        if ( sDay <= pointDay && eDay >= pointDay ) {
                            holder.tvItemGridView.setTextColor(Color.WHITE);
                            holder.cirV.setVisibility(View.VISIBLE);
                        }

                    } else {
                        if ( pointDay == sDay ) {
                            holder.tvItemGridView.setTextColor(Color.WHITE);
                            holder.cirV.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return convertView;

    }

    private class ViewHolder {

        TextView tvItemGridView;
        TextView choice_start_view;
        LinearLayout cirV;
        LinearLayout line;


    }

    public String zeroPoint(String data) {
        data = data.trim();
        if (data.length() == 1) {
            data = "0" + data;
        }
        return data;
    }

}
