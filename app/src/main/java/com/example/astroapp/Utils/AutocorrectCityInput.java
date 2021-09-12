package com.example.astroapp.Utils;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AutocorrectCityInput extends AsyncTask<String, String, String> {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... strings) {
        String JSON = null;
        String cityName = null;
        try {
            JSON = getRequest(strings);
            System.out.println("Json w utils: "+JSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject object = new JSONObject(JSON);
            String correctedCity = (String) object.get("name");
            String[] locationParts = correctedCity.split(" ");
            cityName = locationParts[0];
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cityName;

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getRequest(String[] params) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
//                .url("https://aerisweather1.p.rapidapi.com/observations/"+location[0]+",")
//                .get()
//                .addHeader("x-rapidapi-host", "aerisweather1.p.rapidapi.com")
//                .addHeader("x-rapidapi-key", "fa1edb6aeemshdbfbc48fbeef1aap11530djsn3acd3fde15cb")
//                .build();
                .url("https://community-open-weather-map.p.rapidapi.com/weather?q="+ params[0]+"&lat=0&lon=0&lang=null&units=imperial")
                .get()
                .addHeader("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "fa1edb6aeemshdbfbc48fbeef1aap11530djsn3acd3fde15cb")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }

    }
}
