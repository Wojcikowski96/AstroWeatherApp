package com.example.astroapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.astroapp.R;

public class Moon extends Fragment {
    TextView moonRiseTimeTextView;
    TextView moonSetTimeTextView ;
    TextView nextNewMoonTextView;
    TextView nextFullMoonTextView;
    TextView moonPhaseTextView;
    TextView synodicMonthTextView;
    private String moonRiseTime, moonSetTime, nextNewMoon, nextFullMoon, moonPhase, synodicMonth;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        System.out.println("On create moon");
        if(getArguments()!=null){
            moonRiseTime = getArguments().getString("moonrisetime");
            moonSetTime = getArguments().getString("moonsettime");
            nextNewMoon = getArguments().getString("nextnewmoon");
            nextFullMoon = getArguments().getString("nextfullmoon");
            moonPhase = getArguments().getString("moonphase");
            synodicMonth = getArguments().getString("synodicmonth");
        }

        if(savedInstanceState!=null){
            moonRiseTime = savedInstanceState.getString("moonrisetime");
            moonSetTime = savedInstanceState.getString("moonsettime");
            nextNewMoon = savedInstanceState.getString("nextnewmoon");
            nextFullMoon = savedInstanceState.getString("nextfullmoon");
            moonPhase = savedInstanceState.getString("moonphase");
            synodicMonth = savedInstanceState.getString("synodicmonth");
        }

        View view = inflater.inflate(R.layout.moon, container, false);

        moonRiseTimeTextView = (TextView) view.findViewById(R.id.moonRiseTimeTextView);
        moonSetTimeTextView = (TextView) view.findViewById(R.id.moonSetTimeTextView);
        nextNewMoonTextView = (TextView) view.findViewById(R.id.nextNewMoonTextView);
        nextFullMoonTextView = (TextView) view.findViewById(R.id.nextFullMoonTextView);
        moonPhaseTextView = (TextView) view.findViewById(R.id.moonPhaseTextView);
        synodicMonthTextView = (TextView) view.findViewById(R.id.synodicMonthTextView);

        moonRiseTimeTextView.setText("Wschód: "+moonRiseTime);
        moonSetTimeTextView.setText("Zachód: "+moonSetTime);
        nextNewMoonTextView.setText("Najbliższy nów: "+nextNewMoon);
        nextFullMoonTextView.setText("Najbliższa pełnia: "+nextFullMoon);
        moonPhaseTextView.setText("Aktualna faza: "+moonPhase);
        synodicMonthTextView.setText("Dzień miesiąca synodycznego: "+synodicMonth);

        return view;
    }
    public void update(String[] moonStrings) {

        moonRiseTime = moonStrings[0];
        moonSetTime = moonStrings[1];
        nextNewMoon = moonStrings[2];
        nextFullMoon = moonStrings[3];
        moonPhase = moonStrings[4];
        synodicMonth = moonStrings[5];

        moonRiseTimeTextView.setText("Wschód:  " +moonRiseTime);
        moonSetTimeTextView.setText("Zachód:  " +moonSetTime);
        nextNewMoonTextView.setText("Najbliższy nów:  " +nextNewMoon);
        nextFullMoonTextView.setText("Najbliższa pełnia:  " +nextFullMoon);
        moonPhaseTextView.setText("Aktualna faza:  " +moonPhase);
        synodicMonthTextView.setText("Dzień miesiąca synodycznego:  " +synodicMonth);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        System.out.println("Niszczę moon activity");
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("moonrisetime",moonRiseTime);
        outState.putString("moonsettime",moonSetTime);
        outState.putString("nextnewmoon",nextNewMoon);
        outState.putString("nextfullmoon",nextFullMoon);
        outState.putString("moonphase",moonPhase);
        outState.putString("synodicmonth",synodicMonth);
    }

}