package com.m2comm.module.dao;

import android.content.Context;
import android.util.Log;

import com.m2comm.module.models.AlarmDTO;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class AlarmDAO implements Realm.Transaction {
    Realm realm;
    Context context;

    public AlarmDAO(Context context) {
        this.context = context;
        this.realm = Realm.getDefaultInstance();
    }

    public int getID () {
        Number currentIdNum = realm.where(AlarmDTO.class).max("num");
        Log.d("currentNum",currentIdNum+"");
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }

    public boolean addAlarm(final AlarmDTO alarmDTO) {

        this.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                alarmDTO.setNum(getID());
                realm.insertOrUpdate(alarmDTO);
            }
        });

        return true;
    }

    public List<AlarmDTO> getAllList() {
        ArrayList<AlarmDTO> alarmDTOS = new ArrayList<>();
        alarmDTOS.addAll(this.realm.copyFromRealm(this.realm.where(AlarmDTO.class).findAll()));
        return alarmDTOS;
    }


    @Override
    public void execute(Realm realm) {

    }


}
