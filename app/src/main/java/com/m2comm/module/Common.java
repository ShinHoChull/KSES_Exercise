package com.m2comm.module;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Common {

    public  static  Calendar getCalendar (Date date) {
        Calendar checkCalendar = Calendar.getInstance() ;
        checkCalendar.setTime(date);
        return checkCalendar;
    }

    public static Date getDate (String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
        try {
            return dateFormat.parse(date);
        } catch (Exception e) {
            return null;
        }
    }


}
