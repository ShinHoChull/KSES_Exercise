package com.m2comm.module.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.m2comm.kses_exercise.MyListActivity;
import com.m2comm.module.models.AlarmDTO;
import com.m2comm.module.models.ExerciseDTO;
import com.m2comm.module.models.FavDTO;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class FavDAO implements Realm.Transaction {
    Realm realm;
    Context context;

    public FavDAO(Context context) {
        this.context = context;
        this.realm = Realm.getDefaultInstance();
    }

    public int getID () {
        Number currentIdNum = realm.where(FavDTO.class).max("num");
        Log.d("currentNum",currentIdNum+"");
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }

    public boolean addFav(final FavDTO favDTO) {

        this.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                favDTO.setNum(getID());
                realm.insertOrUpdate(favDTO);
            }
        });
        return true;
    }

    public void delete (final int favNum) {
        this.realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                FavDTO result = realm.where(FavDTO.class).equalTo("num",favNum).findFirst();
                result.deleteFromRealm();
            }
        }, new OnSuccess() {
            @Override
            public void onSuccess() {
                ((MyListActivity)context).adapterChange();
            }
        });

    }

    public ArrayList<FavDTO> getAllList() {
        return new ArrayList<>(this.realm.where(FavDTO.class).findAll());
    }


    @Override
    public void execute(Realm realm) {

    }


}
