package com.m2comm.module.dao;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.m2comm.kses_exercise.ContentListActivity;
import com.m2comm.kses_exercise.MyListActivity;
import com.m2comm.module.CustomHandler;
import com.m2comm.module.models.AlarmDTO;
import com.m2comm.module.models.ExerciseDTO;
import com.m2comm.module.models.FavDTO;
import com.m2comm.module.models.ScheduleDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import io.realm.Realm;
import io.realm.RealmCollection;
import io.realm.RealmResults;

public class FavDAO implements Realm.Transaction {
    Realm realm;
    Context context;

    public FavDAO(Context context) {
        this.context = context;
        this.realm = Realm.getDefaultInstance();
    }

    public int getID (Realm realm) {
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

    public boolean addFav(final FavDTO favDTO ) {

        this.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                favDTO.setNum(getID(realm));
                realm.insertOrUpdate(favDTO);
            }
        });
        return true;
    }

    public FavDTO find(int num) {
        FavDTO favDTO = this.realm.where(FavDTO.class).equalTo("num", true).findFirst();
        return favDTO;
    }

    public FavDTO find(int groupNum , int depth2Num , int depth3Num) {
        FavDTO favDTO = this.realm.where(FavDTO.class).equalTo("groupNum", groupNum).
                equalTo("depth2Num",depth2Num).
                equalTo("depth3Num",depth3Num).findFirst();
        return favDTO;
    }

    public void deleteAll(final int groupNum) {
        this.realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(FavDTO.class).equalTo("groupNum",groupNum).findAll().deleteAllFromRealm();
            }
        }, new OnSuccess() {
            @Override
            public void onSuccess() {
                CustomHandler handler = new CustomHandler(context);
                Message msg = handler.obtainMessage();
                msg.what = CustomHandler.CONTENTLIST_FAV;
                handler.sendMessage(msg);

            }
        }, new OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d("deleteError",error.getMessage());
            }
        });
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
    public ArrayList<FavDTO> findGroup(int groupNum) {
        return new ArrayList<>(this.realm.where(FavDTO.class).equalTo("groupNum", groupNum).findAll());
    }

    public ArrayList<FavDTO> getAllList() {
        return new ArrayList<>(this.realm.where(FavDTO.class).findAll());
    }


    @Override
    public void execute(Realm realm) {

    }


}
