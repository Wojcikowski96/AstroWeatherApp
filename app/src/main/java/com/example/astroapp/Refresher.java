package com.example.astroapp;

import android.os.Handler;

import java.util.List;

public class Refresher {

    public static void clearRefresherThread(List<Handler> h, List<Runnable> r) {

        if (h.size()>0 && r.size()>0) {
            h.get(h.size() - 1).removeCallbacks(r.get(r.size() - 1));
            h.clear();
            r.clear();
        }
    }

    public static void myRunable(List<Handler> h, List<Runnable> r, String interval, final double[] c) {
        final Handler refreshHandler = new Handler();

        h.add(refreshHandler);

        int delay = 0;

        if(interval.charAt(interval.length()-1)=='s'){
            delay = Integer.parseInt(interval.substring(0,interval.length()-1));
            System.out.println("liczba do przekazania" + delay);
        }else{
            delay = Integer.parseInt(interval.substring(0,interval.length()-3)) * 60;
            System.out.println("liczba sekund z minut do przekazania" + delay);
        }
        final int finalDelay = delay;

        Runnable runnable = new Runnable() {

            @Override
            public void run() {

                int[] currTime=settings.getCurrentTime();
                String[] sunStrings = AstroCalculations.astroCalculations(c, currTime,"sun");
                String[] moonStrings = AstroCalculations.astroCalculations(c, currTime,"moon");

                Sun sun = (Sun) MainActivity.viewPagerAdapter.getItem(0);
                sun.update(sunStrings);

                Moon moon = (Moon) MainActivity.viewPagerAdapter.getItem(1);
                moon.update(moonStrings);

                refreshHandler.postDelayed(this, finalDelay * 1000);
            }
        };
        r.add(runnable);
        refreshHandler.postDelayed(runnable, delay * 1000);
    }
}
