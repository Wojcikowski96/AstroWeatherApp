package com.example.astroapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.astroapp.AstroCalculator.AstroCalculations;

import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import Fragments.AdditionalWeatherData;
import Fragments.BasicWeatherData;
import Fragments.ForecastWeather;
import Fragments.Moon;
import Fragments.Sun;

public class settings extends AppCompatActivity {
   public EditText LatDegrees, LatMinutes, LonDegrees, LonMinutes;
    public static String LatDeg ="51";
    public static String LatMin = "45";
    public static String LatD = "N";
    public static String LonDeg = "19";
    public static String LonMin = "28";
    public static String LonD = "E";
    public static String location=MainActivity.location.toUpperCase();
    Button Submit, ClearListButton, addCityButton;
    public static double[] calculatedCoords;
    int LatDSelection;
    public static Spinner LatDir,LonDir, freqSpinner, citiesSpinner, units;
    int LonDSelection;
    int intervalSelection;
    public static List<Handler> handlers = new ArrayList<>();
    public static List<Runnable> runnables = new ArrayList<>();
    public static String[] sunStrings;
    public static String[] moonStrings;
    public static boolean checkIfButton = false;
    public static String interval="60s";
    public static EditText localizationInputEditText;
    public static ArrayList<String> cities;
    public static ArrayAdapter<String> citiesAdapter;
    public static int citiesSpinnerSelection;
    public static int isCelsius=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        LatDegrees= (EditText) findViewById(R.id.LatDegrees);
        LatMinutes= (EditText) findViewById(R.id.LatMinutes);
        LonDegrees= (EditText) findViewById(R.id.LonDegrees);
        LonMinutes= (EditText) findViewById(R.id.LonMinutes);

        Submit= (Button) findViewById(R.id.Submit);
        ClearListButton= (Button) findViewById(R.id.ClearList);
        addCityButton = (Button) findViewById(R.id.addCity);
        localizationInputEditText= (EditText) findViewById(R.id.localizationInput);

        int spinnerItemStyle;
        if (MainActivity.smallestScreenWidth >=600) {
            LatDegrees.setTextSize(50);
            LatMinutes.setTextSize(50);
            LonDegrees.setTextSize(50);
            LonMinutes.setTextSize(50);
            localizationInputEditText.setTextSize(50);
            spinnerItemStyle=R.layout.spinner_item;
        } else {
            spinnerItemStyle=android.R.layout.simple_spinner_dropdown_item;
        }

        LatDir = findViewById(R.id.latSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.LatDirs, spinnerItemStyle);
        adapter.setDropDownViewResource(spinnerItemStyle);
        LatDir.setAdapter(adapter);

        LonDir = findViewById(R.id.lonSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.LonDirs, spinnerItemStyle);
        adapter2.setDropDownViewResource(spinnerItemStyle);
        LonDir.setAdapter(adapter2);

        freqSpinner = findViewById(R.id.freqSpinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,R.array.updateIntervals, spinnerItemStyle);
        adapter3.setDropDownViewResource(spinnerItemStyle);
        freqSpinner.setAdapter(adapter3);

