package com.example.astroapp;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JSONReader {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static JSONObject JSONReadAndParse(Activity activity, String location) {
        JSONObject jsonObject = null;
        JSONParser jsonParser = new JSONParser();


            System.out.println("Wchodzę");
            try (FileReader reader = new FileReader(activity.getCacheDir().toString() + "/AstroWeatherApp/"+location+"CurrentForecast.json")) {
                jsonObject = (JSONObject) jsonParser.parse(reader);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        return jsonObject;
    }
}
