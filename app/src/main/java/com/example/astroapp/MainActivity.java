package com.example.astroapp;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    static Bundle sunData;
    static Bundle moonData;
    static String[] stringsToBundleSun;
    static String[] stringsToBundleMoon;
    String LatDeg ="51";
    String LatMin = "45";
    String LatD = "N";
    String LonDeg = "19";
    String LonMin = "28";
    String LonD = "E";
    String interval = "2s";
    public static TextView time, coordsWidgetTextViewV;

    static ViewPagerAdapter viewPagerAdapter;
    public static int orientation;
    public static int smallestScreenWidth;
    public static double[] calculatedCoords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        orientation = getResources().getConfiguration().orientation;
        smallestScreenWidth = getResources().getConfiguration().smallestScreenWidthDp;

        calculatedCoords = settings.convertFromNSEW(LatD,LatDeg,LatMin,LonD,LonDeg,LonMin);

        int[] currTime=settings.getCurrentTime();

        stringsToBundleSun = AstroCalculations.astroCalculations(calculatedCoords, currTime,"sun");
        stringsToBundleMoon = AstroCalculations.astroCalculations(calculatedCoords, currTime,"moon");

        sunData = Utils.putDataToBundle(stringsToBundleSun,"sun");
        moonData = Utils.putDataToBundle(stringsToBundleMoon,"moon");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        coordsWidgetTextViewV = (TextView)findViewById(R.id.coordsWidgetTextView);

        if(savedInstanceState != null){
            LatDeg =savedInstanceState.getString("LatDeg");
            LatMin=savedInstanceState.getString("LatMin");
            LatD=savedInstanceState.getString("LatD");
            LonDeg =savedInstanceState.getString("LonDeg");
            LonMin =savedInstanceState.getString("LonMin");
            LonD=savedInstanceState.getString("LonD");
            interval=savedInstanceState.getString("IntervalFromSettings");

        }
        //Refresher.myRunable(settings.handlers, settings.runnables, interval, calculatedCoords);
        coordsWidgetTextViewV.setText(LatDeg+"°"+LatMin+"'"+LatD+"  "+ LonDeg+"°"+LonMin+"'"+LonD);
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

    public void openSettingsActivity()
    {
        Intent intent = new Intent(this, settings.class);
        startActivity(intent);
    }

    @Override protected void onDestroy() {
        // TODO Auto-generated method stub
        if (settings.handlers.size()>0 && settings.runnables.size()>0) {
            settings.handlers.get(settings.handlers.size() - 1).removeCallbacks(settings.runnables.get(settings.runnables.size() - 1));
        }
        super.onDestroy();
    }

    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("LatDeg",settings.LatDeg);
        outState.putString("LatMin",settings.LatMin);
        outState.putString("LatD",settings.LatD);
        outState.putString("LonDeg",settings.LonDeg);
        outState.putString("LonMin",settings.LonMin);
        outState.putString("LonD",settings.LonD);
        outState.putString("IntervalFromSettings",settings.interval);
    }

    @Override protected void onPause() {
        super.onPause();
        Refresher.clearRefresherThread(settings.handlers,settings.runnables);
    }

    @Override protected void onResume() {
        if(settings.checkIfButton){
            Refresher.clearRefresherThread(settings.handlers,settings.runnables);
            Refresher.myRunable(settings.handlers, settings.runnables, settings.interval, settings.calculatedCoords);
        }else{
            Refresher.clearRefresherThread(settings.handlers,settings.runnables);
            Refresher.myRunable(settings.handlers, settings.runnables, interval, calculatedCoords);
        }
        super.onResume();
    }
}
