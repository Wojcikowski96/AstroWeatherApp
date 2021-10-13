package com.example.astroapp.Forecasts;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.example.astroapp.Interfaces.WeatherService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetForecastWeatherDaily extends AsyncTask<String, String, String> implements WeatherService {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... strings) {
        String JSON = null;
        try {
            JSON = getRequest(strings);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSON;
    }

    @Override
    public void createJsonFile(String jsonContent, String location, Activity activity) throws JSONException, IOException {
        JSONObject object = new JSONObject(jsonContent);
        String filename = location+"DailyForecast.json";
        String dirName = activity.getCacheDir().toString() + "/AstroWeatherApp/";
        System.out.println("Sciezka pliku: "+activity.getCacheDir().toString() + "/AstroWeatherApp/");
        File dir = new File (dirName);
        dir.mkdirs();
        PrintWriter out = new PrintWriter(new FileWriter(dirName+filename));
        out.write(object.toString());
        out.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public String getRequest(String[] params) throws IOException {
        OkHttpClient client = new OkHttpClient();
        System.out.println("Ilośc dni: "+ params[0] +" "+ "Typ jednstki: "+ params[1] + " W prognozie 10 dni");
        Request request = new Request.Builder()
                .url("https://community-open-weather-map.p.rapidapi.com/forecast/daily?q="+ params[0]+"&lat=0&lon=0&lang=null&units="+params[1])
                .get()
                .addHeader("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "fa1edb6aeemshdbfbc48fbeef1aap11530djsn3acd3fde15cb")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }

    }

}

