package com.example.astroapp.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.astroapp.Interfaces.FragmentInterface;
import com.example.astroapp.JSONReader;
import com.example.astroapp.MainActivity;
import com.example.astroapp.R;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ForecastWeather extends Fragment implements FragmentInterface {
    private static ArrayList<String> days = new ArrayList<>(10);
    private static ArrayList<String> temperatures = new ArrayList<>(10);
    private static ArrayList<String> texts = new ArrayList<>(10);
    private static ArrayList<String> codes = new ArrayList<>(10);

    private static ArrayList<TextView> dayTextViews = new ArrayList<>(10);
    private static ArrayList<ImageView> imageViews = new ArrayList<>(10);
    private static ArrayList<TextView> temperatureTextViews = new ArrayList<>(10);
    private static ArrayList<TextView> textTextViews = new ArrayList<>(10);

    public static String city;
    public static int isCelsiusGlobal;
    List<Object> forecastObject = new ArrayList<>();

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.forecast, container, false);

        for (int i = 0; i < 10; i++) {

            days.add("b/d");
            temperatures.add("0L");
            texts.add("b/d");
            codes.add("0L");

            dayTextViews.add(null);
            imageViews.add(null);
            temperatureTextViews.add(null);
            textTextViews.add(null);

            dayTextViews.set(i, (TextView) view.findViewById(getResources().getIdentifier("day" + (i + 1), "id", MainActivity.PACKAGE_NAME)));
            imageViews.set(i, (ImageView) view.findViewById(getResources().getIdentifier("d" + (i + 1), "id", MainActivity.PACKAGE_NAME)));
            temperatureTextViews.set(i, (TextView) view.findViewById(getResources().getIdentifier("temperature" + (i + 1), "id", MainActivity.PACKAGE_NAME)));
            textTextViews.set(i, (TextView) view.findViewById(getResources().getIdentifier("description" + (i + 1), "id", MainActivity.PACKAGE_NAME)));
        }
        city = MainActivity.location;
        isCelsiusGlobal = MainActivity.isCelsius;
        if (savedInstanceState != null) {
            city = savedInstanceState.getString("city");
            isCelsiusGlobal = savedInstanceState.getInt("isCelsius");
        }
        try {
            Map<String, Object> weatherData = getDataFromJson(city, isCelsiusGlobal, getActivity());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, Object> weatherData = null;
        try {
            weatherData = getDataFromJson(city, isCelsiusGlobal, getActivity());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("Weather data w additional w onCreateView" + weatherData);
        updateCurrentFragment(weatherData, getActivity(), city);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void updateForecastFragment(String location, int isCelsius, Activity activity) {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("city", city);
        outState.putInt("isCelsius", isCelsiusGlobal);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public Map<String, Object> getDataFromJson(String location, int isCelsius, Activity activity) throws JSONException {
        Map<String, Object> weatherForEachDay = new HashMap<String, Object>();

        String description, icon, temperatureDay;
        String temperatureUnit;
        if (location.contains("/")) {
            String[] locationParts = location.split("/");
            location = locationParts[1];
        }

        JSONObject jsonObject = JSONReader.JSONReadAndParse(activity, location, "Daily");

        isCelsiusGlobal = isCelsius;
        if (jsonObject != null) {
            JSONArray list = (JSONArray) jsonObject.get("list");

            for (int i = 0; i < list.size(); i++) {
                Map <String, Object> innerWeatherFields = new HashMap<String, Object>();
                JSONArray weatherItem = (JSONArray) ((JSONObject) list.get(i)).get("weather");
                description = (String)((JSONObject) weatherItem.get(0)).get("main");
                icon = (String)((JSONObject) weatherItem.get(0)).get("icon");
                JSONObject temperatureObject = (JSONObject) ((JSONObject)list.get(i)).get("temp");

                if (isCelsius == 0) {
                    temperatureUnit = "°C";
                    temperatureDay =  temperatureObject.get("day").toString() + temperatureUnit;

                } else {
                    temperatureUnit = "°F";
                    temperatureDay =  temperatureObject.get("day").toString() + temperatureUnit;
                }

                innerWeatherFields.put("Description", description);
                innerWeatherFields.put("Icon", icon);
                innerWeatherFields.put("Temperature", temperatureDay);

                weatherForEachDay.put("Day"+i, innerWeatherFields);
            }
        }
        return weatherForEachDay;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void updateCurrentFragment(Map<String, Object> jsonData, Activity activity, String location) {
        String temperature, pictureCode, description;


        if(jsonData!=null) {
            jsonData.forEach((k,v) -> {
                int i = Integer.parseInt(String.valueOf(k.charAt(k.length()-1)));
                HashMap nestedMapObject = (HashMap)jsonData.get(k);
                days.set(i,k);
                temperatures.set(i, (String) nestedMapObject.get("Temperature"));
                texts.set(i,(String) nestedMapObject.get("Description"));
                codes.set(i,(String) nestedMapObject.get("Icon"));


            });
            System.out.println("Kody obrazków: "+codes);

            for (int i = 0; i < jsonData.size(); i++) {
                dayTextViews.get(i).setText(days.get(i));
                imageViews.get(i).setImageResource(activity.getResources().getIdentifier("i" + codes.get(i), "drawable", MainActivity.PACKAGE_NAME));
                temperatureTextViews.get(i).setText((temperatures.get(i)));
                textTextViews.get(i).setText(texts.get(i));
            }
        }

    }
}
