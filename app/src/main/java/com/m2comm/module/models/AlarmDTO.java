package com.m2comm.module.models;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AlarmDTO extends RealmObject {

    @PrimaryKey
    private int num;
    private String am_pm;
    private String Date;
    private boolean isPush;
    private byte[] week;
    private int pushState; //0 = Schedule ID

    public AlarmDTO(){}

    public AlarmDTO(int num, String am_pm, String date, boolean isPush, byte[] week, int pushState) {
        this.num = num;
        this.am_pm = am_pm;
        Date = date;
        this.isPush = isPush;
        this.week = week;
        this.pushState = pushState;
    }

    public int getNum() {
        return num;
    }

    public String getAm_pm() {
        return am_pm;
    }

    public String getDate() {
        return Date;
    }

    public boolean isPush() {
        return isPush;
    }

    public byte[] getWeek() {
        return week;
    }

    public int getPushState() {
        return pushState;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setAm_pm(String am_pm) {
        this.am_pm = am_pm;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setPush(boolean push) {
        isPush = push;
    }

    public void setWeek(byte[] week) {
        this.week = week;
    }

    public void setPushState(int pushState) {
        this.pushState = pushState;
    }
}

