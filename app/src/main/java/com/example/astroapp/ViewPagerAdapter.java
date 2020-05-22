package com.example.astroapp;
import android.content.res.Configuration;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Map<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();

    ViewPagerAdapter(FragmentManager fm) {

        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        Sun sun = new Sun();
        sun.setArguments(MainActivity.sunData);
        fragments.put(0, sun);
        Moon moon = new Moon();
        moon.setArguments(MainActivity.moonData);
        fragments.put(1, moon);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
        fragments.remove(position);
    }

    @Override
    public int getCount() {
        return fragments.size(); //2 fragments
    }

    @Override
    public float getPageWidth(int position) {

        if (MainActivity.orientation == Configuration.ORIENTATION_LANDSCAPE || MainActivity.smallestScreenWidth >=600) {
            return(0.5f);
        } else {
            return(1.0f);
        }
    }
}
