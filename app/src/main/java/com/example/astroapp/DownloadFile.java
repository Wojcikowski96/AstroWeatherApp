package com.example.astroapp;

import android.app.Activity;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import Forecasts.GetForecastWeather;

public class DownloadFile {
//    public static void getWeatherInfo(String location, int isCelsius, Activity activity) {
//        //Pobieranie prognozy z Yahoo
//        if (Utils.isNetworkAvailable(activity)) {
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
    public static void download(String forecastType, Activity activity, String location){

        if(forecastType == "current"){
            GetForecastWeather forecast = new GetForecastWeather();

            String JSON = null;
            try {
                JSON = forecast.execute(location).get();
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

    }
}

