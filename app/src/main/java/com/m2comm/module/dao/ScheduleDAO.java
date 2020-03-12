package com.m2comm.module.dao;

import android.content.Context;
import android.util.Log;

import com.m2comm.module.models.ExerciseDTO;
import com.m2comm.module.models.ScheduleDTO;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class ScheduleDAO implements Realm.Transaction {
    Realm realm;
    Context context;

    public ScheduleDAO(Context context) {
        this.context = context;
        this.realm = Realm.getDefaultInstance();
    }

    public int getID () {
        Number currentIdNum = realm.where(ScheduleDTO.class).max("num");
        Log.d("currentNum",currentIdNum+"");
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }

    public ScheduleDTO find() {
        return this.realm.copyFromRealm(this.realm.where(ScheduleDTO.class).equalTo("isRun", true).findFirst());
    }

    public boolean addSchedule(final ScheduleDTO scheduleDTO) {

        this.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                scheduleDTO.setNum(getID());
                realm.insertOrUpdate(scheduleDTO);
            }
        });

        return true;
    }

    public boolean updateSchedule (int scheduleid) {

        return true;
    }

    public List<ScheduleDTO> getAllList() {
        ArrayList<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        scheduleDTOS.addAll(this.realm.copyFromRealm(this.realm.where(ScheduleDTO.class).findAll()));
        return scheduleDTOS;
    }


    @Override
    public void execute(Realm realm) {

    }


}