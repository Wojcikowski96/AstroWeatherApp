package com.example.astroapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class settings extends AppCompatActivity {
    EditText LatDegrees, LatMinutes, LonDegrees, LonMinutes;
    public static String LatDeg ="51";
    public static String LatMin = "45";
    public static String LatD = "N";
    public static String LonDeg = "19";
    public static String LonMin = "28";
    public static String LonD = "E";
    Button Submit;
    public static double[] calculatedCoords;
    int LatDSelection;
    Spinner LatDir,LonDir, freqSpinner;
    int LonDSelection;
    int intervalSelection;
    public static List<Handler> handlers = new ArrayList<>();
    public static List<Runnable> runnables = new ArrayList<>();
    public static String[] sunStrings;
    public static String[] moonStrings;
    public static boolean checkIfButton = false;
    public static String interval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("On create settings");
        setContentView(R.layout.activity_settings);

        LatDir = findViewById(R.id.latSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.LatDirs, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        LatDir.setAdapter(adapter);

        LonDir = findViewById(R.id.lonSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.LonDirs, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        LonDir.setAdapter(adapter2);

        freqSpinner = findViewById(R.id.freqSpinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,R.array.updateIntervals, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        freqSpinner.setAdapter(adapter3);

        LatDegrees= (EditText) findViewById(R.id.LatDegrees);
        LatMinutes= (EditText) findViewById(R.id.LatMinutes);
        LonDegrees= (EditText) findViewById(R.id.LonDegrees);
        LonMinutes= (EditText) findViewById(R.id.LonMinutes);
        Submit= (Button) findViewById(R.id.Submit);

        if(savedInstanceState != null){
            LatDeg =savedInstanceState.getString("LatDeg");
            LatMin=savedInstanceState.getString("LatMin");
            LatD=savedInstanceState.getString("LatD");

            LonDeg =savedInstanceState.getString("LonDeg");
            LonMin =savedInstanceState.getString("LonMin");
            LonD=savedInstanceState.getString("LonD");

            interval=savedInstanceState.getString("Interval");
            System.out.println("Interval "+interval);

            System.out.println("Interval Selection "+intervalSelection);
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

        System.out.println("Deg dla Lat "+LatDeg);
        System.out.println("Min dla Lat "+LatMin);
        System.out.println("Dir dla Lat "+LatD);
        System.out.println("Deg dla Lon "+LonDeg);
        System.out.println("Min dla Lon "+LonMin);
        System.out.println("Dir dla Lon "+LonD);
        System.out.println("Interval "+interval);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfButton=true;
                String[] coords = getTextFromSettings(LatDegrees, LatMinutes, LatDir, LonDegrees, LonMinutes, LonDir);

                boolean checkIfError = Utils.checkForInputErrors(coords);
                boolean checkForRange = true;

                if(!checkIfError){
                    Context c = getApplicationContext();
                    Toast.makeText(c, "Błędny typ danych",
                            Toast.LENGTH_LONG).show();
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

                    Sun sun = (Sun) MainActivity.viewPagerAdapter.getItem(0);
                    sun.update(sunStrings);

                    Moon moon = (Moon) MainActivity.viewPagerAdapter.getItem(1);
                    moon.update(moonStrings);

                    Refresher.clearRefresherThread(handlers,runnables);

                    interval = freqSpinner.getSelectedItem().toString();

                    Refresher.myRunable(handlers, runnables, interval, calculatedCoords);

                    MainActivity.coordsWidgetTextViewV.setText(LatDeg+"°"+LatMin+"'"+LatD+"  "+ LonDeg+"°"+LonMin+"'"+LonD);
                }}


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
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        System.out.println("Niszczę settings activity");
    }

    @Override protected void onPause() {
        super.onPause();
        System.out.println("Pauzuję settings activity");
    }

    @Override protected void onStop() {
        super.onStop();
        System.out.println("Stopuję settings activity");
    }
}
