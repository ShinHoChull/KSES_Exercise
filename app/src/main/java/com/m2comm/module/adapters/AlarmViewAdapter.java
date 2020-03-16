package com.m2comm.module.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.m2comm.kses_exercise.R;
import com.m2comm.module.dao.AlarmDAO;
import com.m2comm.module.models.AlarmDTO;
import com.m2comm.module.models.ContentDTO;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class AlarmViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    List<AlarmDTO> contentArray;
    AlarmDAO alarmDAO;


    public AlarmViewAdapter(Context context, LayoutInflater layoutInflater , List<AlarmDTO> contentArray) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.contentArray = contentArray;
        this.alarmDAO = new AlarmDAO(context);
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
            final AlarmDTO row = contentArray.get(position);
            convertView  = this.layoutInflater.inflate(R.layout.alarm_list_item,parent,false);
            TextView am_pm = convertView.findViewById(R.id.am_pm);
            TextView time = convertView.findViewById(R.id.time);
            TextView sun = convertView.findViewById(R.id.alarm_sun);
            TextView mon = convertView.findViewById(R.id.alarm_mon);
            TextView tue = convertView.findViewById(R.id.alarm_tue);
            TextView wed = convertView.findViewById(R.id.alarm_wed);
            TextView thu = convertView.findViewById(R.id.alarm_thu);
            TextView fri = convertView.findViewById(R.id.alarm_fri);
            TextView sat = convertView.findViewById(R.id.alarm_sat);
            final ImageView img = convertView.findViewById(R.id.alarm_img);

            am_pm.setText(row.getAm_pm());
            time.setText(row.getDate());
            byte[] week = row.getWeek();
            sun.setTextColor( week[1] == 1 ? Color.parseColor("#253fac") :  Color.parseColor("#88929c"));
            mon.setTextColor( week[2] == 1 ? Color.parseColor("#253fac") :  Color.parseColor("#88929c"));
            tue.setTextColor( week[3] == 1 ? Color.parseColor("#253fac") :  Color.parseColor("#88929c"));
            wed.setTextColor( week[4] == 1 ? Color.parseColor("#253fac") :  Color.parseColor("#88929c"));
            thu.setTextColor( week[5] == 1 ? Color.parseColor("#253fac") :  Color.parseColor("#88929c"));
            fri.setTextColor( week[6] == 1 ? Color.parseColor("#253fac") :  Color.parseColor("#88929c"));
            sat.setTextColor( week[7] == 1 ? Color.parseColor("#253fac") :  Color.parseColor("#88929c"));
            if ( row.isPush() ) {
                img.setImageResource(R.drawable.alarm_on);
            } else {
                img.setImageResource(R.drawable.alarm_off);
            }
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( row.isPush() ) {
                        alarmDAO.updateAlarm(row.getNum(),false);
                        row.setPush(false);
                        img.setImageResource(R.drawable.alarm_off);
                    } else {
                        alarmDAO.updateAlarm(row.getNum(),true);
                        row.setPush(true);
                        img.setImageResource(R.drawable.alarm_on);
                    }
                }
            });

        }

        return convertView;
    }



}
