package com.example.astroapp;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class Sun extends Fragment
{

    public static String sunRiseTime, sunRiseAzimuth, sunSetTime,sunSetAzimuth,sunRiseTwilightTime, sunSetTwilightTime;
    private TextView sunRiseTextView;
    private TextView sunRiseAzimuthTextView;
    private TextView sunSetTextView;
    private TextView sunSetAzimuthTextView;
    private TextView sunRiseTwilightTextView;
    private TextView sunSetTwilightTextView;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        System.out.println("On create sun");
        // Inflate the layout for this fragment
        if(getArguments()!=null){
            sunRiseTime = getArguments().getString("sunrisetime");
            sunRiseAzimuth = getArguments().getString("sunriseazimuth");
            sunSetTime = getArguments().getString("sunsettime");
            sunSetAzimuth = getArguments().getString("sunsetazimuth");
            sunRiseTwilightTime = getArguments().getString("twilightmorning");
            sunSetTwilightTime = getArguments().getString("twilightevening");
        }

        if(savedInstanceState!=null){
            sunRiseTime = savedInstanceState.getString("sunrisetime");
            sunRiseAzimuth = savedInstanceState.getString("sunriseazimuth");
            sunSetTime = savedInstanceState.getString("sunsettime");
            sunSetAzimuth = savedInstanceState.getString("sunsetazimuth");
            sunRiseTwilightTime = savedInstanceState.getString("twilightmorning");
            sunSetTwilightTime = savedInstanceState.getString("twilightevening");
        }

        View view = inflater.inflate(R.layout.sun, container, false);

        sunRiseTextView = (TextView) view.findViewById(R.id.sunRiseTextView);
        sunRiseAzimuthTextView = (TextView) view.findViewById(R.id.sunRiseAzimuthTextView);
        sunSetTextView = (TextView) view.findViewById(R.id.sunSetTextView);
        sunSetAzimuthTextView = (TextView) view.findViewById(R.id.sunSetAzimuthTextView);
        sunRiseTwilightTextView = (TextView) view.findViewById(R.id.sunRiseTwilightTextView);
        sunSetTwilightTextView = (TextView) view.findViewById(R.id.sunSetTwilightTextView);

        sunRiseTextView.setText("Wschód:  " +sunRiseTime);
        sunRiseAzimuthTextView.setText("Azymut wschodu:  " +sunRiseAzimuth+"°");
        sunSetTextView.setText("Zachód:  " +sunSetTime);
        sunSetAzimuthTextView.setText("Azymut zachodu:  " +sunSetAzimuth+"°");
        sunRiseTwilightTextView.setText("Świt cywilny:  " +sunRiseTwilightTime);
        sunSetTwilightTextView.setText("Zmierzch cywilny:  " +sunSetTwilightTime);

        return view;
    }

    void update(String[] sunStrings) {

        sunRiseTime=sunStrings[0];
        sunRiseAzimuth=sunStrings[1];
        sunSetTime=sunStrings[2];
        sunSetAzimuth=sunStrings[3];
        sunRiseTwilightTime=sunStrings[4];
        sunSetTwilightTime=sunStrings[5];

        sunRiseTextView.setText("Wschód:  " +sunRiseTime);
        sunRiseAzimuthTextView.setText("Azymut wschodu:  " +sunRiseAzimuth+"°");
        sunSetTextView.setText("Zachód:  " +sunSetTime);
        sunSetAzimuthTextView.setText("Azymut zachodu:  " +sunSetAzimuth+"°");
        sunRiseTwilightTextView.setText("Świt cywilny:  " +sunRiseTwilightTime);
        sunSetTwilightTextView.setText("Zmierzch cywilny:  " +sunSetTwilightTime);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        System.out.println("Niszczę sun activity");
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("sunrisetime",sunRiseTime);
        outState.putString("sunriseazimuth",sunRiseAzimuth);
        outState.putString("sunsettime",sunSetTime);
        outState.putString("sunsetazimuth",sunSetAzimuth);
        outState.putString("twilightmorning",sunRiseTwilightTime);
        outState.putString("twilightevening",sunSetTwilightTime);
    }
}
