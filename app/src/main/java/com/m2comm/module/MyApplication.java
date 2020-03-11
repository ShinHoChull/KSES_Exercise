package com.m2comm.module;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("alarm.realm")
                .schemaVersion(1)
                .build();

        Realm.setDefaultConfiguration(config);

        //참고 URL
        //https://black-jin0427.tistory.com/98
    }
}
