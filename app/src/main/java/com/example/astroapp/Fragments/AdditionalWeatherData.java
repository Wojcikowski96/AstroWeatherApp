package com.example.astroapp.Fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.astroapp.JSONReader;
import com.example.astroapp.MainActivity;
import com.example.astroapp.R;

import java.util.HashMap;
import java.util.Map;

import com.example.astroapp.Interfaces.FragmentInterface;

import org.json.simple.JSONObject;

public class AdditionalWeatherData extends Fragment implements FragmentInterface {
    public static TextView speedanddirTextView, airTextView;
    public static String city;
    public static String speed, humidity, transparency, direction;
    public static int isCelsiusGlobal;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        System.out.println("tworzę additional fragment");
        View view = inflater.inflate(R.layout.additional, container, false);
        speedanddirTextView = (TextView) view.findViewById(R.id.forceanddir);
        airTextView = (TextView) view.findViewById(R.id.air);

        city= MainActivity.location;
        isCelsiusGlobal=MainActivity.isCelsius;

        if(savedInstanceState!=null){
            city = savedInstanceState.getString("city");
            isCelsiusGlobal = savedInstanceState.getInt("isCelsius");
        }
        Map<String, Object> weatherData = getDataFromJson(city, isCelsiusGlobal, getActivity());
        System.out.println("Weather data w additional w onCreateView" + weatherData);
        updateCurrentFragment(weatherData, getActivity(), city);
        //updateAdditionalFragment(city.toLowerCase(), isCelsiusGlobal, getActivity());
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void updateAdditionalFragment(String location, int isCelsius, Activity activity){

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("city",city);
        outState.putInt("isCelsius",isCelsiusGlobal);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public Map<String, Object> getDataFromJson(String location, int isCelsius, Activity activity) {

        Map<String, Object> additionalWeatherData = new HashMap<String, Object>();
        String speedUnit, distanceUnit;
        if(location.contains("/")){
            String [] locationParts = location.split("/");
            location = locationParts[1];
        }
        System.out.println("Location w Additional" + location);
        JSONObject jsonObject = JSONReader.JSONReadAndParse(activity,location, "Current");
        System.out.println("JsonObject Additional" + jsonObject);
        isCelsiusGlobal=isCelsius;
        if(jsonObject!=null) {

            humidity = ((JSONObject)(jsonObject.get("main"))).get("humidity")+ " %";
            transparency = jsonObject.get("visibility").toString() + " m";
            direction = ((JSONObject)jsonObject.get("wind")).get("deg").toString();

            if (isCelsius == 0) {
                speedUnit = " km/h";
                distanceUnit = " km";
                speed = ((JSONObject)jsonObject.get("wind")).get("speed").toString()+ speedUnit;
                String transparencyString =  jsonObject.get("visibility").toString();
                Double transparencyInMeters = Double.valueOf(transparencyString);
                Double transparencyInKm = transparencyInMeters / 1000;
                transparency = transparencyInKm.toString() + distanceUnit;
            }
            else {
                speedUnit = " mph";
                distanceUnit = " miles";
                speed = ((JSONObject)jsonObject.get("wind")).get("speed").toString() + speedUnit;
                String transparencyString =  jsonObject.get("visibility").toString();
                Double transparencyInMeters = Double.valueOf(transparencyString);
                Double transparencyInMiles = transparencyInMeters * 0.00062137;
                transparency =  transparencyInMiles.toString() + distanceUnit;
            }

            additionalWeatherData.put("Humidity", humidity);
            additionalWeatherData.put("Transparency", transparency);
            additionalWeatherData.put("Direction", direction);
            additionalWeatherData.put("Speed", speed);
            System.out.println("Mapa z danymi w additional" + additionalWeatherData);

        }
      return additionalWeatherData;

    }

    @Override
    public void updateCurrentFragment(Map<String, Object> jsonData, Activity activity, String location) {
        speedanddirTextView.setText("Speed: " + jsonData.get("Speed").toString() +"\n" + "Direction: " + jsonData.get("Direction").toString() + "°");
        airTextView.setText("Humidity: " + jsonData.get("Humidity").toString()+ "\n " + "Clarity: " + jsonData.get("Transparency").toString());
    }
}
