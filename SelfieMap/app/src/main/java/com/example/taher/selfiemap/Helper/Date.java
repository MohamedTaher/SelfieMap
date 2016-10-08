package com.example.taher.selfiemap.Helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by taher on 12/09/16.
 */
public class Date  {

    public static String getDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(c.getTime());
    }

}
