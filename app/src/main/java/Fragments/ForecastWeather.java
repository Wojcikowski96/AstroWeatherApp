package Fragments;

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

import com.example.astroapp.JSONReader;
import com.example.astroapp.MainActivity;
import com.example.astroapp.R;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ForecastWeather extends Fragment {
    private static ArrayList<String> days = new ArrayList<>(10);
    private static ArrayList <Long> temperatures = new ArrayList<>(10);
    private static ArrayList <String> texts = new ArrayList<>(10);
    private static ArrayList <Long> codes = new ArrayList<>(10);

    private static ArrayList <TextView> dayTextViews = new ArrayList<>(10);
    private static ArrayList <ImageView> imageViews = new ArrayList<>(10);
    private static ArrayList <TextView> temperatureTextViews = new ArrayList<>(10);
    private static ArrayList <TextView> textTextViews = new ArrayList<>(10);

    public static String city;
    public static int isCelsiusGlobal;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.forecast, container, false);

            for (int i = 0; i < 10; i++) {

                days.add("b/d");
                temperatures.add(0L);
                texts.add("b/d");
                codes.add(0L);

                dayTextViews.add(null);
                imageViews.add(null);
                temperatureTextViews.add(null);
                textTextViews.add(null);

                dayTextViews.set(i, (TextView) view.findViewById(getResources().getIdentifier("day" + (i + 1), "id", MainActivity.PACKAGE_NAME)));
                imageViews.set(i, (ImageView) view.findViewById(getResources().getIdentifier("d" + (i + 1), "id", MainActivity.PACKAGE_NAME)));
                temperatureTextViews.set(i, (TextView) view.findViewById(getResources().getIdentifier("temperature" + (i + 1), "id", MainActivity.PACKAGE_NAME)));
                textTextViews.set(i, (TextView) view.findViewById(getResources().getIdentifier("description" + (i + 1), "id", MainActivity.PACKAGE_NAME)));
            }
        city=MainActivity.location;
        isCelsiusGlobal=MainActivity.isCelsius;
            if(savedInstanceState!=null){
            city = savedInstanceState.getString("city");
            isCelsiusGlobal = savedInstanceState.getInt("isCelsius");
        }
        //updateForecastFragment(city.toLowerCase(), isCelsiusGlobal, getActivity());
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void updateForecastFragment(String location, int isCelsius, Activity activity){
//        JSONObject jsonObject = JSONReader.JSONReadAndParse(activity,location);
//        String tempUnit;
//        isCelsiusGlobal=isCelsius;
//        if(jsonObject!=null) {
//            JSONArray forecast = (JSONArray) jsonObject.get("forecasts");
//            city = (String) ((JSONObject) jsonObject.get("location")).get("city");
//
//            for (int i = 0; i < 10; i++) {
//                days.set(i,(String) ((JSONObject) (forecast.get(i))).get("day"));
//                temperatures.set(i,(Long) ((JSONObject) (forecast.get(i))).get("high"));
//                texts.set(i,(String) ((JSONObject) (forecast.get(i))).get("text"));
//                codes.set(i,(Long) ((JSONObject) (forecast.get(i))).get("code"));
//            }
//
//            for (int i = 0; i < 10; i++) {
//                dayTextViews.get(i).setText(days.get(i));
//                imageViews.get(i).setImageResource(activity.getResources().getIdentifier("w" + codes.get(i), "drawable", MainActivity.PACKAGE_NAME));
//                if (isCelsius == 0)
//                    tempUnit = "°C";
//                else
//                    tempUnit = "°F";
//                temperatureTextViews.get(i).setText((temperatures.get(i)).toString() + tempUnit);
//                textTextViews.get(i).setText(texts.get(i));
//            }
//        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("city",city);
        outState.putInt("isCelsius",isCelsiusGlobal);
    }
}
