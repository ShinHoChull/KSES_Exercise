package com.m2comm.module.models;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ScheduleDTO extends RealmObject {

    @PrimaryKey
    private int num;
    private String sdate;
    private String edate;
    private boolean isRun;

    public ScheduleDTO(){}

    public ScheduleDTO(int num, String sdate, String edate, boolean isRun) {
        this.num = num;
        this.sdate = sdate;
        this.edate = edate;
        this.isRun = isRun;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    public int getNum() {
        return num;
    }

    public String getSdate() {
        return sdate;
    }

    public String getEdate() {
        return edate;
    }

    public boolean isRun() {
        return isRun;
    }

}
