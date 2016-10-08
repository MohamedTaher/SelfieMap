package com.example.taher.selfiemap.UI.IconsBar;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.taher.selfiemap.Business.Constants;
import com.example.taher.selfiemap.Business.MapManager;
import com.example.taher.selfiemap.Business.PictureManager;
import com.example.taher.selfiemap.Helper.Check;
import com.example.taher.selfiemap.Models.Picture;
import com.example.taher.selfiemap.R;
import com.example.taher.selfiemap.UI.Activity.ImagesActivity;
import com.example.taher.selfiemap.UI.Activity.MapsActivity;
import com.example.taher.selfiemap.UI.Activity.SearchActivity;

/**
 * Created by taher on 19/09/16.
 */
public class IconsBar {

    public static void images (Activity activity) {
        activity.startActivity(new Intent(activity.getApplicationContext(), ImagesActivity.class));
    }

    public static void map (Activity activity) {
        activity.startActivity(new Intent(activity.getApplicationContext(),MapsActivity.class));
    }

    public static void map (Activity activity, double lat, double lng) {
        Intent intent = new Intent(activity, MapsActivity.class);
        intent.putExtra(activity.getString(R.string.lat), lat);
        intent.putExtra(activity.getString(R.string.lng), lng);
        activity.startActivity(intent);
    }

    public static void favorite (Activity activity){
        Intent intent = new Intent(activity, ImagesActivity.class);
        intent.putExtra(activity.getString(R.string.favoriteShow), true);
        activity.startActivity(intent);
    }

    public static Uri camera (Activity activity) {
        final Activity _activity = activity;

        Uri imageUri = null;
        if (!Check.GPS(_activity.getApplicationContext()))
            MapManager.showSettingsAlert(_activity);
        else
            imageUri = PictureManager.takePicture(_activity, Constants.TAKE_PICTURE);

        return imageUri;
    }

    public static void search(Activity activity) {
        activity.startActivity(new Intent(activity.getApplicationContext(), SearchActivity.class));
    }

}
