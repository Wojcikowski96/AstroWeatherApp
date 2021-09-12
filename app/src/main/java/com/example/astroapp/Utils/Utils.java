package com.example.astroapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.example.astroapp.MainActivity;

import java.io.File;

public class Utils {
    public static String formatTime(int hour, int minute){
        String stringHour, stringMinute, time;

        if (hour<10) { stringHour="0"+hour;
        } else {
            stringHour= String.valueOf(hour);
        }
        if (minute<10) {
            stringMinute="0"+minute;
        } else {
            stringMinute= String.valueOf(minute);
        }
        time = stringHour +":"+stringMinute;

        return time;
    }
    public static String formatDate(int y, int m, int d){
        String date;
        String day, month;
        if (d<10) {
            day="0"+d;
        } else {day= String.valueOf(d);
        }
        if (m<10)
        {
            month="0"+m;
        } else {
            month= String.valueOf(m);
        }
        date = day+"."+month+"."+y;
        return date;
    }
    public static Bundle putDataToBundle(String[] data, String type){
        Bundle bundle = new Bundle();
        if (type.equals("sun")){
            bundle.putString("sunrisetime",data[0]);
            bundle.putString("sunriseazimuth", data[1]);
            bundle.putString("sunsettime",data[2]);
            bundle.putString("sunsetazimuth", data[3]);
            bundle.putString("twilightmorning", data[4]);
            bundle.putString("twilightevening", data[5]);

        }else if(type.equals("moon")){
            bundle.putString("moonrisetime",data[0]);
            bundle.putString("moonsettime",data[1]);
            bundle.putString("nextnewmoon",data[2]);
            bundle.putString("nextfullmoon",data[3]);
            bundle.putString("moonphase", data[4]);
            bundle.putString("synodicmonth", data[5]);
        }
        return bundle;
    }

    public static boolean checkForInputErrors(String[] coords){
        boolean numeric=true;
        for(int i=0;i<coords.length;i++){

            if(i!=2 && i!=5){
                try{
                    int num = Integer.parseInt(coords[i]);
                }catch(NumberFormatException e){
                    numeric=false;
                    break;
                }
            }
        }
        return numeric;
    }

    public static boolean checkForInputRange(String[] coords){
        boolean correct=true;

        if(Integer.parseInt(coords[0]) < 0 || Integer.parseInt(coords[0]) > 90 || Integer.parseInt(coords[1]) < 0 || Integer.parseInt(coords[1]) > 60
        || Integer.parseInt(coords[3]) < 0 || Integer.parseInt(coords[3]) > 180 || Integer.parseInt(coords[4]) < 0 || Integer.parseInt(coords[4]) > 60){

            correct=false;
        }
        return correct;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean checkIfFileExists(Activity activity){
        boolean exists=false;
        String path = activity.getCacheDir().toString() + "/AstroWeatherApp/"+ MainActivity.location+".json";
        System.out.println("path w czekerze "+path);
        File f = new File(path);
        if (f.exists() && !f.isDirectory()) {
            exists=true;
        }
        return exists;
    }



}
