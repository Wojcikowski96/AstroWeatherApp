package com.example.astroapp.Interfaces;

import android.app.Activity;

import org.json.JSONException;

import java.util.Map;

public interface FragmentInterface {
    Map<String, Object> getDataFromJson(String location, int isCelsius, Activity activity) throws JSONException;
    void updateCurrentFragment(Map<String, Object> jsonData, Activity activity, String location);

}
