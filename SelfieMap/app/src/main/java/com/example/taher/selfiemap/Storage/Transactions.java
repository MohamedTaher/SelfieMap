package com.example.taher.selfiemap.Storage;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.example.taher.selfiemap.Models.Picture;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by taher on 18/09/16.
 */
public class Transactions {
    private static Transactions instance;
    private final Realm realm;

    public Transactions(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static Transactions with(Fragment fragment) {

        if (instance == null) {
            instance = new Transactions(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static Transactions with(Activity activity) {

        if (instance == null) {
            instance = new Transactions(activity.getApplication());
        }
        return instance;
    }

    public static Transactions with(Application application) {

        if (instance == null) {
            instance = new Transactions(application);
        }
        return instance;
    }

    public static Transactions getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from Picture.class
    public void clearAll() {

        realm.beginTransaction();
        realm.clear(Picture.class);
        realm.commitTransaction();
    }

    //find all objects in the Picture.class
    public static RealmResults<Picture> getPictures() {
        Realm realm = instance.getRealm();
        return realm.where(Picture.class).findAll();
    }

    //query a single item with the given id
    public static Picture getPicture(String path) {
        Realm realm = instance.getRealm();
        return realm.where(Picture.class).equalTo(DBConstants.PicturePK, path).findFirst();
    }

    //check if Picture.class is empty
    public boolean hasPictures() {

        return !realm.allObjects(Picture.class).isEmpty();
    }

    //clear object from Picture.class by path PK
    public static void clearPic (String path) {
        Realm realm = instance.getRealm();
        final RealmResults<Picture> results = realm.where(Picture.class).equalTo(DBConstants.PicturePK, path).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.clear();
            }
        });
    }

    public static void insert(Picture picture){

        Realm realm = instance.getRealm();

        realm.beginTransaction();
        Picture realmPic = realm.createObject(Picture.class);
        realmPic.setPath(picture.getPath());
        realmPic.setLatitude(picture.getLatitude());
        realmPic.setLongitude(picture.getLongitude());
        realm.commitTransaction();
    }
    
    public static void favorite (Picture picture){
        Realm realm = instance.getRealm();
        realm.beginTransaction();
        if (picture.getFavorite() == 0)
            picture.setFavorite(1);
        else
            picture.setFavorite(0);
        realm.commitTransaction();
    }


}
