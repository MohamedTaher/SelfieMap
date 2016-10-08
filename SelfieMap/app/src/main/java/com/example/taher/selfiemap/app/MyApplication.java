package com.example.taher.selfiemap.app;

import android.app.Application;

import com.example.taher.selfiemap.Business.PictureManager;
import com.example.taher.selfiemap.Storage.Transactions;

import io.realm.Realm;
import io.realm.RealmConfiguration;
/**
 * Created by taher on 18/09/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        //to set static object
        Transactions.with(this);

        //to load pictures from DB
        PictureManager.setPictures();
    }
}