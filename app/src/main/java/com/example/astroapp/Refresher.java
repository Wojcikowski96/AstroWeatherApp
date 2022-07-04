package com.example.astroapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;

import com.example.astroapp.AstroCalculator.AstroCalculations;

import java.util.List;
import java.util.Map;

import com.example.astroapp.Downloader.DownloadFile;
import com.example.astroapp.Fragments.AdditionalWeatherData;
import com.example.astroapp.Fragments.BasicWeatherData;
import com.example.astroapp.Fragments.ForecastWeather;
import com.example.astroapp.Fragments.Moon;
import com.example.astroapp.Fragments.Sun;
import com.example.astroapp.Settings.Settings;

import org.json.JSONException;

public class Refresher {

    public static String location;
    public static int isCelsius;

    public static void clearRefresherThread(List<Handler> h, List<Runnable> r) {

        if (h.size() > 0 && r.size() > 0) {
            h.get(h.size() - 1).removeCallbacks(r.get(r.size() - 1));
            h.clear();
            r.clear();
        }
    }

    public static void myRunnable(List<Handler> h, List<Runnable> r, final String interval, final double[] c, final Activity activity) {
        final Handler refreshHandler = new Handler();

        h.add(refreshHandler);

        int delay = 0;

        if (interval.charAt(interval.length() - 1) == 's') {
            delay = Integer.parseInt(interval.substring(0, interval.length() - 1));
        } else {
            delay = Integer.parseInt(interval.substring(0, interval.length() - 3)) * 60;
        }
        final int finalDelay = delay;

        Runnable runnable = new Runnable() {

            @SuppressLint("NewApi")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {

                int[] currTime = Settings.getCurrentTime();
                String[] sunStrings = AstroCalculations.astroCalculations(c, currTime, "sun");
                String[] moonStrings = AstroCalculations.astroCalculations(c, currTime, "moon");

                if (Settings.checkIfButton) {
                    location = Settings.location;
                    isCelsius = Settings.isCelsius;
                } else {
                    location = MainActivity.location;
                    isCelsius = MainActivity.isCelsius;
                }

                DownloadFile.download("current", activity, location, isCelsius);
                DownloadFile.download("daily", activity, location, isCelsius);

                BasicWeatherData basic = (BasicWeatherData) MainActivity.viewPagerAdapter.getItem(0);
                Map<String, Object> weatherData = null;
                try {
                    weatherData = basic.getDataFromJson(location, isCelsius, activity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                basic.updateCurrentFragment(weatherData, activity, location);

                AdditionalWeatherData additional = (AdditionalWeatherData) MainActivity.viewPagerAdapter.getItem(1);
                Map<String, Object> weatherData2 = null;
                weatherData2 = additional.getDataFromJson(location, isCelsius, activity);
                additional.updateCurrentFragment(weatherData2, activity, location);

                ForecastWeather forecast = (ForecastWeather) MainActivity.viewPagerAdapter.getItem(2);
                Map<String, Object> weatherData3 = null;
                try {
                    weatherData3 = forecast.getDataFromJson(location, isCelsius, activity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                forecast.updateCurrentFragment(weatherData3, activity, location);

                Sun sun = (Sun) MainActivity.viewPagerAdapter.getItem(3);
                sun.update(sunStrings);

                Moon moon = (Moon) MainActivity.viewPagerAdapter.getItem(4);
                moon.update(moonStrings);

                refreshHandler.postDelayed(this, finalDelay * 1000);

                System.out.println("wątek działa");
            }
        };
        r.add(runnable);
        refreshHandler.postDelayed(runnable, delay * 1000);
    }
}
