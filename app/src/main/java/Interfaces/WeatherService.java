package Interfaces;

import android.app.Activity;

import org.json.JSONException;

import java.io.IOException;

public interface WeatherService{
    void createJsonFile(String jsonContent, String location, Activity activity) throws JSONException, IOException;
    String getRequest(String[] location) throws IOException;

}
