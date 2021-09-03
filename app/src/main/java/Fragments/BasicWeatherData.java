package Fragments;

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
import androidx.fragment.app.FragmentActivity;

import com.example.astroapp.JSONReader;
import com.example.astroapp.MainActivity;
import com.example.astroapp.R;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Interfaces.FragmentInterface;

public class BasicWeatherData extends Fragment implements FragmentInterface {
    private static TextView cityTextView,pressureTextView, temperatureTextView, descriptionTextView;
    private static ImageView weatherPictureImageView;
    public static String city, description, pressure, temperature, weatherPictureName;
    public static int isCelsiusGlobal;
    public int icon;
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.basic, container, false);

        cityTextView = (TextView) view.findViewById(R.id.city);
        pressureTextView = (TextView) view.findViewById(R.id.pressure);
        temperatureTextView = (TextView) view.findViewById(R.id.temperature);
        descriptionTextView = (TextView) view.findViewById(R.id.description);
        weatherPictureImageView = (ImageView) view.findViewById(R.id.weatherPicture);

        city= MainActivity.location;
        isCelsiusGlobal=MainActivity.isCelsius;

        if(savedInstanceState!=null){
            city = savedInstanceState.getString("city");
            isCelsiusGlobal = savedInstanceState.getInt("isCelsius");
        }
        System.out.println("City: "+city);
        Map<String, Object> weatherData = getDataFromJson(city+",", isCelsiusGlobal, getActivity());

        updateCurrentFragment(weatherData, getActivity());

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("city",city);
        outState.putInt("isCelsius",isCelsiusGlobal);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public Map<String, Object> getDataFromJson(String location, int isCelsius, Activity activity) {
        Map<String, Object> weatherInfoFromJson = new HashMap<String, Object>();
        System.out.println("Location in getDataFromJson: "+location);
        JSONObject jsonObject = JSONReader.JSONReadAndParse(activity,location);
        System.out.println("JsonObject w get: "+jsonObject);
        String tempUnit;
        String pressureUnit;
        isCelsiusGlobal=isCelsius;

        if(jsonObject!=null) {
            city = (String) ((JSONObject) ((JSONObject) jsonObject.get("response")).get("place")).get("name");
            description = (String) ((JSONObject) ((JSONObject) jsonObject.get("response")).get("ob")).get("weather");
            pressure = (((JSONObject) ((JSONObject) jsonObject.get("response")).get("ob")).get("pressureMB")).toString();
            weatherPictureName = (String) ((JSONObject) ((JSONObject) jsonObject.get("response")).get("ob")).get("icon");

            if (isCelsius == 0) {
                tempUnit = "°C";
                pressureUnit = "hPa";
                temperature = (((JSONObject) ((JSONObject) jsonObject.get("response")).get("ob")).get("tempC")).toString();
            }
            else {
                tempUnit = "°F";
                pressureUnit = "''Hg";
                temperature = (((JSONObject) ((JSONObject) jsonObject.get("response")).get("ob")).get("tempF")).toString();
            }

            weatherInfoFromJson.put("City", city);
            weatherInfoFromJson.put("Description", description);
            weatherInfoFromJson.put("Pressure", pressure);
            weatherInfoFromJson.put("Temperature", temperature);
            weatherInfoFromJson.put("Picture Name", weatherPictureName);

            System.out.println("Mapa z danymi o pogodzie"+ weatherInfoFromJson);
        }else{
            System.out.println("No JSON found");
        }
        return weatherInfoFromJson;
    }

    @Override
    public void updateCurrentFragment(Map<String, Object> jsonData, Activity activity) {
        cityTextView.setText(jsonData.get("City").toString());
        descriptionTextView.setText(jsonData.get("Description").toString());
        pressureTextView.setText(jsonData.get("Pressure").toString()+" Hpa");
        temperatureTextView.setText(jsonData.get("Temperature").toString()+" °C");

        String fullPictureName =  jsonData.get("Picture Name").toString();
        String[] splitedImageName = fullPictureName.split("[.]");
        String imagenameNoExtension = splitedImageName[0];

        icon = activity.getResources().getIdentifier(imagenameNoExtension, "drawable",
                activity.getPackageName());

        System.out.println("Kod obrazka"+ icon);
        weatherPictureImageView.setImageResource(icon);
    }
}
