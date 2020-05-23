package com.example.astroapp;

import android.os.Bundle;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

public class AstroCalculations {
    public static String[] astroCalculations(double [] calculatedCoords, int [] currTime, String type) {

        String[] astroData = new String[6];
        int hour, minute;

        AstroCalculator.Location l = new AstroCalculator.Location(calculatedCoords[0], calculatedCoords[1]);

        AstroDateTime t = new AstroDateTime(currTime[0], currTime[1], currTime[2], currTime[3], currTime[4], currTime[5], currTime[6],true);

        AstroCalculator a = new AstroCalculator(t,l);

        if(type.equals("sun")){

            hour=a.getSunInfo().getSunrise().getHour();
            minute =a.getSunInfo().getSunrise().getMinute();
            String sunRiseTime = Utils.formatTime( hour, minute);
            astroData[0]=sunRiseTime;

            double sunRiseAzimunth = Math.round(a.getSunInfo().getAzimuthRise()*100d)/100d;
            astroData[1]=String.valueOf(sunRiseAzimunth);

            hour = a.getSunInfo().getSunset().getHour();
            minute = a.getSunInfo().getSunset().getMinute();

            String sunSetTime = Utils.formatTime(hour,minute);
            astroData[2]=sunSetTime;

            double sunSetAzimuth = Math.round(a.getSunInfo().getAzimuthSet()*100d)/100d;
            astroData[3]=String.valueOf(sunSetAzimuth);

            hour=a.getSunInfo().getTwilightMorning().getHour();
            minute=a.getSunInfo().getTwilightMorning().getMinute();

            String twilightMorningTime = Utils.formatTime(hour,minute);
            astroData[4]=twilightMorningTime;

            hour=a.getSunInfo().getTwilightEvening().getHour();
            minute=a.getSunInfo().getTwilightEvening().getMinute();

            String twilightEveningTime= Utils.formatTime(hour,minute);
            astroData[5]=twilightEveningTime;

        }else if(type.equals("moon")){

            hour=a.getMoonInfo().getMoonrise().getHour();
            minute=a.getMoonInfo().getMoonrise().getMinute();

            String result=Utils.formatTime(hour,minute);
            astroData[0]=result;

            hour=a.getMoonInfo().getMoonset().getHour();
            minute=a.getMoonInfo().getMoonset().getMinute();

            String result2=Utils.formatTime(hour,minute);
            astroData[1]=result2;

            int d=a.getMoonInfo().getNextNewMoon().getDay();
            int m=a.getMoonInfo().getNextNewMoon().getMonth();
            int y=a.getMoonInfo().getNextNewMoon().getYear();

            String nextNewMoonDate=Utils.formatDate(y,m,d);
            astroData[2]=nextNewMoonDate;

            d=a.getMoonInfo().getNextFullMoon().getDay();
            m=a.getMoonInfo().getNextFullMoon().getMonth();
            y=a.getMoonInfo().getNextFullMoon().getYear();

            String nextFullMoonDate=Utils.formatDate(y,m,d);
            astroData[3]=nextFullMoonDate;

            String phase = (double) Math.round(a.getMoonInfo().getIllumination() * 10000d) / 100d +"%";
            astroData[4]=phase;

            String age = String.valueOf((double) Math.round(a.getMoonInfo().getAge() * 100d) / 100d);
            astroData[5]=age;
        }
        return astroData;
    }
}
