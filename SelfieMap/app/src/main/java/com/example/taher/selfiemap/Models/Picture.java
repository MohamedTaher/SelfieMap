package com.example.taher.selfiemap.Models;

import android.content.Context;
import android.widget.ImageView;

import com.example.taher.selfiemap.R;
import com.example.taher.selfiemap.Service.GPSTracker;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by taher on 12/09/16.
 */
public class Picture extends RealmObject{

    @PrimaryKey
    private String path;

    private double latitude;
    private double longitude;
    private int favorite;


    public Picture() {
        favorite = 0; // 0 -> mean un-favorite
                      // 1 -> mean favorite
    }

    public Picture(String path, Context context) {
        this.path = path;

        //get long & lat
        GPSTracker gps = new GPSTracker(context);
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        }else{
            gps.showSettingsAlert();
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getPath() {
        return path;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

}
