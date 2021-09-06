package com.example.astroapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.astroapp.AstroCalculator.AstroCalculations;

import LocationDatabase.DBHelper;

public class MainActivity extends AppCompatActivity {

    public static String PACKAGE_NAME = null;
    static Bundle sunData;
    static Bundle moonData;
    static String[] stringsToBundleSun;
    static String[] stringsToBundleMoon;
    String LatDeg = "51";
    String LatMin = "45";
    String LatD = "N";
    String LonDeg = "19";
    String LonMin = "28";
    String LonD = "E";
    public static String location="lodz";
    public static int isCelsius = 0;
    public static String interval = "2s";
    public static TextView coordsWidgetTextViewV;
    public static boolean exists;
    public static DBHelper locations;
    static ViewPagerAdapter viewPagerAdapter;
    public static int orientation;
    public static int smallestScreenWidth;
    public static double[] calculatedCoords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        System.out.println("Robię on create mainActivity");
//        GetForecastWeather forecast = new GetForecastWeather();
//
//        String JSON = null;
//        try {
//            JSON = forecast.execute(location).get();
//            System.out.println("JSON: "+JSON);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        try {
//            forecast.createJsonFile(JSON, location, this);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        DownloadFile.download("current",this, location);
        orientation = getResources().getConfiguration().orientation;
        smallestScreenWidth = getResources().getConfiguration().smallestScreenWidthDp;

        calculatedCoords = settings.convertFromNSEW(LatD, LatDeg, LatMin, LonD, LonDeg, LonMin);

        int[] currTime = settings.getCurrentTime();

        stringsToBundleSun = AstroCalculations.astroCalculations(calculatedCoords, currTime, "sun");
        stringsToBundleMoon = AstroCalculations.astroCalculations(calculatedCoords, currTime, "moon");

        sunData = Utils.putDataToBundle(stringsToBundleSun, "sun");
        moonData = Utils.putDataToBundle(stringsToBundleMoon, "moon");

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        coordsWidgetTextViewV = (TextView) findViewById(R.id.coordsWidgetTextView);

        TextView timeClock = findViewById(R.id.clock);

        if (MainActivity.smallestScreenWidth >=600) {
            timeClock.setTextSize(56);
        }

        if (savedInstanceState != null) {
            LatDeg = savedInstanceState.getString("LatDeg");
            LatMin = savedInstanceState.getString("LatMin");
            LatD = savedInstanceState.getString("LatD");
            LonDeg = savedInstanceState.getString("LonDeg");
            LonMin = savedInstanceState.getString("LonMin");
            LonD = savedInstanceState.getString("LonD");
            interval = savedInstanceState.getString("IntervalFromSettings");
        }
        coordsWidgetTextViewV.setText(LatDeg + "°" + LatMin + "'" + LatD + "  " + LonDeg + "°" + LonMin + "'" + LonD);

        if (settings.checkIfButton) {
            location = settings.location.toLowerCase();
            isCelsius = settings.isCelsius;
        }

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        String sharedLocation = sharedPref.getString("location", "lodz");

        if (!sharedLocation.equals("")) {
            location = sharedPref.getString("location", "lodz");
            System.out.println("Location z shared"+location);
            isCelsius = sharedPref.getInt("units", 0);
            interval = sharedPref.getString("IntervalFromSettings", "2s");
        }

        //DownloadFile.getWeatherInfo(location, isCelsius,this);

        if (Utils.checkIfFileExists(this)) {
            exists=true;
        } else {
            Toast.makeText(this, "Nie znaleziono pliku z danymi o pogodzie, włącz Internet aby pobrać dane", Toast.LENGTH_LONG).show();
        }

        //To jest potrzebne gdzie indziej
        PACKAGE_NAME = getApplicationContext().getPackageName();

        //Utwórz bazę danych
        locations = new DBHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            openSettingsActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openSettingsActivity() {
        Intent intent = new Intent(this, settings.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (settings.handlers.size() > 0 && settings.runnables.size() > 0) { settings.handlers.get(settings.handlers.size() - 1).removeCallbacks(settings.runnables.get(settings.runnables.size() - 1));
        }
        super.onDestroy();
    }

    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (settings.checkIfButton) {
            outState.putString("LatDeg", settings.LatDeg);
            outState.putString("LatMin", settings.LatMin);
            outState.putString("LatD", settings.LatD);
            outState.putString("LonDeg", settings.LonDeg);
            outState.putString("LonMin", settings.LonMin);
            outState.putString("LonD", settings.LonD);
            outState.putString("IntervalFromSettings", settings.interval);
        } else {
            outState.putString("LatDeg", LatDeg);
            outState.putString("LatMin", LatMin);
            outState.putString("LatD", LatD);
            outState.putString("LonDeg", LonDeg);
            outState.putString("LonMin", LonMin);
            outState.putString("LonD", LonD);
            outState.putString("IntervalFromSettings", interval);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Refresher.clearRefresherThread(settings.handlers, settings.runnables);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (settings.checkIfButton) {
            editor.putString("location", settings.location);
            editor.putInt("units", settings.isCelsius);
            editor.putString("IntervalFromSettings", settings.interval);
            } else {
            editor.putString("location", location);
            editor.putInt("units", isCelsius);
            editor.putString("IntervalFromSettings", interval);
        }
        editor.apply();
    }

    @Override
    protected void onResume() {
        if (settings.checkIfButton) {
            Refresher.clearRefresherThread(settings.handlers, settings.runnables);
            Refresher.myRunnable(settings.handlers, settings.runnables, settings.interval, settings.calculatedCoords,this);
        } else {
            Refresher.clearRefresherThread(settings.handlers, settings.runnables);
            Refresher.myRunnable(settings.handlers, settings.runnables, interval, calculatedCoords,this);
        }

        super.onResume();
    }
}
