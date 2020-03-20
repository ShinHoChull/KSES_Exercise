package com.m2comm.module.dao;

import android.content.Context;
import android.util.Log;

import com.m2comm.kses_exercise.MyExerciseList;
import com.m2comm.module.models.AlarmDTO;
import com.m2comm.module.models.ExerciseDTO;
import com.m2comm.module.models.ScheduleDTO;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class ExerciseDAO implements Realm.Transaction {
    Realm realm;
    Context context;

    public ExerciseDAO(Context context) {
        this.context = context;
        this.realm = Realm.getDefaultInstance();
    }

    public int getID () {
        Number currentIdNum = realm.where(ExerciseDTO.class).max("num");
        Log.d("currentNum",currentIdNum+"");
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }

    public boolean addExercise(final ExerciseDTO exerciseDTO) {

        this.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                exerciseDTO.setNum(getID());
                realm.insertOrUpdate(exerciseDTO);
            }
        });

        return true;
    }

    public ArrayList<ExerciseDTO> finds(int scheduleNum) {
        ArrayList<ExerciseDTO> exerciseDTOS = new ArrayList<>();
        exerciseDTOS.addAll(this.realm.copyFromRealm(this.realm.where(ExerciseDTO.class).equalTo("scheduleNum",scheduleNum).findAll()));
        return exerciseDTOS;
    }

    public void delete (final int scheduleNum) {
        this.realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<ExerciseDTO> results = realm.where(ExerciseDTO.class).equalTo("scheduleNum",scheduleNum).findAll();
                results.deleteAllFromRealm();
            }
        }, new OnSuccess() {
            @Override
            public void onSuccess() {

            }
        });

    }

    public List<ExerciseDTO> getAllList() {
        ArrayList<ExerciseDTO> exerciseDTOS = new ArrayList<>();
        exerciseDTOS.addAll(this.realm.copyFromRealm(this.realm.where(ExerciseDTO.class).findAll()));
        return exerciseDTOS;
    }


    @Override
    public void execute(Realm realm) {

    }


}
