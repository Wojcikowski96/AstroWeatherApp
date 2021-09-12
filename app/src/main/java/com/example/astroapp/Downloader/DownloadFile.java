package com.example.astroapp.Downloader;

import android.app.Activity;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.example.astroapp.Forecasts.GetForecastWeather;
import com.example.astroapp.Forecasts.GetForecastWeatherDaily;

public class DownloadFile {
//    public static void getWeatherInfo(String location, int isCelsius, Activity activity) {
//        //Pobieranie prognozy z Yahoo
//        if (com.example.astroapp.Utils.isNetworkAvailable(activity)) {
//            YahooWeatherService yahooWeatherService;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                try {
//                    yahooWeatherService = new YahooWeatherService(location, activity, isCelsius);
//                    yahooWeatherService.execute();
//                    String weatherData = yahooWeatherService.get();
//                    yahooWeatherService.createFile(weatherData, activity);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        } else {
//            Toast.makeText(activity, "Brak połączenia z Internetem, dane mogą być nieaktualne", Toast.LENGTH_LONG).show();
//        }
//    }
    public static void download(String forecastType, Activity activity, String location, int isCelcus){

        String unit="metric";
        if(isCelcus==0){
            unit = "metric";
        }else{
            unit = "imperial";        }

        if(forecastType == "current"){
            GetForecastWeather forecast = new GetForecastWeather();

            String JSON = null;
            try {
                JSON = forecast.execute(location,unit).get();
                System.out.println("JSON: "+JSON);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                forecast.createJsonFile(JSON, location, activity);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(forecastType =="daily"){
            GetForecastWeatherDaily forecastDaily = new GetForecastWeatherDaily();
            String JSON = null;
            try {
                JSON = forecastDaily.execute(location,unit).get();
                System.out.println("JSON: "+JSON);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                forecastDaily.createJsonFile(JSON, location, activity);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}

