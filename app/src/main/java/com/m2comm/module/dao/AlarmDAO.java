package com.m2comm.module.dao;

import android.content.Context;
import android.util.Log;

import com.m2comm.module.models.AlarmDTO;
import com.m2comm.module.models.ExerciseDTO;
import com.m2comm.module.models.ScheduleDTO;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class AlarmDAO implements Realm.Transaction {
    Realm realm;
    Context context;

    public AlarmDAO(Context context) {
        this.context = context;
        this.realm = Realm.getDefaultInstance();
    }

    public int getID () {
        Number currentIdNum = realm.where(AlarmDTO.class).max("num");
        Log.d("alarm_currentNum",currentIdNum+"");
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

    public void delete (final int scheduleNum) {
        this.realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<AlarmDTO> results = realm.where(AlarmDTO.class).equalTo("pushState",scheduleNum).findAll();
                results.deleteAllFromRealm();
            }
        }, new OnSuccess() {
            @Override
            public void onSuccess() {

            }
        });

    }

    public boolean updateAlarm (final int id , final boolean state) {
        this.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AlarmDTO scheduleDTO = realm.where(AlarmDTO.class).equalTo("num",id).findFirst();
                scheduleDTO.setPush(state);
            }
        });
        return true;
    }

    public List<AlarmDTO> getAllList() {
        ArrayList<AlarmDTO> alarmDTOS = new ArrayList<>();
        alarmDTOS.addAll(this.realm.copyFromRealm(this.realm.where(AlarmDTO.class).findAll()));
        return alarmDTOS;
    }

    public AlarmDTO find(int id) {
        return this.realm.where(AlarmDTO.class).equalTo("num", id).findFirst();
    }

    @Override
    public void execute(Realm realm) {

    }


}
