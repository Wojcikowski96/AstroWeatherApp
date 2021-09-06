package com.example.astroapp;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;

import com.example.astroapp.AstroCalculator.AstroCalculations;

import java.util.List;

import Fragments.Moon;
import Fragments.Sun;

public class Refresher {

    public static String location;
    public static int isCelsius;

    public static void clearRefresherThread(List<Handler> h, List<Runnable> r) {

        if (h.size()>0 && r.size()>0) {
            h.get(h.size() - 1).removeCallbacks(r.get(r.size() - 1));
            h.clear();
            r.clear();
        }
    }

    public static void myRunnable(List<Handler> h, List<Runnable> r, final String interval, final double[] c, final Activity activity) {
        final Handler refreshHandler = new Handler();

        h.add(refreshHandler);

        int delay = 0;

        if(interval.charAt(interval.length()-1)=='s'){
            delay = Integer.parseInt(interval.substring(0,interval.length()-1));
        }else{
            delay = Integer.parseInt(interval.substring(0,interval.length()-3)) * 60;
        }
        final int finalDelay = delay;

        Runnable runnable = new Runnable() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {

                int[] currTime=settings.getCurrentTime();
                String[] sunStrings = AstroCalculations.astroCalculations(c, currTime,"sun");
                String[] moonStrings = AstroCalculations.astroCalculations(c, currTime,"moon");

                if (settings.checkIfButton) {
                    location=settings.location.toLowerCase();
                    isCelsius=settings.isCelsius;
                } else {
                    location=MainActivity.location.toLowerCase();
                    isCelsius=MainActivity.isCelsius;
                }
                
                //DownloadFile.getWeatherInfo(location, isCelsius, activity);
//                DownloadFile.download("current",activity, location);
//                BasicWeatherData basic = (BasicWeatherData)  MainActivity.viewPagerAdapter.getItem(0);
//                Map<String, Object> weatherData = basic.getDataFromJson(location, isCelsius, activity);
//                basic.updateCurrentFragment(weatherData, activity);

//                AdditionalWeatherData additional = (AdditionalWeatherData)  MainActivity.viewPagerAdapter.getItem(1);
//                additional.updateAdditionalFragment(location, isCelsius, activity);
//
//                ForecastWeather forecast = (ForecastWeather)  MainActivity.viewPagerAdapter.getItem(2);
//                forecast.updateForecastFragment(location, isCelsius, activity);

                Sun sun = (Sun) MainActivity.viewPagerAdapter.getItem(3);
                sun.update(sunStrings);

                Moon moon = (Moon) MainActivity.viewPagerAdapter.getItem(4);
                moon.update(moonStrings);

                refreshHandler.postDelayed(this, finalDelay * 1000);
            }
        };
        r.add(runnable);
        refreshHandler.postDelayed(runnable, delay * 1000);
    }
}