        units = findViewById(R.id.unitSpinner);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,R.array.units, spinnerItemStyle);
        adapter4.setDropDownViewResource(spinnerItemStyle);
        units.setAdapter(adapter4);

        citiesSpinner = findViewById(R.id.localizationListSpinner);
        cities = MainActivity.locations.getAll();
        citiesAdapter = new ArrayAdapter<>(this, spinnerItemStyle, cities);
        citiesAdapter.setDropDownViewResource(spinnerItemStyle);
        citiesSpinner.setAdapter(citiesAdapter);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        isCelsius = sharedPref.getInt("units", 0);
        citiesSpinnerSelection = sharedPref.getInt("citiesSpinnerSelection", 0);
        interval = sharedPref.getString("interval", "lodz");

        if(savedInstanceState != null){
            LatDeg =savedInstanceState.getString("LatDeg");
            LatMin=savedInstanceState.getString("LatMin");
            LatD=savedInstanceState.getString("LatD");

            LonDeg =savedInstanceState.getString("LonDeg");
            LonMin =savedInstanceState.getString("LonMin");
            LonD=savedInstanceState.getString("LonD");

            interval=savedInstanceState.getString("Interval");
            citiesSpinnerSelection=savedInstanceState.getInt("citiesSpinnerSelection");
            isCelsius = savedInstanceState.getInt("unitsSelection");
        }

        LatDegrees.setText(LatDeg);
        LatMinutes.setText(LatMin);

        if (LatD.equals("N")) {
            LatDSelection=0;
        } else {LatDSelection=1;}

        LatDir.setSelection(LatDSelection);
        LonDegrees.setText(LonDeg);
        LonMinutes.setText(LonMin);

        if (LonD.equals("E")) {
            LonDSelection=0;
        } else {LonDSelection=1;}

        LonDir.setSelection(LonDSelection);

        String[] items = getResources().getStringArray(R.array.updateIntervals);
        intervalSelection=Arrays.asList(items).indexOf(interval);

        freqSpinner.setSelection(intervalSelection);
        citiesSpinner.setSelection(citiesSpinnerSelection);
        units.setSelection(isCelsius);

        Submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                checkIfButton=true;
                String[] coords = getTextFromSettings(LatDegrees, LatMinutes, LatDir, LonDegrees, LonMinutes, LonDir);

                boolean checkIfError = Utils.checkForInputErrors(coords);
                boolean checkForRange = true;

                if(!checkIfError){
                    Context c = getApplicationContext();
                    Toast.makeText(c, "Błędny typ danych", Toast.LENGTH_LONG).show();
                }else{
                    checkForRange = Utils.checkForInputRange(coords);
                }

                if(!checkForRange){
                    Context c = getApplicationContext();
                    Toast.makeText(c, "Zły zakres współrzędnych",
                            Toast.LENGTH_LONG).show();

                }
                if(checkIfError && checkForRange){

                    calculatedCoords = convertFromNSEW(coords[2],coords[0],coords[1],coords[5],coords[3],coords[4]);

                    int[] currTime=getCurrentTime();

                    sunStrings = AstroCalculations.astroCalculations(calculatedCoords, currTime,"sun");
                    moonStrings = AstroCalculations.astroCalculations(calculatedCoords, currTime,"moon");

                    Sun sun = (Sun) MainActivity.viewPagerAdapter.getItem(3);
                    sun.update(sunStrings);

                    Moon moon = (Moon) MainActivity.viewPagerAdapter.getItem(4);
                    moon.update(moonStrings);

                    Refresher.clearRefresherThread(handlers,runnables);

                    interval = freqSpinner.getSelectedItem().toString();
                    intervalSelection = freqSpinner.getSelectedItemPosition();
                    location = citiesSpinner.getSelectedItem().toString();
                    isCelsius = units.getSelectedItemPosition();
                    interval=freqSpinner.getSelectedItem().toString();

                    Refresher.myRunnable(handlers, runnables, interval, calculatedCoords,settings.this);
                    MainActivity.coordsWidgetTextViewV.setText(LatDeg+"°"+LatMin+"'"+LatD+"  "+ LonDeg+"°"+LonMin+"'"+LonD);

                   // DownloadFile.getWeatherInfo(location, isCelsius,settings.this);
                    DownloadFile.download("current",settings.this, location);

//                    BasicWeatherData basic = (BasicWeatherData)  MainActivity.viewPagerAdapter.getItem(0);
//                    basic.updateBasicFragment(citiesSpinner.getSelectedItem().toString().toLowerCase(), isCelsius, settings.this);

                    BasicWeatherData basic = (BasicWeatherData)  MainActivity.viewPagerAdapter.getItem(0);
                    Map<String, Object> weatherDataBasic = basic.getDataFromJson(citiesSpinner.getSelectedItem().toString().toLowerCase(), isCelsius, settings.this);
                    basic.updateCurrentFragment(weatherDataBasic, settings.this);

                    AdditionalWeatherData additional = (AdditionalWeatherData)  MainActivity.viewPagerAdapter.getItem(1);
                    Map<String, Object> weatherDataAdditional = additional.getDataFromJson(citiesSpinner.getSelectedItem().toString().toLowerCase(), isCelsius, settings.this);
                    additional.updateCurrentFragment(weatherDataAdditional, settings.this);

                    ForecastWeather forecast = (ForecastWeather)  MainActivity.viewPagerAdapter.getItem(2);
                    forecast.updateForecastFragment(citiesSpinner.getSelectedItem().toString().toLowerCase(), isCelsius, settings.this);
                }}


        });

        ClearListButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                MainActivity.locations.deleteAllRecords();
                citiesAdapter.clear();
                citiesAdapter.notifyDataSetChanged();
        }});

        addCityButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                String location = localizationInputEditText.getText().toString().toLowerCase();

                if(!MainActivity.locations.checkIfRecordExists(location)) {
                   // DownloadFile.getWeatherInfo(location, isCelsius,settings.this);
                    DownloadFile.download("current",settings.this, location);
                    JSONObject jsonObject = JSONReader.JSONReadAndParse(settings.this, location);

                    if(jsonObject==null){
                        Toast.makeText(settings.this, "Aeris nie ma takiego miasta", Toast.LENGTH_LONG).show();

                    }else{
                        Double longitude = (Double) ((JSONObject) ((JSONObject) jsonObject.get("response")).get("loc")).get("long");
                        Double latitude = (Double) ((JSONObject) ((JSONObject) jsonObject.get("response")).get("loc")).get("lat");;
                        //Long woeid = (Long) ((JSONObject) jsonObject.get("location")).get("woeid");

                        MainActivity.locations.insert(location, null, longitude, latitude);
                        citiesAdapter.add(location);
                        citiesAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(settings.this, "Takie miasto już jest w bazie, wybierz je z listy", Toast.LENGTH_LONG).show();
                }
            }}) ;

        citiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                location = citiesSpinner.getSelectedItem().toString();
                citiesSpinnerSelection = citiesSpinner.getSelectedItemPosition();
                localizationInputEditText.setText(location);

                String latitudeFloat = MainActivity.locations.getData(location,"Latitude");
                String longitudeFloat = MainActivity.locations.getData(location,"Longitude");

                convertToNSEW(latitudeFloat, longitudeFloat);

                LatDegrees.setText(LatDeg);
                LatMinutes.setText(LatMin);
                LonDegrees.setText(LonDeg);
                LonMinutes.setText(LonMin);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        units.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                isCelsius = units.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        LonDir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                LonD=LonDir.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        LatDir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                LatD=LatDir.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        freqSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                interval=freqSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    public static String []  getTextFromSettings(EditText LatDegrees, EditText LatMinutes, Spinner LatDir, EditText LonDegrees, EditText LonMinutes, Spinner LonDir){
        String [] coords = new String[6];

        LatDeg=LatDegrees.getText().toString();
        coords[0] = LatDeg;
        LatMin=LatMinutes.getText().toString();
        coords[1] = LatMin;
        LatD=LatDir.getSelectedItem().toString();
        coords[2] = LatD;
        LonDeg=LonDegrees.getText().toString();
        coords[3] = LonDeg;
        LonMin=LonMinutes.getText().toString();
        coords[4] = LonMin;
        LonD=LonDir.getSelectedItem().toString();
        coords[5] = LonD;

        return coords;
    }

    public static double[] convertFromNSEW(String LatD, String LatDeg, String LatMin, String LonD, String LonDeg, String LonMin) {
        double[] calculatedCoords = new double[2];
        if(LatD.equals("N")){
            calculatedCoords[0] = Double.parseDouble(LatDeg)+Double.parseDouble(LatMin)/60;
        }else {
            calculatedCoords[0] = -(Double.parseDouble(LatDeg)+Double.parseDouble(LatMin)/60);
        }

        if(LonD.equals("E")){
            calculatedCoords[1] = Double.parseDouble(LonDeg)+Double.parseDouble(LonMin)/60;
        }else {
            calculatedCoords[1] = -(Double.parseDouble(LonDeg)+Double.parseDouble(LonMin)/60);
        }
        return  calculatedCoords;
    }

    public static void convertToNSEW(String latitudeFloat, String longitudeFloat) {

        Double latDec, lonDec;
        double doubleNumber = Double.parseDouble(latitudeFloat);
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(doubleNumber));
        int intValue = bigDecimal.intValue();
        if(intValue < 0) {
             latDec = (Double.valueOf(intValue))*-1;
             LatDir.setSelection(1);

        }else{
             latDec = Double.valueOf(intValue);
             LatDir.setSelection(0);
        }

        Double latUlamek = Double.parseDouble(bigDecimal.subtract(new BigDecimal(intValue)).toPlainString());

        double doubleNumber2 = Double.parseDouble(longitudeFloat);
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(doubleNumber2));
        int intValue2 = bigDecimal2.intValue();

        if(intValue2 < 0){
            lonDec = (Double.valueOf(intValue2)) *-1;
            LonDir.setSelection(1);
        }else{
            lonDec = Double.valueOf(intValue2);
            LonDir.setSelection(0);
        }

        Double lonUlamek = Double.parseDouble(bigDecimal2.subtract(new BigDecimal(intValue2)).toPlainString());

        Double latMin = (60*(latUlamek));

        if(latMin < 0){
            latMin = (60*(latUlamek))*-1;
        }

        Double lonMin = (60*(lonUlamek));

        if(lonMin < 0){
            lonMin = (60*(lonUlamek))*-1;
        }

        LatDeg = Integer.toString((int)Math.round(latDec));
        LatMin = Integer.toString((int)Math.round(latMin));
        LonDeg = Integer.toString((int)Math.round(lonDec));
        LonMin = Integer.toString((int)Math.round(lonMin));
    }

    public static int[] getCurrentTime() {
        int[] currTime = new int[7];
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getDefault());
        currTime[0] = c.get(Calendar.YEAR);
        currTime[1] = c.get(Calendar.MONTH) + 1;
        currTime[2] = c.get(Calendar.DAY_OF_MONTH);
        currTime[3] = c.get(Calendar.HOUR_OF_DAY);
        currTime[4] = c.get(Calendar.MINUTE);
        currTime[5] = c.get(Calendar.SECOND);
        currTime[6] = c.get(Calendar.ZONE_OFFSET) + 1;
        return currTime;
    }

    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("LatDeg",LatDeg);
        outState.putString("LatMin",LatMin);
        outState.putString("LatD",LatD);
        outState.putString("LonDeg",LonDeg);
        outState.putString("LonMin",LonMin);
        outState.putString("LonD",LonD);
        outState.putString("Interval",interval);
        outState.putInt("citiesSpinnerSelection",citiesSpinnerSelection);
        outState.putInt("unitsSelection",isCelsius);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        System.out.println("Niszczę settings activity");
    }

    @Override protected void onPause() {
        super.onPause();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("units", isCelsius);
        editor.putInt("citiesSpinnerSelection", citiesSpinnerSelection);
        editor.putString("interval", interval);
        editor.apply();
    }

    @Override protected void onStop() {
        super.onStop();
        System.out.println("Stopuję settings activity");
    }
}
