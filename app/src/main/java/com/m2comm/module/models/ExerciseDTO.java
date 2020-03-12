package com.m2comm.module.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ExerciseDTO extends RealmObject {

    @PrimaryKey
    private int num;
    private int scheduleNum;
    private String checkDate;

    public ExerciseDTO(){}

    public ExerciseDTO(int num, int scheduleNum, String checkDate) {
        this.num = num;
        this.scheduleNum = scheduleNum;
        this.checkDate = checkDate;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setScheduleNum(int scheduleNum) {
        this.scheduleNum = scheduleNum;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public int getNum() {
        return num;
    }

    public int getScheduleNum() {
        return scheduleNum;
    }

    public String getCheckDate() {
        return checkDate;
    }
}
