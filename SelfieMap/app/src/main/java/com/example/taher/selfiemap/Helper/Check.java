package com.example.taher.selfiemap.Helper;

import android.content.ContentResolver;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
 * Created by taher on 12/09/16.
 */
public class Check {

    public static boolean GPS(Context context) {
        ContentResolver cr = context.getContentResolver();
        return Settings.Secure.isLocationProviderEnabled(cr, LocationManager.GPS_PROVIDER);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
