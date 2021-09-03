package Fragments;

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

import org.json.simple.JSONObject;

public class AdditionalWeatherData extends Fragment {
    public static TextView speedanddirTextView, airTextView;
    public static String city;
    public static String speed, humidity, transparency;
    public static int isCelsiusGlobal;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.additional, container, false);
        speedanddirTextView = (TextView) view.findViewById(R.id.forceanddir);
        airTextView = (TextView) view.findViewById(R.id.air);

        city= MainActivity.location;
        isCelsiusGlobal=MainActivity.isCelsius;

        if(savedInstanceState!=null){
            city = savedInstanceState.getString("city");
            isCelsiusGlobal = savedInstanceState.getInt("isCelsius");
        }

        //updateAdditionalFragment(city.toLowerCase(), isCelsiusGlobal, getActivity());
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void updateAdditionalFragment(String location, int isCelsius, Activity activity){
        String speedUnit;
        JSONObject jsonObject = JSONReader.JSONReadAndParse(activity,location);
        isCelsiusGlobal=isCelsius;
        if(jsonObject!=null) {
            city = (String) ((JSONObject) jsonObject.get("location")).get("city");
            Long direction = (Long) ((JSONObject) ((JSONObject) jsonObject.get("current_observation")).get("wind")).get("direction");
            speed = (((JSONObject) ((JSONObject) jsonObject.get("current_observation")).get("wind")).get("speed")).toString();
            humidity = (((JSONObject) ((JSONObject) jsonObject.get("current_observation")).get("atmosphere")).get("humidity")).toString();
            transparency = (((JSONObject) ((JSONObject) jsonObject.get("current_observation")).get("atmosphere")).get("visibility")).toString();

            if (isCelsius == 0) {
                speedUnit = "km/h";
            }
            else {
                speedUnit = "mph";
            }

            speedanddirTextView.setText("Speed: " + speed + speedUnit+"\n" + "Direction: " + direction.toString() + "°");
            airTextView.setText("Humidity: " + humidity + "%\n " + "Clarity: " + transparency + "%");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("city",city);
        outState.putInt("isCelsius",isCelsiusGlobal);
    }
}
